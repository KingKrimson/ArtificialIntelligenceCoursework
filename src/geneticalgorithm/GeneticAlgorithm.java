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

    final static int POP = 64;
    final static int G_LENGTH = 10;
    final static int NUM_GENERATIONS = 50;
    final static double M_RATE = 0.01;
    final static boolean FULL_PRINT = false;

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
            prototypeSet(FitnessType.TOTAL_VALUE, GenomeType.BIT);
            //dataSet1(FitnessType.LOOKUP_TABLE, GenomeType.BIT);
            //dataSet2(FitnessType.RULE_SET_INT, GenomeType.RULE_SET_INT);
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
        TreeMap<String, String> lookup = readData("data1.txt");
        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);
        
        for (int i = 0; i < POP; i++) {
            CandidateSolution individual;
            switch (genome) {
                case BIT:
                    individual = new LookupCandidateSolution(GenomeHelper.generateBitGenome(64));
                    FitnessFunctions.calculateFitnessLookupTable(individual, lookup);
                    break;
                case RULE_SET:
                    individual = new RuleSetCandidateSolution(GenomeHelper.generateRuleGenome(G_LENGTH * 7)); // generate G_LENGTH rules.
                    break;
                default:
                    throw new RuntimeException("dataSet1 requires GenomeType to be BIT or RULE_SET. Actual type: " + genome.name());
            }
            
            initialGeneration.add(individual);
        }
        
        geneticAlgorithm(initialGeneration, lookup, fit);
    }
    
    public static void dataSet2(FitnessType fit, GenomeType genome) 
    throws FileNotFoundException {
        TreeMap<String, String> lookup = readData("data2.txt");
        //geneticAlgorithm(initialGeneration, fit);
    }
            
    public static void dataSet3(FitnessType fit, GenomeType genome) 
    throws FileNotFoundException {
        TreeMap<String, String> lookup = readData("data3.txt");
        //geneticAlgorithm(initialGeneration, fit);
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

    public static void geneticAlgorithm(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<CandidateSolution> newGeneration(ArrayList<CandidateSolution> oldGeneration, TreeMap<String, String> lookup, FitnessType fit) {
        ArrayList<CandidateSolution> parents = SelectionAlgorithms.tournamentSelection(oldGeneration, POP);
        ArrayList<CandidateSolution> newGeneration = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < parents.size(); i = i + 2) {
            CandidateSolution[] currentParents = new CandidateSolution[2];
            currentParents[0] = parents.get(i);
            currentParents[1] = parents.get(i + 1);
            int point = rand.nextInt(currentParents[0].getSize());

            for (int j = 0; j <= 1; j++) {
                int k = (j == 0) ? 1 : 0; // j == 0, k == 1 and j == 1, k == 0.
                CandidateSolution child = currentParents[j].crossover(point, currentParents[k]);
                child.mutation(M_RATE);
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
}
