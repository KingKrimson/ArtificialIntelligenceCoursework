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
public class Perceptron {
    private ArrayList<Connection> inputs;
    private ArrayList<Connection> outputs;

    public Perceptron(ArrayList<Connection> inputs, ArrayList<Connection> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public ArrayList<Connection> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Connection> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<Connection> getOutputs() {
        return outputs;
    }

    public void setOutput(ArrayList<Connection> outputs) {
        this.outputs = outputs;
    }
    
    // Sigmoid activation
    public double sigmoidFunction() {
        double totalSum = 0;
        for (Connection con : inputs) {
            totalSum += con.getOutput();
        }

        // Actual sigmoid function.
        double sigmoid = (1 / (1 + Math.pow(Math.E, (-1 * totalSum))));
        
        sigmoid = sigmoid < 0.5 ? 0 : sigmoid;
        
        for (Connection con : outputs) {
            con.setValue(sigmoid);
        }
        
        return sigmoid;
    }
    
    // For use populating the input layer.
    public void setInputValues(double input) {
        for (Connection inputCon : inputs) {
            inputCon.setValue(input);
        }
        
        for (Connection outputCon : outputs) {
            outputCon.setValue(input);
        }
    }
    
}
