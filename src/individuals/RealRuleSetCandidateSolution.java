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

    /**
     *
     * @param genome
     */
    public RealRuleSetCandidateSolution(ArrayList<Double> genome) {
        super(genome);
        this.ruleSize = 13;
    }

    /**
     *
     * @param probability
     */
    @Override
    public void mutation(double probability) {
        shuffleMutation(probability);
        // modify the probablilty so that it's proportional to the overall length
        // of the genome, rather than the number of rules in the genome.
        double geneProbability = probability / (size / ruleSize);
        geneCreepMutation(geneProbability);
    }

    /**
     * Shuffle the rules to create a new ordering. This might allow a 'correct' rule
     * to capture inputs that were previously being captured by an 'incorrect' rule
     * 
     * @param probabilty
     */
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

    /**
     * For each gene, add or subtract a value from the gene if the probability
     * is met. The value to subtract is in the normally distributed range
     * between -0.25 and 0.25. This is known as creep mutation.
     *
     * @param probability
     */
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
     * @ret
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
