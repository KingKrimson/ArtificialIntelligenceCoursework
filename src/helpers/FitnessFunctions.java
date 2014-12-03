/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import individuals.CandidateSolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import neuralnetwork.ArtificialNeuralNetwork;
import rules.BinaryRuleSet;
import rules.RealRuleSet;
import rules.RuleSet;

/**
 *
 * All of the fitness functions are contained in here.
 * 
 * @author ad3-brown
 */
public class FitnessFunctions {

    /**
     *
     * simple total value fitness function. A bit genome is considered to be 'fit'
     * if all of its genes are equal to 1.
     * @param individual
     * @return
     */
    public static int calculateFitnessTotalValue(CandidateSolution individual) {
        int fitness = 0;
        final List<Integer> genome = individual.getGenome();

        for (int gene : genome) {
            fitness += gene;
        }

        individual.setFitness(fitness);
        return fitness;
    }

    /**
     * Calculate the fitness for the lookup table solution for dataset1. For a 
     * genome length of 64, increase the fitness if the gene matches corresponding 
     * answer in the lookup table provided. 
     * @param individual
     * @param lookup contains the data
     * @return
     */
    public static int calculateFitnessLookupTable(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        final List<Integer> genome = individual.getGenome();
        Object[] keyArray = lookup.keySet().toArray();

        for (int i = 0; i < genome.size(); i++) {
            String predictedAnswer = genome.get(i).toString();
            String actualAnswer = lookup.get((String) keyArray[i]);

            if (actualAnswer.equals(predictedAnswer)) {
                fitness++;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }

    /**
     *
     * calculate the fitness for the binary rule sets. The function tests the current
     * row of the data against each rule (of 6 or 11 conditions + 1 action).
     * if the inputs of the row match the conditions of the rule, then the predicted
     * answer is taken to be the action of that rule. The fitness increases if the
     * predicted answer is correct.
     * 
     * @param individual
     * @param lookup
     * @return
     */
    public static int calculateFitnessBinaryRuleSet(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        RuleSet ruleSet;
        if (lookup.size() <= 64) {
            ruleSet = new BinaryRuleSet(individual.toString(), 7);
        } else {
            ruleSet = new BinaryRuleSet(individual.toString(), 12);
        }
        Object[] keyArray = lookup.keySet().toArray();

        for (Object k : keyArray) {
            String key = (String) k;
            String predictedAnswer = ruleSet.testRuleSet(key);
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && predictedAnswer.equals(actualAnswer)) {
                fitness++;
            }
            if (predictedAnswer == null || predictedAnswer.equals("2")) {
                //fitness--;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }
    
    /**
     *
     * calculate the fitness for the binary rule sets. The function tests the current
     * row of the data against each rule (of 6 or 11 conditions + 1 action).
     * if the inputs of the row match the conditions of the rule, then the predicted
     * answer is taken to be the action of that rule. The fitness increases if the
     * predicted answer is correct.
     * 
     * @param individual
     * @param lookup
     * @return
     */
    public static int calculateFitnessBinaryRuleSetPressure(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        int ruleSize = 0;
        RuleSet ruleSet;
        if (lookup.size() <= 64) {
            ruleSize = 7;
            ruleSet = new BinaryRuleSet(individual.toString(), 7);
        } else {
            ruleSize = 12;
            ruleSet = new BinaryRuleSet(individual.toString(), 12);
        }
        Object[] keyArray = lookup.keySet().toArray();

        for (Object k : keyArray) {
            String key = (String) k;
            String predictedAnswer = ruleSet.testRuleSet(key);
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && predictedAnswer.equals(actualAnswer)) {
                fitness++;
            }
            if (predictedAnswer == null || predictedAnswer.equals("2")) {
                //fitness--;
            }  
        }

        fitness = fitness - (individual.getGenome().size()/ruleSize);
        individual.setFitness(fitness);
        return fitness;
    }

    /**
     *
     * similar to the function for the binary rule set. Each rule is tested against
     * the current row. This time, one condition consists of two real values, which creates
     * a range that an input must fall into to be valid. If the inputs of the row 
     * fits a rule, then the predicted answer is the action of that rule. 
     * fitness increases if that prediction is correct.
     * 
     * @param individual
     * @param lookup
     * @return
     */
    public static int calculateFitnessRealRuleSet(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        RuleSet ruleSet = new RealRuleSet(individual.getGenome(), 13);
        Object[] keyArray = lookup.keySet().toArray();

        for (Object k : keyArray) {
            String key = (String) k;
            List<String> stringInputList = new ArrayList<>(Arrays.asList(key.split(" ")));
            List<Double> doubleInputList = new ArrayList<>();
            
            for(String s : stringInputList) {
                doubleInputList.add(Double.parseDouble(s));
            }
            
            String predictedAnswer = ruleSet.testRuleSet(doubleInputList);
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && Double.parseDouble(predictedAnswer) == Integer.parseInt(actualAnswer)) {
                fitness++;
            }
        }

        
        individual.setFitness(fitness);
        return fitness;
    }
    
    /**
     *
     * similar to the function for the binary rule set. Each rule is tested against
     * the current row. This time, one condition consists of two real values, which creates
     * a range that an input must fall into to be valid. If the inputs of the row 
     * fits a rule, then the predicted answer is the action of that rule. 
     * fitness increases if that prediction is correct.
     * 
     * @param individual
     * @param lookup
     * @return
     */
    public static int calculateFitnessRealRuleSetPressure(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        RuleSet ruleSet = new RealRuleSet(individual.getGenome(), 13);
        Object[] keyArray = lookup.keySet().toArray();

        for (Object k : keyArray) {
            String key = (String) k;
            List<String> stringInputList = new ArrayList<>(Arrays.asList(key.split(" ")));
            List<Double> doubleInputList = new ArrayList<>();
            
            for(String s : stringInputList) {
                doubleInputList.add(Double.parseDouble(s));
            }
            
            String predictedAnswer = ruleSet.testRuleSet(doubleInputList);
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && Double.parseDouble(predictedAnswer) == Integer.parseInt(actualAnswer)) {
                fitness++;
            }
        }

        fitness = fitness - (individual.getGenome().size()/13);
        individual.setFitness(fitness);
        return fitness;
    }

    /**
     * constructs a neural network using the set of real valued weights provided.
     * the input values of each row are placed into the neural network. The value
     * that the neural net outputs is the predicted answer. Fitness is increased if
     * the predicted answer is correct.
     * 
     * @param individual
     * @param lookup
     * @param hiddenNumber
     * @return
     */
    public static int calculateFitnessMLP(CandidateSolution individual, TreeMap<String, String> lookup, int hiddenNumber) {
        int fitness = 0;
        ArtificialNeuralNetwork network;
        List<Double> weights = individual.getGenome();

        // input nodes will always be six. Number of nodes in hidden layer can be tweaked
        network = new ArtificialNeuralNetwork(weights, 6, hiddenNumber);

        Object[] keyArray = lookup.keySet().toArray();
        for (Object k : keyArray) {
            ArrayList<Double> inputs = new ArrayList();
            String key = (String) k;
            for (String input : key.split(" +")) {
                inputs.add(Double.parseDouble(input));
            }
            String predictedAnswer = String.valueOf(network.calcValue(inputs));
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && predictedAnswer.equals(actualAnswer)) {
                fitness++;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }
}
