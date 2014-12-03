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
 * A CandidateSolution where the genome consists of 1's 0's, and 2's. These numbers
 * are arranged into rules in the fitness function. A rule is either 7 or 12
 * numbers long, depending on whether it's for dataset 1 or 2. Converted into
 * a BinaryRuleSet in the fitness function.
 * 
 * @author ad3-brown
 */
public class BinaryRuleSetCandidateSolution extends CandidateSolution<Integer> {

    int ruleSize;

    /**
     * create a BinaryRuleSetCandidateSolution with the given genome.
     * @param genome
     */
    public BinaryRuleSetCandidateSolution(ArrayList<Integer> genome) {
        super(genome);
        // Really hacky way to figure out the size of a rule.
        if (genome.size() % 7 == 0 && genome.size() % 12 == 0) {
            ruleSize = 12;
        } else if (genome.size() % 7 == 0) {
            ruleSize = 7;
        } else {
            ruleSize = 12;
        }
    }

    /**
     * attempt to perform each mutation in turn.
     * @param probability
     */
    @Override
    public void mutation(double probability) {
        // generate probability for variable rules.
        probability = (double)(1) / (size/ruleSize);
        ruleSizeMutation(probability);
        shuffleMutation(probability);
        // modify the probablilty so that it's proportional to the overall length
        // of the genome, rather than the number of rules in the genome.
        double geneProbability = (probability * (size / ruleSize))/size;
        geneMutation(geneProbability);
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
            ArrayList<Integer> newGenome = new ArrayList<>();
            ArrayList<List<Integer>> shuffle = new ArrayList<>();
            // add each rule, stored in it's own list, into another list.
            for (int i = 0; i < genome.size(); i = (i + ruleSize)) {
                List<Integer> subList = genome.subList(i, (i + ruleSize));
                shuffle.add(subList);
            }
            // Shuffle the list, and add the rules back into the genome.
            Collections.shuffle(shuffle);
            for (List<Integer> rule : shuffle) {
                newGenome.addAll(rule);
            }
            genome = newGenome;
        }
    }

    /**
     * for each gene in the genome, mutate a gene if the generated random number.
     * is less than the probability. 'Action' genes are flipped. Condition 
     * genes have a 90% chance of being flipped.and a 10% chance of becoming a wild card.
     * 
     * @param probability
     */
    public void geneMutation(double probability) {
        Random randGen = new Random();
        double rand;
        int totalMutations = 0;

        for (int i = 0; i < this.genome.size(); i++) {
            rand = randGen.nextDouble();
            if (rand <= probability) {
                //System.out.println("MUTATING POINT " + i);
                totalMutations++;
                int value = genome.get(i);
                rand = randGen.nextDouble();
                if ((i + 1) % ruleSize == 0) {
                    if (value == 0) {
                        genome.set(i, 1);
                    } else {
                        genome.set(i, 0);
                    }
                } else {
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
                // add a rule
                List newRule = GenomeHelper.generateBinaryRuleGenome(1, ruleSize);
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
     * @param point
     * @param partner
     * @return
     */
    @Override
    public CandidateSolution crossover(int point, CandidateSolution partner) {

        // Allow for crossover between rulesets with variable rulesizes.
        
        //System.out.println(""); 
        //System.out.println("\nPARENT 1: " + this.getGenome().toString());
        //System.out.println("PARENT 2:" + partner.getGenome().toString());
        //System.out.println("CROSSOVER POINT: " + point);
        ArrayList<Integer> childGenome = new ArrayList<>(this.size);
        childGenome.addAll(this.getGenome().subList(0, point));
        childGenome.addAll(partner.getGenome().subList(point, partner.getSize()));

        CandidateSolution child = new BinaryRuleSetCandidateSolution(childGenome);
        //System.out.println("CHILD: " + child.getGenome().toString()\n);
        //System.out.println("");

        return child;
    }

}
