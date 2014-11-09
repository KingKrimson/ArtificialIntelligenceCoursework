 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;

/**
 *
 * @author ad3-brown
 */
public class MLPCandidateSolution extends CandidateSolution<Double> {
    
    public MLPCandidateSolution(ArrayList<Double> genome) {
        super(genome);
    }
    
    @Override
    public void mutation(double proabability) {
        
    }
    
    @Override
    public CandidateSolution crossover(int point, CandidateSolution partner) {
        ArrayList<Double> childGenome = new ArrayList<>();
        
        CandidateSolution child = new MLPCandidateSolution(childGenome);
        return child;
    }
    
    @Override
    public int calculateFitness(Object o) {
        return 0;
    }
    
}
