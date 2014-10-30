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
        Double rand;
        ArrayList<Integer> genome = new ArrayList<>();
        
        for(int i = 0; i < size; i++) {
            rand = randGen.nextDouble();
            
            // Could probably do this better.
            // rand = randGen.nextInt(2)?
            if (rand < 0.5) {
                genome.add(1);
            } else {
                genome.add(0);
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
