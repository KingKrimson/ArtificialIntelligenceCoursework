/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import helpers.FitnessFunctions;
import helpers.SelectionAlgorithms;
import helpers.SimpleResultWriter;
import helpers.GenomeHelper;
import individuals.CandidateSolution;
import individuals.BinaryCandidateSolution;
import individuals.LookupCandidateSolution;
import individuals.MLPCandidateSolution;
import individuals.BinaryRuleSetCandidateSolution;
import individuals.RealRuleSetCandidateSolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ad3-brown
 */

// POTENTIAL CHANGES:
// Tournament size change (15 vs 2) - DONE, PARAMETERISABLE
// Crossover rate change 0.9 - DONE
// keep top 5%
// change rule size dynamically?
// Stop sort in fitness (<- YES, IT'S SHITTY) and shuffle instead? - STOPPED RANDOMISATION. DO SHUFFLE?
// Sort out shitty randomisation and streamline it and stuff.
// Change occurance of wildcards?

public class GeneticAlgorithm {

    static int POP = 300; // How many individuals are in the population.
    static int G_LENGTH = 10; // length of genome. For rules, this is modified a bit. Ignored for MLP, different way of calculating length.
    static int NUM_GENERATIONS = 10000; //MAXIMUM number of generations. May stop beforehand.
    static int STOP_GENERATIONS = 0; // number of generations to stop after max fitness has been acheived.
    static int TOURNAMENT_SIZE = 20; // size of tournament.
    static double M_RATE = (double)1/G_LENGTH; // Mutation rate. Inverse of gene length. For rules, multiplied so it mutates bit strings.
    static double ANN_M_RATE = 0.2; // Mutation rate for neural network.
    static double C_RATE = 1.0; // Crossover rate. Sometimes two parents are just added back into the pool, instead of their children.
    static double PERCENT_TO_KEEP = 0.05; // percentage of best parents to keep in the pool. NOT IMPLEMENTED YET
    static double TRAINING_POP = 0.8; // percent of dataset to train on.
    static boolean FULL_PRINT = false; // verbose mode.
    
    static int NUMBER_OF_HIDDEN_NODES = 5; // number of nodes in hidden layer of MLP.

    public enum FitnessType {
        TOTAL_VALUE, LOOKUP_TABLE, RULE_SET_INT, RULE_SET_REAL, MLP
    }

    public enum GenomeType {
        BIT, RULE_SET, MLP
    }
    
    public enum SelectionType {
        TOURNAMENT, ROULETTE
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //prototypeSet(FitnessType.TOTAL_VALUE, GenomeType.BIT, SelectionType.TOURNAMENT);
            //dataSet1(FitnessType.LOOKUP_TABLE, GenomeType.BIT, SelectionType.TOURNAMENT);
            //dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
            //dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
            //dataSet3(FitnessType.MLP, GenomeType.MLP, SelectionType.TOURNAMENT);
            
//            int noPassed = 0;
//            int noFailed = 0;        
//            for (int i = 0; i < 20; i++) {
//                boolean passed = dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
//                if (passed) {
//                    noPassed++;
//                } else {
//                    noFailed++;
//                }
//            }
//            System.out.println("NUMBER PASSED: " + noPassed + " NUMBER FAILED " + noFailed);
            
            dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
         
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void prototypeSet(FitnessType fit, GenomeType genome, SelectionType sel) {
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);

        for (int i = 0; i < POP; i++) {
            CandidateSolution individual = new BinaryCandidateSolution(GenomeHelper.generateBitGenome(G_LENGTH));
            FitnessFunctions.calculateFitnessTotalValue(individual);
            initialGeneration.add(individual);
        }

        geneticAlgorithm(initialGeneration, null, fit, sel);
    }
    
    // BEST SETTINGS SO FAR: POP 100, G_LENGTH 10, STOP_GENERATIONS 100
    public static boolean dataSet1(FitnessType fit, GenomeType genome, SelectionType sel) 
        throws FileNotFoundException{
        TreeMap<String, String> fullLookup = readData("data1.txt"); 
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData  = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        switch (genome) {
                case BIT:
                    // in this case, the data can't be seperated 
                    // without breaking the training, as the lookup method is
                    // just a noddy example.
                    trainingData = new TreeMap<>(fullLookup);
                    realData = new TreeMap<>(fullLookup);
                    break; 
                case RULE_SET:
                    TreeMap[] maps = getTrainingData(fullLookup, TRAINING_POP);
                    trainingData = maps[0];
                    realData = maps[1];
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be BIT or RULE_SET. Actual type: " + genome.name());
        }
        
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case BIT:
                    individual = new LookupCandidateSolution(GenomeHelper.generateBitGenome(64));
                    FitnessFunctions.calculateFitnessLookupTable(individual, trainingData);
                    break;
                case RULE_SET:
                    individual = new BinaryRuleSetCandidateSolution(GenomeHelper.generateBinaryRuleGenome(G_LENGTH, 7)); // generate G_LENGTH rules.
                    FitnessFunctions.calculateFitnessBinaryRuleSet(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be BIT or RULE_SET. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel); // train;
        return testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
    }
    
    // BEST SETTINGS SO FAR: POP 100, G_LENGTH 20, STOP_GENERATIONS 100.
    public static boolean dataSet2(FitnessType fit, GenomeType genome, SelectionType sel) 
    throws FileNotFoundException {
        TreeMap<String, String> fullLookup = readData("data2.txt"); 
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData  = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        switch (genome) {
            case RULE_SET:
            case MLP:
                TreeMap[] maps = getTrainingData(fullLookup, TRAINING_POP);
                trainingData = maps[0];
                realData = maps[1];
                break;
            default:
                throw new RuntimeException("dataSet2 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
        }
        
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case RULE_SET:
                    individual = new BinaryRuleSetCandidateSolution(GenomeHelper.generateBinaryRuleGenome(G_LENGTH, 12)); // generate G_LENGTH rules.
                    FitnessFunctions.calculateFitnessBinaryRuleSet(individual, trainingData);
                    break;
                case MLP:
                    // what limits should the weights have? How many weights should I have?
                    individual = new MLPCandidateSolution(GenomeHelper.generateDoubleGenome(0, 0, 0));
                    FitnessFunctions.calculateFitnessMLP(individual, trainingData, NUMBER_OF_HIDDEN_NODES);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel);
        return testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
    }
            
    public static boolean dataSet3(FitnessType fit, GenomeType genome, SelectionType sel) 
    throws FileNotFoundException {
        TreeMap<String, String> fullLookup = readData("data3.txt"); 
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData  = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        switch (genome) {
            case MLP:
            case RULE_SET:
                TreeMap[] maps = getTrainingData(fullLookup, 0.8);
                trainingData = maps[0];
                realData = maps[1];
                break;
            default:
                throw new RuntimeException("dataSet3 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
        }
        
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case MLP:
                    // 7 == number of inputs + bias. each input connects to a hidden node.
                    // each hidden node has one connection to the output node.
                    int numWeights = (7 * NUMBER_OF_HIDDEN_NODES) + NUMBER_OF_HIDDEN_NODES;
                    individual = new MLPCandidateSolution(GenomeHelper.generateDoubleGenome(numWeights, -1, 1));
                    FitnessFunctions.calculateFitnessMLP(individual, trainingData, NUMBER_OF_HIDDEN_NODES);
                    break;
                case RULE_SET:
                    individual = new RealRuleSetCandidateSolution(GenomeHelper.generateRealRuleGenome(G_LENGTH, 13));
                    FitnessFunctions.calculateFitnessRealRuleSet(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet3 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel);
        return testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
    }
    
    

    public static CandidateSolution geneticAlgorithm(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit, SelectionType sel) {
        try {
            SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, true);
            resultWriter.write(0, oldGeneration);

            ArrayList<CandidateSolution> newGeneration;
            int bestPossible = lookup.size();
            int bestFitness = 0;
            int fitnessGens = 0;
            int count = 0;
            boolean reachedMaxFitness = false;

            int overallBestFitness = 0;
            for (int i = 0; i < NUM_GENERATIONS; i++) {
                newGeneration = newGeneration(oldGeneration, PERCENT_TO_KEEP, lookup, fit, sel);
                bestFitness = resultWriter.write((i + 1), newGeneration);
                oldGeneration = newGeneration;
                
                if (reachedMaxFitness) {
                    ++count;
                    if (count > STOP_GENERATIONS && bestFitness == bestPossible) {
                        break;
                    }
                } else if (bestFitness == bestPossible) {
                    reachedMaxFitness = true;
                    if (STOP_GENERATIONS == 0) {
                        break;
                    }
                }
                
                if (bestFitness == overallBestFitness) {
                    fitnessGens++;
                } else {
                    fitnessGens = 0;
                    overallBestFitness = bestFitness;
                }
                if (fitnessGens > 2000) {
                    break;
                }
            }
            
            resultWriter.close();
            
            CandidateSolution bestSolution = null;
            for (CandidateSolution individual : oldGeneration) {
                if (bestSolution == null || bestSolution.getFitness() <= individual.getFitness()) {
                    bestSolution = individual;
                }
            }
            return bestSolution;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<CandidateSolution> newGeneration(ArrayList<CandidateSolution> oldGeneration, double percentToKeep, TreeMap<String, String> lookup, FitnessType fit, SelectionType sel) {
        ArrayList<CandidateSolution> parents;
        ArrayList<CandidateSolution> newGeneration = new ArrayList<>();
        
        
        // keep the best solutions, as determined by PERCENT_TO_KEEP
        int numberToKeep = (int)(PERCENT_TO_KEEP * oldGeneration.size());
        ArrayList<CandidateSolution> best = new ArrayList<>();
        if (numberToKeep > 0) {
            ArrayList<CandidateSolution> sortedGeneration = new ArrayList<>(oldGeneration);
            Collections.sort(sortedGeneration);
            Collections.reverse(sortedGeneration);
            for (int i = 0; i < numberToKeep; i++) {
                newGeneration.add(sortedGeneration.get(i));
                best.add(sortedGeneration.get(i));
            }
        }
         
        switch (sel) {
            case TOURNAMENT:
                parents = SelectionAlgorithms.tournamentSelection(oldGeneration, POP, TOURNAMENT_SIZE);
                break;
            case ROULETTE:
                parents = SelectionAlgorithms.rouletteSelection(oldGeneration);
                break;
            default:
                throw new RuntimeException("Please choose from TOURNAMENT or ROULETTE selection.");
        }

        Random rand = new Random();
        
        double mutationRate = (fit == FitnessType.RULE_SET_INT) ? (M_RATE / (parents.get(0).getGenome().size()/G_LENGTH)) : M_RATE;
        if (fit == FitnessType.RULE_SET_INT || fit == FitnessType.RULE_SET_REAL) {
            mutationRate = ANN_M_RATE;
        } else if (fit == FitnessType.MLP) {
            mutationRate = M_RATE / (parents.get(0).getGenome().size()/G_LENGTH);
        } else {
            mutationRate = M_RATE;
        }
        int popLimit = parents.size() - numberToKeep;
        
        // change to allow for odd populations?
        for (int i = 0; i < popLimit; i = i + 2) {

            CandidateSolution[] currentParents = new CandidateSolution[2];
            currentParents[0] = parents.get(i);
            currentParents[1] = parents.get(i + 1);
            int point = rand.nextInt(currentParents[0].getSize());
            double crossoverRate = rand.nextFloat();
            for (int j = 0; j <= 1; j++) {
                int k = (j == 0) ? 1 : 0; // j == 0, k == 1 and j == 1, k == 0.
                CandidateSolution child;
                if (crossoverRate < C_RATE) {
                    child = currentParents[j].crossover(point, currentParents[k]);
                } else {
                    child = currentParents[j];
                }
                
                child.mutation(mutationRate);
                switch (fit) {
                    case TOTAL_VALUE:
                        FitnessFunctions.calculateFitnessTotalValue(child);
                        break;
                    case LOOKUP_TABLE:
                        FitnessFunctions.calculateFitnessLookupTable(child, lookup);
                        break;
                    case RULE_SET_INT:
                        FitnessFunctions.calculateFitnessBinaryRuleSet(child, lookup);
                        break;
                    case RULE_SET_REAL:
                        FitnessFunctions.calculateFitnessRealRuleSet(child, lookup);
                        break;
                    case MLP:
                        FitnessFunctions.calculateFitnessMLP(child, lookup, NUMBER_OF_HIDDEN_NODES);
                        break;
                }
                newGeneration.add(child);
            }

        }

        Collections.shuffle(newGeneration);
        return newGeneration;
    }
    
    public static TreeMap<String, String> readData(String name) 
        throws FileNotFoundException {
        File file = new File("data/" + name).getAbsoluteFile();
        Scanner scan = new Scanner(file);
        
        TreeMap<String, String> lookup = new TreeMap<>();
        
        if (scan.hasNextLine()) {
            scan.nextLine(); // get rid of header, I don't need it.
        }
        
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] words = line.split(" "); // split on space.
            String key = "";
            for (int i = 0; i < words.length; i++) {
                if (i == (words.length - 1)) {
                    key = key.trim();
                    lookup.put(key, words[i]);
                } else {
                    key += words[i] + " ";
                }
            }
        }
        return lookup;
    }
    
    public static TreeMap<String, String>[] getTrainingData(TreeMap fullLookup, double percentage) {
        TreeMap<String, String> trainingData = new TreeMap(fullLookup);
        TreeMap<String, String> realData = new TreeMap();
        
        int size = trainingData.size();
        int elements = (int) Math.ceil(size * percentage);
        if (elements < size) {
            int inverseElements = size - elements; //number of elements to delete from map.
            Random rand = new Random();
            for (int i = 0; i < inverseElements; i++) {
                Object[] keyArray = trainingData.keySet().toArray();
                String doomedKey = (String) keyArray[rand.nextInt(keyArray.length)];
                String doomedValue = trainingData.remove(doomedKey);
                realData.put(doomedKey, doomedValue);
            }
        }
        TreeMap[] maps = new TreeMap[2];
        maps[0] = trainingData;
        maps[1] = realData;
        
        return maps;
    }
    
    public static boolean testRealData(CandidateSolution bestSolution, TreeMap<String, String> realData, TreeMap<String, String> trainingData, FitnessType fit) {
        boolean pass = true;
        int realPassed = 0;
        int trainingPassed = 0;
        
        switch (fit) {
            case LOOKUP_TABLE:
                realPassed = FitnessFunctions.calculateFitnessLookupTable(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessLookupTable(bestSolution, trainingData);
                break;
            case RULE_SET_INT:
                realPassed = FitnessFunctions.calculateFitnessBinaryRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessBinaryRuleSet(bestSolution, trainingData);
                break;
            case RULE_SET_REAL:
                realPassed = FitnessFunctions.calculateFitnessRealRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessRealRuleSet(bestSolution, trainingData);
                break;
            case MLP:
                realPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, realData, NUMBER_OF_HIDDEN_NODES);
                trainingPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, trainingData, NUMBER_OF_HIDDEN_NODES);
                break;
            default:
                System.out.println("Cannot test fitness type: " + fit.name());
                break;
        }
        
        int totalPassed = realPassed + trainingPassed;
        
        int realFailed = realData.size() - realPassed;
        int trainingFailed = trainingData.size() - trainingPassed;
        int totalFailed = realFailed + trainingFailed;
        
        DecimalFormat df = new DecimalFormat("#.##");
        
        String realPercentagePass = df.format((double)realPassed/realData.size() * 100);
        String realPercentageFail = df.format(100 - Double.valueOf(realPercentagePass));
        String trainingPercentagePass = df.format((double)trainingPassed/trainingData.size() * 100);
        String trainingPercentageFail = df.format(100 - Double.valueOf(trainingPercentagePass));
        String totalPercentagePass = df.format((double)totalPassed/(trainingData.size()+realData.size()) * 100);
        String totalPercentageFail = df.format(100 - Double.valueOf(totalPercentagePass));
        
        System.out.println("\nFinal Results:\n");
        System.out.println("Real data:\nPASS: " + realPassed + "\nFAIL: " + realFailed + "\nTOTAL: " + realData.size());
        System.out.println("PERCENT PASSED: " + realPercentagePass + "%\nPERCENT FAILED: " + realPercentageFail + "%");
        System.out.println("");
        System.out.println("Training data:\nPASS: " + trainingPassed + "\nFAIL: " + trainingFailed + "\nTOTAL: " + trainingData.size());
        System.out.println("PERCENT PASSED: " + trainingPercentagePass + "%\nPERCENT FAILED: " + trainingPercentageFail + "%");
        System.out.println("");
        System.out.println("Total data:\nPASS: " + totalPassed + "\nFAIL: " + totalFailed + "\nTOTAL: " + (realData.size() + trainingData.size()));
        System.out.println("PERCENT PASSED: " + totalPercentagePass + "%\nPERCENT FAILED: " + totalPercentageFail + "%");
        System.out.println("");
        
        if (totalFailed != 0) {
            System.out.println("FAIL :(");
            pass = false;
        } else {
            System.out.println("PASS!!!");
        }
        
        return pass;
    }
}
