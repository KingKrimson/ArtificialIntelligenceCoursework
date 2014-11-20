/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ad3-brown
 */
public class RuleSetCandidateSolution extends CandidateSolution<Integer>{
    int ruleSize;
    
    public RuleSetCandidateSolution(ArrayList<Integer> genome) {
        super(genome);
        if (genome.size() % 7 == 0 && genome.size() % 12 == 0) {
            ruleSize = 12;
        } else if (genome.size() % 7 == 0) {
            ruleSize = 7;
        } else {
            ruleSize = 12;
        }
    }
    
    @Override
    public void mutation(double probability) {
        shuffleMutation(probability);
        double geneProbability = probability / (size / ruleSize);
        geneMutation(geneProbability);
    }
    
    //FIX ME
    public void shuffleMutation(double probabilty) {
        Random randGen = new Random();
        double rand = randGen.nextDouble();
        
        if (rand < probabilty) {
            ArrayList<Integer> newGenome = new ArrayList<>();
            ArrayList<List<Integer>> shuffle = new ArrayList<>();
            for (int i = 0; i < genome.size(); i = (i + ruleSize)) {
                List<Integer> subList = genome.subList(i, (i + ruleSize));
                shuffle.add(subList);
            }
            Collections.shuffle(shuffle);
            for (List<Integer> rule : shuffle) {
                newGenome.addAll(rule);
            }
            genome = newGenome;
        }
    }
    
    public void geneMutation(double probability) {
        Random randGen = new Random();
        double rand;
        int totalMutations = 0;
        
        for (int i = 0; i < this.getSize(); i++) {
            rand = randGen.nextDouble();
            if (rand <= probability) {
                //System.out.println("MUTATING POINT " + i);
                totalMutations++;
                int value = genome.get(i);
                rand = randGen.nextDouble();
                if (value == 0) {
                    if (rand <= 0.90) {
                        genome.set(i, 1);
                    } else {
                        genome.set(i, 2);
                    }
                } else if (value == 1) {
                    if (rand <= 0.90) {
                        genome.set(i, 0);
                    } else {
                        genome.set(i, 2);
                    }
                } else {
                    if (rand <= 0.5) {
                        genome.set(i, 0);
                    } else {
                        genome.set(i, 1);
                    }
                }    
            }
        }
    }
    
    public void ruleSizeMutation() {
        // add 50%
        // add rule at random point in between rules
        // size += ruleSize
        // remove 50%
        // remove random rule
        // size +- ruleSize
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
        
        CandidateSolution child = new RuleSetCandidateSolution(childGenome);
        //System.out.println("CHILD: " + child.getGenome().toString()\n);
        //System.out.println("");
        
        return child;
    }
    
}
