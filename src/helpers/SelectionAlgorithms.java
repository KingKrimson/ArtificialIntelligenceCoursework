/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import individuals.CandidateSolution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * a collection of selection algorithms.
 * @author ad3-brown
 */
public class SelectionAlgorithms {

    /**
     * chooses a random number, and iterates through a list of individuals, summing up their
     * fitness. Once the summed fitness is greater than the random number, the 
     * current individual becomes a parent.
     * The algorithm does this until the population of parents matches the population of the
     * generation it was given as a parameter.
     * 
     * @param oldGeneration
     * @return
     */
    public static ArrayList<CandidateSolution> rouletteSelection(ArrayList<CandidateSolution> oldGeneration) {
        int totalFitness = 0;

        Random rand = new Random();
        
        // Removed a sort from here, as I believe it was biasing the selection.
        
        ArrayList<CandidateSolution> parents = new ArrayList<>();
        for (CandidateSolution individual : oldGeneration) {
            totalFitness += individual.getFitness();
        }
        
        for (int i = 0; i < oldGeneration.size(); i++) {
            int incrementalFitness = 0;
            int selection = rand.nextInt((totalFitness) + 1);
            
            for (CandidateSolution candidateSolution : oldGeneration) {
                incrementalFitness += candidateSolution.getFitness();
                if (incrementalFitness >= selection) {
                    parents.add(candidateSolution);
                    break;
                }
            }
        }
        
        return parents;
    }
    
    /**
     *
     * chooses tournamentSize random competitors from the given generation. Adds
     * the competitor with the best fitness to the pool of parents to be returned.
     * it does this until the population size of the parents reaches the population
     * size of the generation parameter. 
     * 
     * @param oldGeneration
     * @param pop
     * @param tournamentSize size of a tournament.
     * @return
     */
    public static ArrayList<CandidateSolution> tournamentSelection(ArrayList<CandidateSolution> oldGeneration, int pop, int tournamentSize) {
        ArrayList<CandidateSolution> parents = new ArrayList<>(); 
        
        Random rand = new Random();
        for (int i = 0; i < (oldGeneration.size()); i++) {
            ArrayList<CandidateSolution> competitors = new ArrayList(tournamentSize);
            for (int j = 0; j < tournamentSize; j++) {
                competitors.add(oldGeneration.get(rand.nextInt(pop)));
            }
            
            Collections.sort(competitors);
            CandidateSolution winner = competitors.get(competitors.size() - 1);
            parents.add(winner);
        }
        return parents;
    }
}
