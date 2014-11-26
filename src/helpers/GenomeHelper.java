/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ad3-brown
 */
public class GenomeHelper {
    
    public static ArrayList<Integer> generateBitGenome(int size) {
        Random randGen = new Random();
        ArrayList<Integer> genome = new ArrayList<>();
        
        for(int i = 0; i < size; i++) {
            genome.add(randGen.nextInt(2));
        }
        return genome;
    }
    
    public static ArrayList<Integer> generateBinaryRuleGenome(int popSize, int ruleSize) {
        Random randGen = new Random();
        Double rand;
        ArrayList<Integer> genome = new ArrayList<>();
        
        for(int i = 0; i < popSize * ruleSize; i++) {
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
    
    public static ArrayList<Double> generateRealRuleGenome(int popSize, int ruleSize) {
        Random randGen = new Random();
        Double rand;
        ArrayList<Double> genome = new ArrayList<>();
        
        for(int i = 0; i < popSize * ruleSize; i++) {
            rand = randGen.nextDouble();
            
            // answer must be 1 or 0.
            if ((i + 1) % ruleSize == 0) {
                genome.add((double)randGen.nextInt(2));
            } else {
                // conditions can be between 0-1 or be 100 for wildcard.
                if (rand < 0.95) {
                    genome.add(i, randGen.nextDouble());
                } else {
                    genome.add(i, (double)100);
                }
            }
        }
        return genome;
    }
    
    public static ArrayList<Integer> generateIntGenome(int size, int min, int max) {
        Random randGen = new Random();
        int rand;
        ArrayList<Integer> genome = new ArrayList<>();
        
        for(int i = 0; i < size; i++) {
            // plus one as nextInt(max) is exclusive.
            rand = min + randGen.nextInt((max - min) + 1);
            genome.add(rand);
        }
        return genome;
    }
    
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
    
    public static ArrayList<Float> generateFloatGenome(int size, float min, float max) {
        Random randGen = new Random();
        float rand;
        ArrayList<Float> genome = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            rand = (min + ((max - min) * randGen.nextFloat()));
            genome.add(rand);
        }
        
        return genome;
    }
}
