/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import helpers.Averager;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Random;
import java.util.Scanner;
import rules.BinaryRuleSet;
import rules.RealRuleSet;
import rules.RuleSet;

/**
 *
 * @author ad3-brown
 */
// POTENTIAL CHANGES:
// Tournament size change (15 vs 2) - DONE, PARAMETERISABLE
// Crossover rate change ability - DONE
// keep top 5% - DONE
// change rule size dynamically? - Sort of done.
// Stop sort in fitness and shuffle instead? - STOPPED RANDOMISATION, NOW DO SHUFFLE INSTEAD.
// Sort out randomisation and streamline it and stuff. - Not done, but it seems to work fine...
// Change occurance of wildcards? - Done
public class GeneticAlgorithm {

    public static int POP = 100; // How many individuals are in the population.
    public static int G_LENGTH = 5; // length of genome. For rules, this is modified a bit. Ignored for MLP, different way of calculating length.
    public static int NUM_GENERATIONS = 10000; //MAXIMUM number of generations. May stop beforehand.
    public static int STOP_GENERATIONS = 0; // number of generations to stop after max fitness has been acheived. 0 stops straight away.
    public static int TOURNAMENT_SIZE = 10; // number of candidates in tournament.
    public static double M_RATE = (double) 1 / G_LENGTH; // Mutation rate. Typically inverse of gene length. For rules, manipulatied in candidate for different mutations.
    public static double ANN_M_RATE = 0.2; // Mutation rate for neural network.
    public static double C_RATE = 1.0; // Crossover rate. Sometimes two parents are just added back into the pool, instead of their children.
    public static double PERCENT_TO_KEEP = 0.05; // percentage of best parents to keep in the pool. NOT IMPLEMENTED YET
    public static double TRAINING_POP = 0.8; // percent of dataset to train on.
    public static boolean FULL_PRINT = false; // verbose mode.
    static int NUMBER_OF_HIDDEN_NODES = 5; // number of nodes in hidden layer of MLP.

    /**
     * The specific type of fitness function to use.
     */
    public enum FitnessType {

        TOTAL_VALUE,
        LOOKUP_TABLE,
        RULE_SET_INT,
        RULE_SET_REAL,
        MLP
    }

    /**
     * The generic genome type being used
     */
    public enum GenomeType {

        BIT,
        RULE_SET,
        MLP
    }

    /**
     * The selection algorithm to use when selecting a new generation
     */
    public enum SelectionType {

        TOURNAMENT,
        ROULETTE
    }

    /**
     * The entry point. Used to run the various data sets.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //prototypeSet(FitnessType.TOTAL_VALUE, GenomeType.BIT, SelectionType.TOURNAMENT);
            //dataSet1(FitnessType.LOOKUP_TABLE, GenomeType.BIT, SelectionType.TOURNAMENT);
            //dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
            //dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT);
            //dataSet3(FitnessType.MLP, GenomeType.MLP, SelectionType.TOURNAMENT);

            runDataSet1Fiddles();
            //runDataSet2Fiddles();
            //runDataSet3Fiddles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * repeatedly run dataset1 with different parameters, in order to generate
     * results.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void runDataSet1Fiddles()
            throws FileNotFoundException, IOException {
        int numRuns = 0;
        POP = 100;
        G_LENGTH = 5;
        TOURNAMENT_SIZE = 5;
        M_RATE = (double) 1 / G_LENGTH;
        
        Averager averageWriter = new Averager(52, 12);

        for (int i = 0; i < 20; i++) {
            dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "BASIC_TOURNAMENT_" + (i + 1));
            averageWriter.addResults("dataset1/results_BASIC_TOURNAMENT_" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset1/averages/results_BASIC_TOURNAMENT");

        for (int i = 0; i < 20; i++) {
            dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.ROULETTE, "BASIC_ROULETTE_" + (i + 1));
            averageWriter.addResults("dataset1/results_BASIC_ROULETTE_" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset1/averages/results_BASIC_ROULETTE");

        TOURNAMENT_SIZE = 4;
        for (int i = 0; i < 10; i++) {
            TOURNAMENT_SIZE += 1;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " TOURNAMENT_SIZE: " + TOURNAMENT_SIZE);
                dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j+1));
                averageWriter.addResults("dataset1/results_TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset1/averages/results_TOURNAMENT_" + TOURNAMENT_SIZE);
        }

        POP = 50;
        for (int i = 0; i < 10; i++) {
            POP += 10;
            TOURNAMENT_SIZE = (int) (POP * 0.1);
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " POP SIZE: " + POP);
                dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "POP_" + POP + "_" + (j+1));
                averageWriter.addResults("dataset1/results_POP_" + POP + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset1/averages/results_POP_" + POP);
        }

        POP = 100;
        M_RATE = (double) (1 / G_LENGTH);
        for (int i = 0; i < 10; i++) {
            M_RATE += 0.1;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " MUTATION RATE: " + M_RATE);
                dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "MUTATION_" + M_RATE + "_" + (j+1));
                averageWriter.addResults("dataset1/results_MUTATION_" + M_RATE + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset1/averages/results_MUTATION_" + M_RATE);
        }

        G_LENGTH = 0;
        for (int i = 0; i < 10; i++) {
            G_LENGTH += 1;
            M_RATE = 1.0 / (double) G_LENGTH;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " G_LENGTH: " + G_LENGTH);
                dataSet1(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "G_LENGTH_" + G_LENGTH + "_" + (j+1));
                averageWriter.addResults("dataset1/results_G_LENGTH_" + G_LENGTH + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset1/averages/results_G_LENGTH_" + G_LENGTH);
        }
    }

    /**
     * Run dataset 2 with a bunch of parameter changes.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void runDataSet2Fiddles()
            throws FileNotFoundException, IOException {
        int numRuns = 0;

        POP = 200;
        G_LENGTH = 10;
        TOURNAMENT_SIZE = 10;
        M_RATE = (double) 1 / G_LENGTH;
        
        Averager averageWriter = new Averager(1639, 409);

        for (int i = 0; i < 20; i++) {
            dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "BASIC_TOURNAMENT_" + (i + 1));
            averageWriter.addResults("dataset2/results_BASIC_TOURNAMENT_" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset2/averages/BASIC_TOURNAMENT");

        for (int i = 0; i < 20; i++) {
            dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.ROULETTE, "BASIC_ROULETTE_" + (i + 1));
            averageWriter.addResults("dataset2/results_BASIC_ROULETTE_" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset2/averages/BASIC_ROULETTE");

        TOURNAMENT_SIZE = 4;
        for (int i = 0; i < 15; i++) {
            TOURNAMENT_SIZE += 1;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " TOURNAMENT_SIZE: " + TOURNAMENT_SIZE);
                dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j+1));
                averageWriter.addResults("dataset2/results_TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j + 1));
            }
            averageWriter.averageWriteClear("dataset2/averages/TOURNAMENT_" + TOURNAMENT_SIZE);
            
        }

        POP = 0;
        for (int i = 0; i < 10; i++) {
            POP += 100;
            TOURNAMENT_SIZE = (int) (POP * 0.1);
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " POP SIZE: " + POP);
                dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "POP_" + POP + "_" + (j+1));
                averageWriter.addResults("dataset2/results_POP_" + POP + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset2/averages/POP_" + POP);
        }

        POP = 200;
        M_RATE = (double) (1 / G_LENGTH);
        for (int i = 0; i < 10; i++) {
            M_RATE += 0.1;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " MUTATION RATE: " + M_RATE);
                dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "MUTATION_" + M_RATE + "_" + (j+1));
                averageWriter.addResults("dataset2/results_MUTATION_" + M_RATE + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset2/averages/MUTATION_" + M_RATE);
        }

        G_LENGTH = 4;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                G_LENGTH += 1;
                M_RATE = 1.0 / (double) G_LENGTH;
                System.out.println("RUN NUMBER " + ++numRuns + " G_LENGTH: " + G_LENGTH);
                dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "G_LENGTH_" + G_LENGTH + "_" + (j+1));
                averageWriter.addResults("dataset2/results_G_LENGTH_" + G_LENGTH + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset2/averages/G_LENGTH_" + G_LENGTH);
        }
    }

    /**
     *
     * run dataset 3 with a bunch of parameter changes.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void runDataSet3Fiddles()
            throws FileNotFoundException, IOException {
        int numRuns = 0;

        POP = 200;
        G_LENGTH = 10;
        TOURNAMENT_SIZE = 10;
        M_RATE = (double) 1 / G_LENGTH;
        
        Averager averageWriter = new Averager(1600, 400);

        for (int i = 0; i < 20; i++) {
            dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "BASIC_TOURNAMENT_" + (i + 1));
            averageWriter.addResults("dataset3/results_BASIC_TOURNAMENT" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset3/averages/BASIC_TOURNAMENT");
  

        for (int i = 0; i < 20; i++) {
            dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.ROULETTE, "BASIC_ROULETTE_" + (i + 1));
            averageWriter.addResults("dataset3/results_BASIC_ROULETTE_" + (i + 1));
        }
        averageWriter.averageWriteClear("dataset3/averages/BASIC_ROULETTE");

        TOURNAMENT_SIZE = 4;
        for (int i = 0; i < 15; i++) {
            TOURNAMENT_SIZE += 1;
            TOURNAMENT_SIZE = (int) (POP * 0.1);
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " TOURNAMENT_SIZE: " + TOURNAMENT_SIZE);
                dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j+1));
                averageWriter.addResults("dataset3/results_TOURNAMENT_" + TOURNAMENT_SIZE + "_" + (j + 1));
            }
            averageWriter.averageWriteClear("dataset3/averages/TOURNAMENT_" + TOURNAMENT_SIZE);
        }

        TOURNAMENT_SIZE = 10;
        POP = 0;
        for (int i = 0; i < 10; i++) {
            POP += 100;
            TOURNAMENT_SIZE = (int) (POP * 0.1);
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " POP SIZE: " + POP);
                dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "POP_" + POP + "_" + (j+1));
                averageWriter.addResults("dataset3/results/results_POP_" + POP + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset3/averages/POP_" + POP);
        }

        POP = 200;
        M_RATE = (double) (1 / G_LENGTH);
        for (int i = 0; i < 10; i++) {
            M_RATE += 0.1;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " MUTATION RATE: " + M_RATE);
                dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "MUTATION_" + M_RATE + "_" + (j+1));
                averageWriter.addResults("dataset3/results/results_MUTATION_" + M_RATE + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset3/averages/MUTATION_" + M_RATE);
        }

        G_LENGTH = 4;
        for (int i = 0; i < 10; i++) {
            G_LENGTH += 1;
            M_RATE = 1.0 / (double) G_LENGTH;
            for (int j = 0; j < 10; j++) {
                System.out.println("RUN NUMBER " + ++numRuns + " G_LENGTH: " + G_LENGTH);
                dataSet3(FitnessType.RULE_SET_REAL, GenomeType.RULE_SET, SelectionType.TOURNAMENT, "G_LENGTH_" + G_LENGTH + "_" + (j+1));
                averageWriter.addResults("dataset3/results/results_G_LENGTH_" + G_LENGTH + "_" + (j+1));
            }
            averageWriter.averageWriteClear("dataset3/averages/G_LENGTH_" + G_LENGTH);
        }
    }

    /**
     *
     * Run the prototype set, which is a bit genome attempting to solve the 'all
     * ones' problem.
     *
     * @param fit specific fitness type
     * @param genome type of genome to use
     * @param sel selection algorithm to use
     * @param extension extension to put on the generated results file.
     * @throws IOException
     */
    public static void prototypeSet(FitnessType fit, GenomeType genome, SelectionType sel, String extension)
            throws IOException {
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, "", extension, true);

        // generate the initial population 
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual = new BinaryCandidateSolution(GenomeHelper.generateBitGenome(G_LENGTH));
            FitnessFunctions.calculateFitnessTotalValue(individual);
            initialGeneration.add(individual);
        }

        // run the generic algorithm
        geneticAlgorithm(initialGeneration, null, fit, sel, resultWriter);
    }

    // BEST SETTINGS SO FAR: POP 100, G_LENGTH 10, STOP_GENERATIONS 100
    /**
     *
     * Run dataset1, which can be represented as a string that maps onto the
     * output of a lookup table, or as a rule set.
     *
     * @param fit specific fitness type
     * @param genome type of genome to use
     * @param sel selection algorithm to use
     * @param extension extension to put on the generated results file.
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean dataSet1(FitnessType fit, GenomeType genome, SelectionType sel, String extension)
            throws FileNotFoundException, IOException {
        // read the data from the file, and put it in a lookup table.
        TreeMap<String, String> fullLookup = readData("data1.txt");
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, "dataset1", extension, true);

        // depending on the genome type being used, split the data into training data and test data.
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

        // generate the initial population, depending on the genome type being used.
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

        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel, resultWriter); // train;
        return testRealData(bestSolution, realData, trainingData, fit, resultWriter); // test the best solution on the real data.
    }

    /**
     * Run dataset2, which can be represented as a rule set.
     *
     * @param fit specific fitness type
     * @param genome type of genome to use
     * @param sel selection algorithm to use
     * @param extension extension to put on the generated results file.
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean dataSet2(FitnessType fit, GenomeType genome, SelectionType sel, String extension)
            throws FileNotFoundException, IOException {
        // read data
        TreeMap<String, String> fullLookup = readData("data2.txt");
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, "dataset2", extension, true);

        // split data into test and training.
        switch (genome) {
            case RULE_SET:
                TreeMap[] maps = getTrainingData(fullLookup, TRAINING_POP);
                trainingData = maps[0];
                realData = maps[1];
                break;
            default:
                throw new RuntimeException("dataSet2 requires GenomeType to be RULE_SET. Actual type: " + genome.name());
        }

        // generate initial population
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case RULE_SET:
                    individual = new BinaryRuleSetCandidateSolution(GenomeHelper.generateBinaryRuleGenome(G_LENGTH, 12)); // generate G_LENGTH rules.
                    FitnessFunctions.calculateFitnessBinaryRuleSet(individual, trainingData);
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be RULE_SET. Actual type: " + genome.name());
            }

            initialGeneration.add(individual);
        }

        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel, resultWriter);
        return testRealData(bestSolution, realData, trainingData, fit, resultWriter); // test the best solution on the real data.
    }

    /**
     *
     * run dataset 3, which can be represented as either a rule set or a set of
     * weights for a multi-layer perceptron.
     *
     * @param fit specific fitness type
     * @param genome type of genome to use
     * @param sel selection algorithm to use
     * @param extension extension to put on the generated results file.
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean dataSet3(FitnessType fit, GenomeType genome, SelectionType sel, String extension)
            throws FileNotFoundException, IOException {
        TreeMap<String, String> fullLookup = readData("data3.txt");
        TreeMap<String, String> trainingData = null;
        TreeMap<String, String> realData = null;
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, "dataset3", extension, true);

        // get the training data.
        switch (genome) {
            case MLP:
            case RULE_SET:
                TreeMap[] maps = getTrainingData(fullLookup, TRAINING_POP);
                trainingData = maps[0];
                realData = maps[1];
                break;
            default:
                throw new RuntimeException("dataSet3 requires GenomeType to be RULE_SET or MLP. Actual type: " + genome.name());
        }

        // generate the initial generation.
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

        // test the GA.
        CandidateSolution bestSolution = geneticAlgorithm(initialGeneration, trainingData, fit, sel, resultWriter);
        return testRealData(bestSolution, realData, trainingData, fit, resultWriter); // test the best solution on the real data.
    }

    /**
     *
     * The genetic algorithm entry point.
     *
     * @param oldGeneration initial generation
     * @param lookup lookup table containing the inputs and expected values
     * @param fit specific fitness function being used
     * @param sel selection algorithm to use
     * @param resultWriter the result writer instance to use
     * @return
     */
    public static CandidateSolution geneticAlgorithm(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit, SelectionType sel, SimpleResultWriter resultWriter) {
        try {

            ArrayList<CandidateSolution> newGeneration;
            int bestPossible = lookup.size();
            int bestFitness = 0;
            int fitnessGens = 0;
            int count = 0;
            boolean reachedMaxFitness = false;

            int overallBestFitness = 0;
            for (int i = 0; i < NUM_GENERATIONS; i++) {

                // generate a new generation
                newGeneration = newGeneration(oldGeneration, PERCENT_TO_KEEP, lookup, fit, sel);
                // write the results and find the bestFitness.
                bestFitness = resultWriter.write((i + 1), newGeneration);
                oldGeneration = newGeneration;

                // if the fitness is the max fitness, and STOP_GENERATIONS have
                // passed, stop the run.
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

                // if the fitness has been constant for some time, stop the run.
                if (bestFitness == overallBestFitness) {
                    fitnessGens++;
                } else {
                    fitnessGens = 0;
                    overallBestFitness = bestFitness;
                }
                if (fitnessGens > 1000) {
                    break;
                }
            }

            // get the best solution, if one exists, and return it.
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

    /**
     *
     * Generate a new generation from an old generation. parents are selected
     * from the old generation, and children are created by using crossover and
     * mutation on the parents.
     *
     * @param oldGeneration old generation from which the new one is created
     * @param percentToKeep keep a percentage of the best parents from the old
     * gen.
     * @param lookup table with inputs and expected outputs.
     * @param fit specific fitness function to use.
     * @param sel selection algorithm to use.
     * @return
     */
    public static ArrayList<CandidateSolution> newGeneration(ArrayList<CandidateSolution> oldGeneration, double percentToKeep, TreeMap<String, String> lookup, FitnessType fit, SelectionType sel) {
        ArrayList<CandidateSolution> parents;
        ArrayList<CandidateSolution> newGeneration = new ArrayList<>();

        // keep the best solutions, as determined by PERCENT_TO_KEEP
        int numberToKeep = (int) (PERCENT_TO_KEEP * oldGeneration.size());
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

        // select the parents to use.
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

        double mutationRate;
        if (fit == FitnessType.MLP) {
            mutationRate = ANN_M_RATE;
        } else {
            mutationRate = M_RATE;
        }
        int popLimit = parents.size() - numberToKeep;

        // generate a new population using crossover and mutation.
        for (int i = 0; i < popLimit; i = i + 2) {

            CandidateSolution[] currentParents = new CandidateSolution[2];
            currentParents[0] = parents.get(i);
            currentParents[1] = parents.get(i + 1);
            int point = rand.nextInt(currentParents[0].getSize());
            // probability that crossover occurs.
            double crossoverRate = rand.nextFloat();
            
            // crossover with both parents
            for (int j = 0; j <= 1; j++) {
                int k = (j == 0) ? 1 : 0; // j == 0, k == 1 and j == 1, k == 0.
                CandidateSolution child;
                // make sure that crossover is being used this run.
                if (crossoverRate < C_RATE) {
                    child = currentParents[j].crossover(point, currentParents[k]);
                } else {
                    child = currentParents[j];
                }

                // attempt to mutate the child
                child.mutation(mutationRate);
                
                // find the fitness value of the child.
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
                // finally, add the child.
                newGeneration.add(child);
            }

        }

        // shuffle the generation, so there's no order, and the list is random.
        // This stops the best solutions from being at the front.
        Collections.shuffle(newGeneration);
        return newGeneration;
    }

    /**
     *
     * read a dataset, and put it's values into a map. The inputs are the key;
     * and the values are the output.
     * 
     * @param name
     * @return
     * @throws FileNotFoundException
     */
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
                    // place the key and value in the map.
                    key = key.trim();
                    lookup.put(key, words[i]);
                } else {
                    // place the next input value in the key.
                    key += words[i] + " ";
                }
            }
        }
        return lookup;
    }

    /**
     *
     * Split the full dataset into training and real data.
     * 
     * @param fullLookup
     * @param percentage
     * @return
     */
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

    /**
     *
     * given a solution, test it against the real data to see how well it's
     * learnt the dataSet.
     * 
     * @param bestSolution the best solution found in the GA
     * @param realData data that the solution hasn't seen.
     * @param trainingData the data that the solution was trained on.
     * @param fit the specific fitness function to use
     * @param resultWriter the result writer
     * @return
     * @throws IOException
     */
    public static boolean testRealData(CandidateSolution bestSolution, TreeMap<String, String> realData, TreeMap<String, String> trainingData, FitnessType fit, SimpleResultWriter resultWriter)
            throws IOException {
        boolean pass = true;
        int realPassed = 0;
        int trainingPassed = 0;
        RuleSet ruleSet;
        String ruleRepresentation = "";

        // calculate how much of the data the solution gets correct
        switch (fit) {
            case LOOKUP_TABLE:
                realPassed = FitnessFunctions.calculateFitnessLookupTable(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessLookupTable(bestSolution, trainingData);
                break;
            case RULE_SET_INT:
                realPassed = FitnessFunctions.calculateFitnessBinaryRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessBinaryRuleSet(bestSolution, trainingData);
                ruleSet = new BinaryRuleSet(bestSolution.toString(), getRuleSize(bestSolution.getGenome()));
                ruleRepresentation = ruleSet.ruleSetRepresentation();
                break;
            case RULE_SET_REAL:
                realPassed = FitnessFunctions.calculateFitnessRealRuleSet(bestSolution, realData);
                trainingPassed = FitnessFunctions.calculateFitnessRealRuleSet(bestSolution, trainingData);
                ruleSet = new RealRuleSet(bestSolution.getGenome(), 13);
                ruleRepresentation = ruleSet.ruleSetRepresentation();
                break;
            case MLP:
                realPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, realData, NUMBER_OF_HIDDEN_NODES);
                trainingPassed = FitnessFunctions.calculateFitnessMLP(bestSolution, trainingData, NUMBER_OF_HIDDEN_NODES);
                break;
            default:
                System.out.println("Cannot test fitness type: " + fit.name());
                break;
        }

        // find out how much of the data wasn't correctly classified.
        int realFailed = realData.size() - realPassed;
        int trainingFailed = trainingData.size() - trainingPassed;
        int totalFailed = realFailed + trainingFailed;

        // write the final results in the results file.
        resultWriter.writeFinalResults(realPassed, trainingPassed, realData.size(), trainingData.size(), ruleRepresentation);

        if (totalFailed != 0) {
            pass = false;
        }

        resultWriter.close();
        return pass;
    }

    // hacky function to get the size of the rules being tested. Assume the
    // rule length is 12, unless the total size of the genome is less than
    // the LCM of 7 and 12. This is because rule sets from data one are likely
    // to have less than 10 rules.
    static int getRuleSize(List genome) {
        int ruleSize;
        if (genome.size() % 7 == 0 && genome.size() % 12 == 0) {
            ruleSize = 12;
        } else if (genome.size() % 7 == 0) {
            ruleSize = 7;
        } else {
            ruleSize = 12;
        }
        return ruleSize;
    }
}
