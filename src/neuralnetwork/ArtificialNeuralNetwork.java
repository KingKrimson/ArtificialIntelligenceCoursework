/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * An artificial neural network, consisting of a input layer, a hidden layer,
 * and an output layer.
 * 
 * @author ad3-brown
 */
public class ArtificialNeuralNetwork {
    private List<Perceptron> inputLayer = new ArrayList<>();
    private List<Perceptron> hiddenLayer = new ArrayList<>();
    private Perceptron outputLayer;
    private Perceptron bias;

    /**
     * Constructs a neural network with inputNumber number of nodes in the input
     * layer, hiddenNumber number of nodes in the hidden layer, and one node in
     * the output layer. The first set of weights in the weight list is mapped onto the output
     * connections of the input layer, and the second set are mapped onto the outputs
     * of the hidden layer.
     * 
     * @param weightList
     * @param inputNumber
     * @param hiddenNumber
     */
        public ArtificialNeuralNetwork(List<Double> weightList, int inputNumber, int hiddenNumber) {
        int actualSize = weightList.size();
        // Make sure that the number of weights is correct. +1 in the input layer to account for the bias weights.
        int expectedSize = ((inputNumber + 1) * hiddenNumber) + hiddenNumber;
        if (expectedSize != actualSize) {
            throw new RuntimeException("Weight list size does not match amount of weights. List: " + actualSize + " Expected: " + expectedSize);
        }
        
        int weightIndex = -1;
        // generate bias node.
        List<Connection> biasInput = new ArrayList<>();
        biasInput.add(new Connection(1, 1));
        List<Connection> biasWeights = new ArrayList<>();
        for (int i = 0; i < hiddenNumber; i++) {
            // bias always outputs 1, to stop the decision plane from going 
            // through the origin.
            biasWeights.add(new Connection(1, weightList.get(++weightIndex)));
        }
        bias = new Perceptron(biasInput, biasWeights);
        
        // generate input layer
        for (int i = 0; i < inputNumber; i++) {
            // generate input connections
            List<Connection> inputValue = new ArrayList<>();
            // input value hasn't been set yet, so set it to -1 for now.
            // weight is alway one for the inputs.
            inputValue.add(new Connection(-1, 1));
            
            // generate output connections, with weights.
            List<Connection> outputValues = new ArrayList<>();
            for (int j = 0; j < hiddenNumber; j++) {
                Connection con = new Connection(-1, weightList.get(++weightIndex));
                outputValues.add(con);
            }
            
            Perceptron p = new Perceptron(inputValue, outputValues);
            inputLayer.add(p);
        }
        
        // generate hidden layer. Each node in the hidden layer has an connection
        // from each node in the input layer, and another connection from the bias.
        for (int i = 0; i < hiddenNumber; i++) {
            // generate input connections (output from input layer and bias)
            List<Connection> inputValues = new ArrayList<>();
            
            Connection biasCon = bias.getOutputs().get(i);
            inputValues.add(biasCon);
            for (int j = 0; j < inputNumber; j++) {
                Connection con = inputLayer.get(j).getOutputs().get(i);
                inputValues.add(con);
            }
            
            // generate output connection
            List<Connection> outputValue = new ArrayList<>();
            outputValue.add(new Connection(-1, weightList.get(++weightIndex)));
            
            Perceptron p = new Perceptron(inputValues, outputValue);
            hiddenLayer.add(p);
        }
        
        // generate output layer. The output layer has a connection from each
        // node in the hidden layer.
        List<Connection> inputValues = new ArrayList<>();
        for (Perceptron p : hiddenLayer) {
            Connection con = p.getOutputs().get(0);
            inputValues.add(con);
        }
        
        List<Connection> outputValue = new ArrayList<>();
        outputValue.add(new Connection(-1, 1));
        this.outputLayer = new Perceptron(inputValues, outputValue);
    }
    
    /**
     * Given a list of input values, calculate the expected output values using
     * the neural net.
     * 
     * @param inputs
     * @return
     */
        public int calcValue(ArrayList<Double> inputs) {
        if (inputs.size() != inputLayer.size()) {
            throw new RuntimeException("Inputs given do not match size of input layer. Inputs: " + inputs.size() + " Input Layer:" + inputLayer.size());
        }
        
        // put values into input layer
        for (int i = 0; i < inputLayer.size(); i++) {
            Perceptron p = inputLayer.get(i);
            p.setInputValues(inputs.get(i));
        }
        
        // get sigmoid for each hidden neuron. The summed input includes the bias,
        // as each node has a connection to it.
        for (Perceptron p : hiddenLayer) {
            p.sigmoidFunction();
        }
        
        // get sigmoid for output
        double value = outputLayer.sigmoidFunction();
        // return 0 if value is < that 0.5, or 1 if it's greater.
        int finalValue = value < 0.5 ? 0 : 1;
        
        return finalValue;
    }
    
    /**
     *
     */
    public void backpropagate() {
        // Not yet implemented. Probably won't need it, but leaving it here just in case.
    }

    /**
     *
     * @return
     */
    public List<Perceptron> getInputLayer() {
        return inputLayer;
    }

    /**
     *
     * @param inputLayer
     */
    public void setInputLayer(List<Perceptron> inputLayer) {
        this.inputLayer = inputLayer;
    }

    /**
     *
     * @return
     */
    public List<Perceptron> getHiddenLayer() {
        return hiddenLayer;
    }

    /**
     *
     * @param hiddenLayer
     */
    public void setHiddenLayer(List<Perceptron> hiddenLayer) {
        this.hiddenLayer = hiddenLayer;
    }

    /**
     *
     * @return
     */
    public Perceptron getOutputLayer() {
        return outputLayer;
    }

    /**
     *
     * @param outputLayer
     */
    public void setOutputLayer(Perceptron outputLayer) {
        this.outputLayer = outputLayer;
    }
}
