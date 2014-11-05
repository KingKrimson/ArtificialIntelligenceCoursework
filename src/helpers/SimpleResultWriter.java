/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import individuals.CandidateSolution;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ad3-brown
 */
public class SimpleResultWriter {
        
    private final boolean printToConsole;
    private final int population;
    private final int genomeLength;
    private File results;
    private BufferedWriter writer;
    
    public SimpleResultWriter(int population, int genomeLength, boolean printToConsole) 
        throws IOException {
        this.population = population;
        this.genomeLength = genomeLength;
        this.printToConsole = printToConsole;
        
        Date currentDate = new Date();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        String currentDateString = currentDateFormat.format(currentDate);
        
        results = new File("results/results_"+ currentDateString + ".csv").getAbsoluteFile();
        
        writer = new BufferedWriter(new FileWriter(results));
        writer.write("sep=|"); // set excel delimiter
        writer.newLine();
        writer.write("Population|" + population + "|Genome Length|" + genomeLength);
        writer.newLine();
        writer.write("Generation|Best Fitness|Mean Fitness|Best Candidate");
        writer.newLine();
        writer.flush();
        
    }
    
    public void write(int gen, ArrayList<CandidateSolution> population) 
        throws IOException {
        int totalFitness = 0;
        int bestFitness  = 0;
        int meanFitness  = 0;
        CandidateSolution bestCandidate = null;
                
        for (CandidateSolution candidateSolution : population) {
            int fitness  = candidateSolution.getFitness();            
            totalFitness += fitness;
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestCandidate = candidateSolution;
            }
        }
        
        meanFitness = totalFitness / population.size();

        writer.write("" + gen + "|" + bestFitness + "|" + meanFitness + "|" + bestCandidate.getGenome().toString());
        writer.newLine();
        writer.flush();
        if (printToConsole) {
            System.out.println("Gen: " + gen + " Best Fitness: " + bestFitness + " Mean Fitness: " + meanFitness + " Best Candidate: " + bestCandidate.getGenome().toString());
        }
    }
    
    public void close() 
        throws IOException {
        writer.close();
    }
}
