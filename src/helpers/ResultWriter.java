/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import individuals.CandidateSolution;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jxl.*;
import jxl.write.*;
import jxl.write.Number; 

/**
 *
 * @author ad3-brown
 */
public class ResultWriter {
    
    private final WritableWorkbook workbook;
    private final boolean printToConsole;
    private final int population;
    private final int genomeLength;
    private int yAxis;
    
    public ResultWriter(int population, int genomeLength, boolean printToConsole)
        throws WriteException, IOException {
        this.population = population;
        this.genomeLength = genomeLength;
        this.printToConsole = printToConsole;
        yAxis = 0;
        
        Date currentDate = new Date();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        String currentDateString = currentDateFormat.format(currentDate);
        
        workbook = Workbook.createWorkbook(new File("results/results_" + currentDateString + ".csv"));
        WritableSheet sheet = workbook.createSheet("Fitness results", 0);
        
        Label  popL    = new Label (0, yAxis, "Population");
        Number popN    = new Number(1, yAxis, population);
        Label  lengthL = new Label (2, yAxis, "Genome Length");
        Number lengthN = new Number(3, yAxis, genomeLength);
        
        sheet.addCell(popL);
        sheet.addCell(popN);
        sheet.addCell(lengthL);
        sheet.addCell(lengthN);
        yAxis++;       
        
        Label gen       = new Label(0, yAxis, "Generation");
        Label best      = new Label(1, yAxis, "Best Fitness");
        Label mean      = new Label(2, yAxis, "Mean Fitness");
        Label candidate = new Label(3, yAxis, "Best Candidate");
        
        sheet.addCell(gen);
        sheet.addCell(best);
        sheet.addCell(mean);
        sheet.addCell(candidate);
        yAxis++;
        
        workbook.write();
    }
    
    public void write(int gen, ArrayList<CandidateSolution> population)
        throws WriteException, IOException {
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
        
        Number genN  = new Number(0, yAxis, gen);
        Number bestN = new Number(1, yAxis, bestFitness);
        Number meanN = new Number(2, yAxis, meanFitness);
        Label candidateL = new Label(3, yAxis, bestCandidate.getGenome().toString());
        
        WritableSheet sheet = workbook.getSheet(0);
        
        sheet.addCell(genN);
        sheet.addCell(bestN);
        sheet.addCell(meanN);
        sheet.addCell(candidateL);
        
        workbook.write();
        if (printToConsole) {
            System.out.println("Gen: " + gen + " Best Fitness: " + bestFitness + " Mean Fitness: " + meanFitness + " Best Candidate: " + bestCandidate.getGenome().toString());
        }
        
        yAxis++;
    }
    
    public void close() 
        throws WriteException, IOException {
        workbook.close();
    }
}
