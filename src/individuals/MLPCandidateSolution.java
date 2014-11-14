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
public class MLPCandidateSolution extends CandidateSolution<Double> {
    
    public MLPCandidateSolution(ArrayList<Double> genome) {
        super(genome);
    }
    
    // creep mutation
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
    
    @Override
    public CandidateSolution crossover(int point, CandidateSolution partner) {
        return pointCrossover(point, partner);
    }
    
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
    
    // implement this.
    public CandidateSolution mergeCrossover(int point, CandidateSolution partner) {
        ArrayList<Double> childGenome = new ArrayList<>(this.size);
        
        // point to start merge
        // generate point to end merge
        // do merge
        
        CandidateSolution child = new MLPCandidateSolution(childGenome);
        return child;
    }
}
