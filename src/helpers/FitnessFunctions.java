/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import individuals.CandidateSolution;
import java.util.ArrayList;
import java.util.TreeMap;
import neuralnetwork.ArtificialNeuralNetwork;
import rules.BinaryRuleSet;
import rules.RealRuleSet;
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
            String actualAnswer = lookup.get((String) keyArray[i]);

            if (actualAnswer.equals(predictedAnswer)) {
                fitness++;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }

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
            if (predictedAnswer == null || predictedAnswer.equals(2)) {
                fitness--;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }

    public static int calculateFitnessRealRuleSet(CandidateSolution individual, TreeMap<String, String> lookup) {
        int fitness = 0;
        RuleSet ruleSet = new RealRuleSet(individual.toString(), 13);
        Object[] keyArray = lookup.keySet().toArray();

        for (Object k : keyArray) {
            String key = (String) k;
            String predictedAnswer = ruleSet.testRuleSet(key);
            String actualAnswer = lookup.get(key);

            if (predictedAnswer != null && predictedAnswer.equals(actualAnswer)) {
                fitness++;
            }
        }

        individual.setFitness(fitness);
        return fitness;
    }

    public static int calculateFitnessMLP(CandidateSolution individual, TreeMap<String, String> lookup, int hiddenNumber) {
        int fitness = 0;
        ArtificialNeuralNetwork network;
        ArrayList<Double> weights = individual.getGenome();

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
