/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import individuals.CandidateSolution;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import rules.RuleSet;

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
    
    public static int calculateFitnessLookupTable(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        final ArrayList<Integer> genome = individual.getGenome();
        Object[] keyArray = lookup.keySet().toArray();
        
        for (int i = 0; i < genome.size(); i++) {
            String predictedAnswer = genome.get(i).toString();
            String actualAnswer = lookup.get((String)keyArray[i]);
                    
            if (actualAnswer.equals(predictedAnswer)) {
                fitness++;
            }
        }
        
        individual.setFitness(fitness);
        return fitness;
    }
    
    public static int calculateFitnessRuleSet(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        RuleSet ruleSet; 
        if (lookup.size() <= 64) {
            ruleSet = new RuleSet(individual.toString(), 7);
        } else {
            ruleSet = new RuleSet(individual.toString(), 12);
        }
        Object[] keyArray = lookup.keySet().toArray();
         
         for (Object k : keyArray) {
             String key = (String)k;
             String predictedAnswer = ruleSet.testRuleSet(key);
             String actualAnswer = lookup.get(key);
             
             if (predictedAnswer != null && predictedAnswer.equals(actualAnswer)) {
                 fitness++;
             }
         }
        
        individual.setFitness(fitness);
        return fitness;
    }
    
    public static int calculateFitnessMLP(CandidateSolution individual, Map lookup) {
        int fitness = 0;
        
        individual.setFitness(fitness);
        return fitness;
    }
}
