/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * A class that collects data from the csv's, and averages them out for graph
 * making.
 *
 * @author Andrew
 */
public class Averager {

    private List<List<Integer>> bestList;
    private List<List<Integer>> meanList;
    private List<Integer> averageBest;
    private List<Integer> averageMean;
    private List<Integer> stopPoints;
    private List<Integer> ruleSizes;

    private double meanTrainingPassed;
    private double meanRealPassed;

    private int number;
    private int trainingSize;
    private int realSize;
    private int averageGenerations;

    public Averager(int trainingSize, int realSize) {
        this.trainingSize = trainingSize;
        this.realSize = realSize;
        bestList = new ArrayList<>();
        meanList = new ArrayList<>();
        stopPoints = new ArrayList<>();
        averageBest = new ArrayList<>();
        averageMean = new ArrayList<>();
        ruleSizes = new ArrayList<>();
        number = 0;
        meanTrainingPassed = 0;
        meanRealPassed = 0;
    }

    public void addResults(String relativeFileLocation)
            throws FileNotFoundException {
        File file = new File("results/" + relativeFileLocation + ".csv").getAbsoluteFile();
        Scanner scan = new Scanner(file);
        List<Integer> best = new ArrayList<>();
        List<Integer> mean = new ArrayList<>();

        ++number;

        // first three lines aren't useful.
        for (int i = 0; i < 3; ++i) {
            scan.nextLine();
        }

        String[] prevData = null;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] data = line.split("\\|");
            if (data[0].equals("BEST INDIVIDUAL")) {
                if (prevData != null) {
                    String[] individual = prevData[3].split(",");
                    int size = individual.length;
                    int numberOfRules;
                    if (size % 12 == 0) {
                        numberOfRules = size/12;
                    } else {
                        numberOfRules = size/13;
                    }
                    ruleSizes.add(numberOfRules);
                }
                break;
            }
            best.add(Integer.parseInt(data[1]));
            mean.add(Integer.parseInt(data[2]));
            prevData = data;

        }
        
        //skip column header.
        scan.nextLine();
        // get real results
        String[] data = scan.nextLine().split("\\|");
        meanRealPassed += Integer.parseInt(data[1]);
        // get training results
        data = scan.nextLine().split("\\|");
        meanTrainingPassed += Integer.parseInt(data[1]);
        bestList.add(best);
        meanList.add(mean);
        scan.close();
    }

    public void averageWriteClear(String fileLocation)
            throws IOException {
        average();
        write(fileLocation);
        clear();
    }

    // clear the results for a new run.
    public void clear() {
        bestList.clear();
        meanList.clear();
        stopPoints.clear();
        averageBest.clear();
        averageMean.clear();
        ruleSizes.clear();
        number = 0;
        averageGenerations = 0;
        meanTrainingPassed = 0;
        meanRealPassed = 0;
    }

    // average out the collected results.
    public void average() {
        averageBest.clear();
        averageMean.clear();

        for (List<Integer> best : bestList) {
            int point;
            int stopPoint = best.size();
            stopPoints.add(stopPoint);
            for (point = 0; point < stopPoint; point++) {
                int bestVal = best.get(point);
                if (point < averageBest.size()) {
                    averageBest.set(point, averageBest.get(point) + bestVal);
                } else {
                    averageBest.add(bestVal);
                }
            }
        }

        Collections.sort(stopPoints);
        for (int point : stopPoints) {
            averageGenerations += point;
        }

        averageGenerations = averageGenerations / number;

        for (List<Integer> mean : meanList) {
            int point;
            for (point = 0; point < mean.size(); point++) {
                int meanVal = mean.get(point);
                if (point < averageMean.size()) {
                    averageMean.set(point, averageMean.get(point) + meanVal);
                } else {
                    averageMean.add(meanVal);
                }
            }
        }

        int divisor = number;
        int stopIndex = 0;
        int stopGen = stopPoints.get(stopIndex);
        for (int i = 0; i < averageBest.size(); i++) {
            while (i > stopGen) {
                divisor--;
                if ((stopIndex+1) < stopPoints.size()) {
                    stopGen = stopPoints.get(++stopIndex);
                }
            }
            int bestValue = averageBest.get(i);
            int bestMean = (bestValue / divisor);
            averageBest.set(i, bestMean);

            int meanValue = averageMean.get(i);
            int meanMean = (meanValue / divisor);
            averageMean.set(i, meanMean);
        }

        meanTrainingPassed = meanTrainingPassed / number;
        meanRealPassed = meanRealPassed / number;
    }

    // write the collected averages.
    public void write(String fileString)
            throws IOException {
        double meanTrainingFailed = trainingSize - meanTrainingPassed;
        double meanRealFailed = realSize - meanRealPassed;
        double meanTotalPassed = meanTrainingPassed + meanRealPassed;
        double meanTotalFailed = (trainingSize + realSize) - meanTotalPassed;

        DecimalFormat df = new DecimalFormat("#.##");

        String realPercentagePass = df.format((double) meanRealPassed / realSize * 100);
        String realPercentageFail = df.format(100 - Double.valueOf(realPercentagePass));
        String trainingPercentagePass = df.format((double) meanTrainingPassed / trainingSize * 100);
        String trainingPercentageFail = df.format(100 - Double.valueOf(trainingPercentagePass));
        String totalPercentagePass = df.format((double) meanTotalPassed / (trainingSize + realSize) * 100);
        String totalPercentageFail = df.format(100 - Double.valueOf(totalPercentagePass));

        File results = new File("results/" + fileString + ".csv");

        
        double averageRuleSize = 0;
        
        for (int ruleSize : ruleSizes) {
            averageRuleSize += ruleSize;
        }
        averageRuleSize = (double)(averageRuleSize)/number;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(results));
        writer.write("sep=|"); // set excel delimiter
        writer.newLine();
        writer.write("NUMBER OF RUNS: 10");
        writer.newLine();
        writer.write("SHORTEST RUN STOPPED AT|" + stopPoints.get(0));
        writer.newLine();
        writer.write("LONGEST RUN STOPPED AT|" + stopPoints.get(stopPoints.size() - 1));
        writer.newLine();
        writer.write("AVERAGE GENERATION|" + averageGenerations);
        writer.newLine();
        writer.write("RULE SIZES|" + ruleSizes.toString());
        writer.newLine();
        writer.write("AVERAGE RULE SIZE|" + averageRuleSize);
        writer.newLine();
        writer.write("Generation|Best Fitness|Mean Fitness");
        writer.newLine();

        for (int i = 0; i < averageBest.size(); ++i) {
            writer.write(i + "|" + averageBest.get(i) + "|" + averageMean.get(i));
            writer.newLine();
        }

        writer.write("FINAL RESULTS|PASS|FAIL|PERCENT PASSED|PERCENT FAILED|TOTAL");
        writer.newLine();
        writer.write("REAL|" + meanRealPassed + "|" + meanRealFailed
                + "|" + realPercentagePass + "|" + realPercentageFail
                + "|" + realSize);
        writer.newLine();
        writer.write("TRAINING|" + meanTrainingPassed + "|" + meanTrainingFailed
                + "|" + trainingPercentagePass + "|" + trainingPercentageFail
                + "|" + trainingSize);
        writer.newLine();
        writer.write("TOTAL|" + meanTotalPassed + "|" + meanTotalFailed
                + "|" + totalPercentagePass + "|" + totalPercentageFail
                + "|" + (trainingSize + realSize));
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
