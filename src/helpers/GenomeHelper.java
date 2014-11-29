/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that generates genomes for the initial population. 
 * 
 * @author ad3-brown
 */
public class GenomeHelper {

    /**
     *
     * generates a genome where the genes either consists of one or two.
     * 
     * @param size size of the genome
     * @return
     */
    public static ArrayList<Integer> generateBitGenome(int size) {
        Random randGen = new Random();
        ArrayList<Integer> genome = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            genome.add(randGen.nextInt(2));
        }
        return genome;
    }

    /**
     *
     * generate a 'binary' ruleset. a gene consist of 0, 1, or 2, which is a 
     * wildcard that matches either bit. Wildcards are generated only 10% of the time.
     * the first (n-1) bits of a rule are conditions, which can be wildcards. The
     * nth bit is an action, which must be 1 or 0.
     * 
     * @param numRules number of rules to generate
     * @param ruleSize the size of a rule.
     * @return
     */
    public static ArrayList<Integer> generateBinaryRuleGenome(int numRules, int ruleSize) {
        Random randGen = new Random();
        Double rand;
        ArrayList<Integer> genome = new ArrayList<>();

        // generate all of the rules requested.
        for (int i = 0; i < numRules * ruleSize; i++) {
            rand = randGen.nextDouble();

            // don't let the answer be a wildcard.
            if ((i + 1) % ruleSize == 0) {
                genome.add(randGen.nextInt(2));
            } else {
                if (rand < 0.45) {
                    genome.add(0);
                } else if (rand < 0.90) {
                    genome.add(1);
                } else {
                    genome.add(2);
                }
            }
        }

        return genome;
    }

    /**
     * generate a real rule genome. Each condition consists of two values,
     * which are real numbers between 0 and 1. they form a range which the inputs
     * must fall into. The action must be 1.0 or 0.0, to match the output of dataset 3.
     * @param numRules
     * @param ruleSize
     * @return
     */
    public static ArrayList<Double> generateRealRuleGenome(int numRules, int ruleSize) {
        Random randGen = new Random();
        Double rand;
        ArrayList<Double> genome = new ArrayList<>();

        for (int i = 0; i < numRules * ruleSize; i++) {
            rand = randGen.nextDouble();

            // answer must be 1 or 0.
            if ((i + 1) % ruleSize == 0) {
                genome.add((double) randGen.nextInt(2));
            } else {
                // conditions can be between 0-1 
                genome.add(i, randGen.nextDouble());
            }
        }
        return genome;
    }

    /**
     *
     * generate a list of doubles. Used for the ANN weights.
     * 
     * @param size number of doubles to generate.
     * @param min minimum value of double
     * @param max max value of double
     * @return
     */
    public static ArrayList<Double> generateDoubleGenome(int size, double min, double max) {
        Random randGen = new Random();
        double rand;
        ArrayList<Double> genome = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            rand = (min + ((max - min) * randGen.nextDouble()));
            genome.add(rand);
        }

        return genome;
    }
}
