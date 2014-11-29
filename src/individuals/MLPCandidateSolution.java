 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;
import java.util.Random;

/**
 * Candidate solution for the Multi Layer Perceptron representation of dataset 3.
 * The genome is a list of doubles, which become weights in the Perceptron. 
 * 
 * @author ad3-brown
 */
public class MLPCandidateSolution extends CandidateSolution<Double> {
    
    /**
     * Create a MLPCandidateSolution with the given genome.
     * 
     * @param genome
     */
    public MLPCandidateSolution(ArrayList<Double> genome) {
        super(genome);
    }
    
    /**
     * For each gene, add or subtract a value from the gene if the probability 
     * is met. The value to subtract is in the normally distributed range 
     * between -0.25 and 0.25. This is known as creep mutation.
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
                totalMutations++;
                // normal distribution of +- 0.25
                double creep = (randGen.nextGaussian() * 0.25);
                genome.set(i, genome.get(i) + creep);
            }
        }
    }
    
    /**
     * perform varying crossover actions. Right now, only does standard point
     * crossover.
     * 
     * @param point
     * @param partner
     * @return
     */
    @Override
    public CandidateSolution crossover(int point, CandidateSolution partner) {
        return pointCrossover(point, partner);
    }
    
    /**
     * Given another CandidateSolution and a point to crossover, perform the 
     * crossover. This CandidateSolution becomes the first section of the new
     * solution, and the parameter CandidateSolution becomes the second section.
     * 
     * To get two children from two parents, call this function on both of them,
     * with the other parent as a parameter.
     *
     * @param point
     * @param partner
     * @return
     */
    public CandidateSolution pointCrossover(int point, CandidateSolution partner) {
        // should throw exception here?
        if (this.size != partner.size) {
            return partner;
        }
        //System.out.println(""); 
        //System.out.println("\nPARENT 1: " + this.getGenome().toString());
        //System.out.println("PARENT 2:" + partner.getGenome().toString());
        //System.out.println("CROSSOVER POINT: " + point);
        ArrayList<Double> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, genome.size()));
        
        CandidateSolution child = new MLPCandidateSolution(childGenome);
        return child;
    }
    
    // Still need to implement this.

    /**
     *
     * @param point
     * @param partner
     * @return
     */
        public CandidateSolution mergeCrossover(int point, CandidateSolution partner) {
        ArrayList<Double> childGenome = new ArrayList<>(this.size);
        
        // point to start merge
        // generate point to end merge
        // do merge
        
        CandidateSolution child = new MLPCandidateSolution(childGenome);
        return child;
    }
}
