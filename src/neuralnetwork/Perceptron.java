/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple perceptron/neuron. It has a list of inputs, and a list of outputs.
 * The perceptron function is just the sigmoid function. It operates on the summed
 * value of the weight*value from each input connection.
 * @author ad3-brown
 */
public class Perceptron {
    private List<Connection> inputs;
    private List<Connection> outputs;

    /**
     * Create a new Perceptron with the given inputs and outputs.
     * @param inputs
     * @param outputs
     */
    public Perceptron(List<Connection> inputs, List<Connection> outputs) {
        this.inputs = new ArrayList<>(inputs);
        this.outputs = new ArrayList<>(outputs);
    }

    /**
     *
     * @return
     */
    public List<Connection> getInputs() {
        return inputs;
    }

    /**
     *
     * @param inputs
     */
    public void setInputs(List<Connection> inputs) {
        this.inputs = inputs;
    }

    /**
     *
     * @return
     */
    public List<Connection> getOutputs() {
        return outputs;
    }

    /**
     *
     * @param outputs
     */
    public void setOutput(List<Connection> outputs) {
        this.outputs = outputs;
    }
    
    // Sigmoid activation

    /**
     * generate a new output value, based on the sigmoid function.
     * @return
     */
        public double sigmoidFunction() {
        double totalSum = 0;
        // collect the summed outputs of the input connection.
        for (Connection con : inputs) {
            totalSum += con.getOutput();
        }

        // perform sigmoid function on summed value.
        double sigmoid = (1 / (1 + Math.pow(Math.E, (-1 * totalSum))));
        
        // set the value for each output connection.
        for (Connection con : outputs) {
            con.setValue(sigmoid);
        }
        
        return sigmoid;
    }

    /**
     * used for populating the input layer with values provided by the calling
     * function. This allows the ANN to quickly add the input values given to
     * it by the fitness function.
     * 
     * @param input
     */
        public void setInputValues(double input) {
        for (Connection inputCon : inputs) {
            inputCon.setValue(input);
        }
        
        for (Connection outputCon : outputs) {
            outputCon.setValue(input);
        }
    }    
}
