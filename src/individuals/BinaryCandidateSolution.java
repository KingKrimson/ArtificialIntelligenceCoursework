/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;
import java.util.Random;

/**
 * an individual simply consisting of 1's and 0's.
 * @author ad3-brown
 */
public class BinaryCandidateSolution extends CandidateSolution<Integer> {
    
    /**
     * construct a BinaryCandidateSolution with the given genome.
     * @param genome
     */
    public BinaryCandidateSolution(ArrayList<Integer> genome) {
        super(genome);
    }
    
    /**
     * for each gene in the genome, flip the bit if the probability for mutation
     * is met.
     * 
     * @param probability
     */
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
    
    /**
     * Given another CandidateSolution and a point to crossover, perform the 
     * crossover. This CandidateSolution becomes the first section of the new
     * solution, and the parameter CandidateSolution becomes the second section.
     * 
     * To get two children from two parents, call this function on both of them,
     * with the other parent as a parameter.
     * @param point
     * @param partner
     * @return
     */
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
}
