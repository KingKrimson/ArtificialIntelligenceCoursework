/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetwork;

import java.util.ArrayList;

/**
 *
 * @author ad3-brown
 */
public abstract class Perceptron {
    private ArrayList<Connection> inputs;
    private Connection output;
    public double threshold;

    public Perceptron(ArrayList<Connection> inputs, Connection output, double threshold) {
        this.inputs = inputs;
        this.output = output;
        this.threshold = threshold;
    }

    public ArrayList<Connection> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Connection> inputs) {
        this.inputs = inputs;
    }

    public Connection getOutput() {
        return output;
    }

    public void setOutput(Connection output) {
        this.output = output;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public abstract void perceptronFunction();
    
}