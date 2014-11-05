/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ad3-brown
 */
public class BinaryCandidateSolution extends CandidateSolution<Integer> {
    
    public BinaryCandidateSolution(ArrayList<Integer> genome) {
        super(genome);
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
        //System.out.println(""); 
        //System.out.println("\nPARENT 1: " + this.getGenome().toString());
        //System.out.println("PARENT 2:" + partner.getGenome().toString());
        //System.out.println("CROSSOVER POINT: " + point);
        ArrayList<Integer> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, genome.size()));
        
        CandidateSolution child = new BinaryCandidateSolution(childGenome);
        //System.out.println("CHILD: " + child.getGenome().toString()\n);
        //System.out.println("");
        
        return child;
    }
    
    @Override
    public int calculateFitness(Object o) {
        int currentFitness = 0;
        
        for (int gene : genome) {
            currentFitness += gene;
        }
        
        this.setFitness(currentFitness);
        return currentFitness;
    }
    
}
