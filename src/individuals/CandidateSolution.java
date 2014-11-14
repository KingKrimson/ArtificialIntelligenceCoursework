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
 * @param <T>
 */
public abstract class CandidateSolution<T> implements Comparable<CandidateSolution> {
    public int size;
    public ArrayList<T> genome;
    public int fitness;
 
    public CandidateSolution(ArrayList<T> genome) {
        this.size = genome.size();
        this.genome = genome;
        this.fitness = 0;
    }
    
    public abstract CandidateSolution crossover(int point, CandidateSolution partner);

    public abstract void mutation(double probability);

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<T> getGenome() {
        return genome;
    }

    public void setGenome(ArrayList<T> genome) {
        this.genome = genome;
    }

    public int getFitness() {
        return fitness;
    }

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
