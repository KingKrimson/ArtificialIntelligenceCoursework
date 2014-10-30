/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import individuals.CandidateSolution;
import individuals.CandidateSolution;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ad3-brown
 */
public class FitnessFunctions {
    
    public static int calculateFitnessTotalValue(CandidateSolution individual) {
        int fitness = 0;
        final ArrayList<Integer> genome = individual.getGenome();
        
        for (int gene : genome) {
            fitness += gene;
        }
        
        individual.setFitness(fitness);
        return fitness;
    }
    
    public static int calculateFitnessLookupTable(CandidateSolution individual, HashMap lookup) {
        int fitness = 0;
        
        return fitness;
    }
    
    public static int calculateFitnessRuleSet(CandidateSolution individual, HashMap lookup) {
        int fitness = 0;
        
        return fitness;
    }
    
    public static int calculateFitnessMLP() {
        int fitness = 0;
        return fitness;
    }
}
