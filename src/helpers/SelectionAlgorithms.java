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
 *
 * @author ad3-brown
 */
public class SelectionAlgorithms {
        public static ArrayList<CandidateSolution> rouletteSelection(ArrayList<CandidateSolution> oldGeneration) {
        int totalFitness = 0;

        Random rand = new Random();
        
        Collections.sort(oldGeneration);
        
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
    
    public static ArrayList<CandidateSolution> tournamentSelection(ArrayList<CandidateSolution> oldGeneration, int pop) {
        ArrayList<CandidateSolution> parents = new ArrayList<>(); 
        
        Random rand = new Random();
        for (int i = 0; i < (oldGeneration.size()); i++) {
            CandidateSolution parent1 = oldGeneration.get(rand.nextInt(pop));
            CandidateSolution parent2 = oldGeneration.get(rand.nextInt(pop));
            
            if (parent1.getFitness() >= parent2.getFitness()) {
                parents.add(parent1);
            } else {
                parents.add(parent2);
            }
        }
        return parents;
    }
}
