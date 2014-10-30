/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import individuals.CandidateSolution;
import individuals.BinaryCandidateSolution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author ad3-brown
 */
public class LookupCandidateSolution extends CandidateSolution<Integer> {
    
    public LookupCandidateSolution(ArrayList<Integer> genome) {
        super(genome);
    }
    
    @Override
    public int calculateFitness(Object o) {
        HashMap<String, String> lookupTable = (HashMap<String, String>) o;
        int currentFitness = 0;
        int i = 0;
        
        for (String key : lookupTable.keySet()) {
            String correct = lookupTable.get(key);
            String potential = getGenome().get(i).toString();
            
            if (potential.equals(correct)) {
                currentFitness++;
            }
            
            i++;
        }
        
        setFitness(currentFitness);
        return currentFitness;
    }
    
    @Override
    public void mutation(double probability) {
        Random randGen = new Random();
        double rand;
        int totalMutations = 0;
        
        for (int i = 0; i < this.getSize(); i++) {
            rand = randGen.nextDouble();
            if (rand <= probability) {
                //System.out.println("MUTATING POINT " + i);
                totalMutations++;
                int value = genome.get(i);
                if (value == 0) {
                    genome.set(i, 1);
                } else {
                    genome.set(i, 0);
                }    
            }
        }
    }
    
    @Override
    public CandidateSolution crossover(int point, CandidateSolution partner) {
                        
        // should throw exception here?
        if (this.size != partner.size) {
            return partner;
        }

        ArrayList<Integer> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, genome.size()));
        
        CandidateSolution child = new BinaryCandidateSolution(childGenome);

        
        return child;
    }
    
}