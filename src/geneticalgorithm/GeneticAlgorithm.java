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
import individuals.RuleSetCandidateSolution;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ad3-brown
 */
public class GeneticAlgorithm {

    final static int POP = 100; // How many individuals are in the population.
    final static int G_LENGTH = 10; // length of genome. For rules, this is number of rules, not bits.
    final static int NUM_GENERATIONS = 100; // how many generations to run for? Considering adding a stop condition, so this many not stick around for long.
    final static double M_RATE = (double)1/G_LENGTH; // Mutation rate. Inverse of gene length. For rules, multiplied so it mutates bit strings.
    final static boolean FULL_PRINT = false; // verbose mode.

    public enum FitnessType {

        TOTAL_VALUE, LOOKUP_TABLE, RULE_SET_INT, RULE_SET_FLOAT, MLP
    }

    public enum GenomeType {

        BIT, RULE_SET, MLP
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //prototypeSet(FitnessType.TOTAL_VALUE, GenomeType.BIT);
            //dataSet1(FitnessType.LOOKUP_TABLE, GenomeType.BIT);
            dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET);
            //dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET);
            //dataSet3(FitnessType.MLP, GenomeType.MLP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void prototypeSet(FitnessType fit, GenomeType genome) {
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);

        for (int i = 0; i < POP; i++) {
            CandidateSolution individual = new BinaryCandidateSolution(GenomeHelper.generateBitGenome(G_LENGTH));
            FitnessFunctions.calculateFitnessTotalValue(individual);
            initialGeneration.add(individual);
        }

        geneticAlgorithm(initialGeneration, null, fit);
    }
    
    public static void dataSet1(FitnessType fit, GenomeType genome) 
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
                    TreeMap[] maps = getTrainingData(fullLookup, 0.8);
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
                    individual = new RuleSetCandidateSolution(GenomeHelper.generateRuleGenome(G_LENGTH, 7)); // generate G_LENGTH rules.
                    FitnessFunctions.calculateFitnessRuleSet(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be BIT or RULE_SET. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit); // train;
        testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
    }
    
    public static void dataSet2(FitnessType fit, GenomeType genome) 
    throws FileNotFoundException {
        TreeMap<String, String> fullLookup = readData("data2.txt"); 
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData  = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        switch (genome) {
            case RULE_SET:
            case MLP:
                TreeMap[] maps = getTrainingData(fullLookup, 0.8);
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
                    individual = new RuleSetCandidateSolution(GenomeHelper.generateRuleGenome(G_LENGTH, 12)); // generate G_LENGTH rules.
                    FitnessFunctions.calculateFitnessLookupTable(individual, trainingData);
                    break;
                case MLP:
                    // what limits should the weights have? How many weights should I have?
                    individual = new MLPCandidateSolution(GenomeHelper.generateDoubleGenome(0, 0, 0));
                    FitnessFunctions.calculateFitnessMLP(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit);
        testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
    }
            
    public static void dataSet3(FitnessType fit, GenomeType genome) 
    throws FileNotFoundException {
        TreeMap<String, String> fullLookup = readData("data3.txt"); 
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData  = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        switch (genome) {
            case MLP:
                TreeMap[] maps = getTrainingData(fullLookup, 0.8);
                trainingData = maps[0];
                realData = maps[1];
                break;
            default:
                throw new RuntimeException("dataSet2 requires GenomeType to be MLP. Actual type: " + genome.name());
        }
        
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case MLP:
                    // what limits should the weights have? How many weights should I have?
                    individual = new MLPCandidateSolution(GenomeHelper.generateDoubleGenome(0, 0, 0));
                    FitnessFunctions.calculateFitnessMLP(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be MLP. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit);
        testRealData(bestSolution, realData, trainingData, fit); // test the best solution on the real data.
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

    public static CandidateSolution geneticAlgorithm(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit) {
        try {
            SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, true);
            resultWriter.write(0, oldGeneration);

            ArrayList<CandidateSolution> newGeneration;
            for (int i = 0; i < NUM_GENERATIONS; i++) {
                newGeneration = newGeneration(oldGeneration, lookup, fit);
                resultWriter.write((i + 1), newGeneration);
                oldGeneration = newGeneration;
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

    public static ArrayList<CandidateSolution> newGeneration(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit) {
        ArrayList<CandidateSolution> parents = SelectionAlgorithms.tournamentSelection(oldGeneration, POP);
        ArrayList<CandidateSolution> newGeneration = new ArrayList<>();
        Random rand = new Random();
        double mutation_rate = (fit == FitnessType.RULE_SET_INT) ? (M_RATE / (parents.get(0).getGenome().size()/G_LENGTH)) : M_RATE;
        
        for (int i = 0; i < parents.size(); i = i + 2) {
            CandidateSolution[] currentParents = new CandidateSolution[2];
            currentParents[0] = parents.get(i);
            currentParents[1] = parents.get(i + 1);
            int point = rand.nextInt(currentParents[0].getSize());
            for (int j = 0; j <= 1; j++) {
                int k = (j == 0) ? 1 : 0; // j == 0, k == 1 and j == 1, k == 0.
                CandidateSolution child = currentParents[j].crossover(point, currentParents[k]);
                
                child.mutation(mutation_rate);
                switch (fit) {
                    case TOTAL_VALUE:
                        FitnessFunctions.calculateFitnessTotalValue(child);
                        break;
                    case LOOKUP_TABLE:
                        FitnessFunctions.calculateFitnessLookupTable(child, lookup);
                        break;
                    case RULE_SET_INT:
                        FitnessFunctions.calculateFitnessRuleSet(child, lookup);
                        break;
                    case RULE_SET_FLOAT:
                        FitnessFunctions.calculateFitnessRuleSet(child, lookup);
                    case MLP:
                        FitnessFunctions.calculateFitnessMLP(child, lookup);
                        break;
                }
                newGeneration.add(child);
            }

        }

        return newGeneration;
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
                realPassed = FitnessFunctions.calculateFitnessRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessRuleSet(bestSolution, trainingData);
                break;
            case RULE_SET_FLOAT:
                realPassed = FitnessFunctions.calculateFitnessRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessRuleSet(bestSolution, trainingData);
                break;
            case MLP:
                realPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, trainingData);
                break;
            default:
                System.out.println("Cannot test fitness type: " + fit.name());
                break;
        }
        
        int totalPassed = realPassed + trainingPassed;
        
        int realFailed = realData.size() - realPassed;
        int trainingFailed = trainingData.size() - trainingPassed;
        int totalFailed = realFailed + trainingFailed;
        
        int realPercentagePass = (int)Math.ceil(((double)realPassed/realData.size()) * 100);
        int realPercentageFail = 100 - realPercentagePass;
        int trainingPercentagePass = (int)Math.ceil(((double)trainingPassed/trainingData.size()) * 100);
        int trainingPercentageFail = 100 - trainingPercentagePass;
        int totalPercentagePass = (int)Math.ceil(((double)totalPassed/(trainingData.size()+realData.size())) * 100);
        int totalPercentageFail = 100 - totalPercentagePass;
        
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
