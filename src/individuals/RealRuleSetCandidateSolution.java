/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package individuals;

import helpers.GenomeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * An individual consisting of several real valued numbers between 0 and 1. These
 * numbers generate a range which a input value must fall into, or a potential output
 * of 1 or 0. Converted into a realRuleSet in the fitness function.
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
     * Attempt to try each mutation operator.
     * @param probability
     */
    @Override
    public void mutation(double probability) {
        // generate mutation for variable rules.
        probability = (double)(1) / (size/ruleSize);    
        ruleSizeMutation(probability);
        shuffleMutation(probability);
        // modify the probablilty so that it's proportional to the overall length
        // of the genome, rather than the number of rules in the genome.
        double geneProbability = (probability * (size / ruleSize))/size;
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
            // add each rule, stored in it's own list, into another list.
            for (int i = 0; i < genome.size(); i = (i + ruleSize)) {
                List<Double> subList = genome.subList(i, (i + ruleSize));
                shuffle.add(subList);
            }
            // shuffle the rules, and then add them back into the genome.
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
     * add or remove a rule from the ruleset, depending on the probability.
     * 
     * @param probabilty
     */
    public void ruleSizeMutation(double probabilty) {
        Random randGen = new Random();
        double rand = randGen.nextDouble();

        if (rand < probabilty) {
            rand = randGen.nextDouble();
            if (rand < 0.5) {
                List newRule = GenomeHelper.generateRealRuleGenome(1, ruleSize);
                genome.addAll(newRule);
            } else {
                // delete rule
                this.genome = genome.subList(0, (genome.size() - ruleSize));
            }
            setSize(genome.size());
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

        // don't compare genome lengths, to allow variable rules
        
        //System.out.println(""); 
        //System.out.println("\nPARENT 1: " + this.getGenome().toString());
        //System.out.println("PARENT 2:" + partner.getGenome().toString());
        //System.out.println("CROSSOVER POINT: " + point);
        ArrayList<Double> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, partner.getSize()));

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
