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
 * @author Andrew
 */
public class RealRuleSetCandidateSolution extends CandidateSolution<Double> {

    int ruleSize;

    public RealRuleSetCandidateSolution(ArrayList<Double> genome) {
        super(genome);
        this.ruleSize = 13;
    }

    @Override
    public void mutation(double probability) {
        shuffleMutation(probability);
        double geneProbability = probability / (size / ruleSize);
        geneCreepMutation(geneProbability);
    }

    public void shuffleMutation(double probabilty) {
        Random randGen = new Random();
        double rand = randGen.nextDouble();

        if (rand < probabilty) {
            ArrayList<Double> newGenome = new ArrayList<>();
            ArrayList<List<Double>> shuffle = new ArrayList<>();
            for (int i = 0; i < genome.size(); i = (i + ruleSize)) {
                List<Double> subList = genome.subList(i, (i + ruleSize));
                shuffle.add(subList);
            }
            Collections.shuffle(shuffle);
            for (List<Double> rule : shuffle) {
                newGenome.addAll(rule);
            }
            genome = newGenome;
        }
    }

    public void geneCreepMutation(double probability) {
        Random randGen = new Random();
        double rand;
        int totalMutations = 0;

        for (int i = 0; i < this.getSize(); i++) {
            rand = randGen.nextDouble();
            if (rand <= probability) {
                //System.out.println("MUTATING POINT " + i);
                totalMutations++;
                double value = genome.get(i);
                if ((i + 1) % ruleSize == 0) {
                    if ((int) value == 1) {
                        genome.set(i, 1.0);
                    } else {
                        genome.set(i, 0.0);
                    }
                } else {
                    // normal distribution of +- 0.25
                    double creep = (randGen.nextGaussian() * 0.25);
                    double newValue = value + creep;
                    if (newValue > 1) {
                        newValue = 1.0;
                    } else if (newValue < 0) {
                        newValue = 0.0;
                    }
                    genome.set(i, newValue);
                }
            }
        }
    }

    public void geneRandomMutation(double probability) {
        Random randGen = new Random();
        double rand;
        int totalMutations = 0;

        for (int i = 0; i < this.getSize(); i++) {
            rand = randGen.nextDouble();
            if (rand <= probability) {
                //System.out.println("MUTATING POINT " + i);
                totalMutations++;
                // normal distribution of +- 0.25
                genome.set(i, randGen.nextDouble());
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
        ArrayList<Double> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, genome.size()));

        CandidateSolution child = new RealRuleSetCandidateSolution(childGenome);
        //System.out.println("CHILD: " + child.getGenome().toString()\n);
        //System.out.println("");

        return child;
    }

    @Override
    public String toString() {
        String rules = "";
        for (Double d : genome) {
            rules += d.toString() + " ";
        }
        return rules.trim();
    }
}
