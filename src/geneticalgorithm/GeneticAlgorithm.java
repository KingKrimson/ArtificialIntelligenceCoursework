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
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ad3-brown
 */
public class GeneticAlgorithm {

    final static int POP = 64;
    final static int G_LENGTH = 50;
    final static int NUM_GENERATIONS = 50;
    final static double M_RATE = 0.01;
    final static boolean FULL_PRINT = false;

    public enum FitnessType {

        TOTAL_VALUE, LOOKUP_TABLE, RULE_SET, MLP
    }

    public enum GenomeType {

        BIT, RULE_SET, MLP
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList<CandidateSolution> initialGeneration = new ArrayList<>(POP);

        for (int i = 0; i < POP; i++) {
            CandidateSolution individual = new BinaryCandidateSolution(GenomeHelper.generateBitGenome(G_LENGTH));
            FitnessFunctions.calculateFitnessTotalValue(individual);
            initialGeneration.add(individual);
        }

        geneticAlgorithm(initialGeneration, FitnessType.TOTAL_VALUE);

    }

    public static void geneticAlgorithm(ArrayList<CandidateSolution> oldGeneration, FitnessType fit) {
        try {
            SimpleResultWriter resultWriter = new SimpleResultWriter(POP, G_LENGTH, true);
            resultWriter.write(0, oldGeneration);

            ArrayList<CandidateSolution> newGeneration;
            for (int i = 0; i < NUM_GENERATIONS; i++) {
                newGeneration = newGeneration(oldGeneration, fit);
                resultWriter.write((i + 1), newGeneration);
                oldGeneration = newGeneration;
            }
            resultWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<CandidateSolution> newGeneration(ArrayList<CandidateSolution> oldGeneration, FitnessType fit) {
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
                        FitnessFunctions.calculateFitnessLookupTable(child, null);
                        break;
                    case RULE_SET:
                        FitnessFunctions.calculateFitnessRuleSet(child, null);
                        break;
                    case MLP:
                        FitnessFunctions.calculateFitnessMLP();
                        break;
                }
                newGeneration.add(child);
            }

        }

        return newGeneration;
    }
}
