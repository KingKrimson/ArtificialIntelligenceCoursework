/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package individuals;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract CandidateSolution class, which is an individual. Provides methods 
 * that a CandidateSolution must implement. Also has a compareTo function, that 
 * allows a list of CandidateSolutions to be sorted.
 * 
 * @author ad3-brown
 * @param <T>
 */
public abstract class CandidateSolution<T> implements Comparable<CandidateSolution> {

    /**
     * the size of the solution.
     */
    public int size;

    /**
     * the genome of the solution
     */
    public List<T> genome;

    /**
     * fitness of the solution
     */
    public int fitness;
 
    /**
     *
     * @param genome
     */
    public CandidateSolution(ArrayList<T> genome) {
        this.size = genome.size();
        this.genome = genome;
        this.fitness = 0;
    }
    
    /**
     * crossover signature. Takes a point to crossover on, and a CandidateSolution
     * to crossover with. To get two new individuals, call it on two parents in
     * turn, with the other parent as a parameter.
     * 
     * @param point
     * @param partner
     * @return
     */
    public abstract CandidateSolution crossover(int point, CandidateSolution partner);

    /**
     * Mutation signature. Takes a probability, which is the probablity to mutate.
     * @param probability
     */
    public abstract void mutation(double probability);

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return
     */
    public List<T> getGenome() {
        return genome;
    }

    /**
     *
     * @param genome
     */
    public void setGenome(ArrayList<T> genome) {
        this.genome = genome;
    }

    /**
     *
     * @return
     */
    public int getFitness() {
        return fitness;
    }

    /**
     *
     * @param fitness
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
     
    @Override
    public int compareTo(CandidateSolution candidateSolution) {
        final int LESS = -1;
        final int EQUAL = 0;
        final int MORE = 1;    
        
        if (this.getFitness() < candidateSolution.getFitness()) {
            return LESS;
        } else if (this.getFitness() == candidateSolution.getFitness()) {
            return EQUAL;
        } else {
            return MORE;
        }
    }
    
    @Override
    public String toString() {
        String genomeString = "";
        for (T element : genome) {
            genomeString += element.toString();
        }
        return genomeString;
    }
}
