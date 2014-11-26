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
public class ArtificialNeuralNetwork {
    private ArrayList<Perceptron> inputLayer = new ArrayList<>();
    private ArrayList<Perceptron> hiddenLayer = new ArrayList<>();
    private Perceptron outputLayer;
    private Perceptron bias;
    
    // todo: add bias?
    // todo: change representation so the weights for each neuron aren't sequential?
    // for example 1 = first weight on first neuron, 5 = second weight...
    public ArtificialNeuralNetwork(ArrayList<Double> weightList, int inputNumber, int hiddenNumber) {
        int expectedSize = weightList.size();
        // +1 to account for the bias weights
        int actualSize = ((inputNumber + 1) * hiddenNumber) + hiddenNumber;
        if (expectedSize != actualSize) {
            throw new RuntimeException("Weight list size does not match amount of weights. List: " + expectedSize + " Actual: " + actualSize);
        }
        
        int weightIndex = -1;
        // generate bias
        ArrayList<Connection> biasInput = new ArrayList<>();
        biasInput.add(new Connection(1, 1));
        ArrayList<Connection> biasWeights = new ArrayList<>();
        for (int i = 0; i < hiddenNumber; i++) {
            // bias always outputs 1, to stop the decision plane from going 
            // through the origin.
            biasWeights.add(new Connection(1, weightList.get(++weightIndex)));
        }
        bias = new Perceptron(biasInput, biasWeights);
        
        // generate input layer
        for (int i = 0; i < inputNumber; i++) {
            // generate input connections
            ArrayList<Connection> inputValue = new ArrayList<>();
            inputValue.add(new Connection(-1, 1));
            
            // generate output connections.
            ArrayList<Connection> outputValues = new ArrayList<>();
            for (int j = 0; j < hiddenNumber; j++) {
                Connection con = new Connection(-1, weightList.get(++weightIndex));
                outputValues.add(con);
            }
            
            Perceptron p = new Perceptron(inputValue, outputValues);
            inputLayer.add(p);
        }
        
        // generate hidden layer
        for (int i = 0; i < hiddenNumber; i++) {
            // generate input connections (output from input layer and bias)
            ArrayList<Connection> inputValues = new ArrayList<>();
            
            Connection biasCon = bias.getOutputs().get(i);
            inputValues.add(biasCon);
            for (int j = 0; j < inputNumber; j++) {
                Connection con = inputLayer.get(j).getOutputs().get(i);
                inputValues.add(con);
            }
            
            // generate output connection
            ArrayList<Connection> outputValue = new ArrayList<>();
            outputValue.add(new Connection(-1, weightList.get(++weightIndex)));
            
            Perceptron p = new Perceptron(inputValues, outputValue);
            hiddenLayer.add(p);
        }
        
        // generate output layer
        ArrayList<Connection> inputValues = new ArrayList<>();
        for (Perceptron p : hiddenLayer) {
            Connection con = p.getOutputs().get(0);
            inputValues.add(con);
        }
        
        ArrayList<Connection> outputValue = new ArrayList<>();
        outputValue.add(new Connection(-1, 1));
        this.outputLayer = new Perceptron(inputValues, outputValue);
    }
    
    // Don't sigmoid the inputs. Leave them as is.
    public int calcValue(ArrayList<Double> inputs) {
        if (inputs.size() != inputLayer.size()) {
            throw new RuntimeException("Inputs given do not match size of input layer. Inputs: " + inputs.size() + " Input Layer:" + inputLayer.size());
        }
        
        // put values into input layer
        for (int i = 0; i < inputLayer.size(); i++) {
            Perceptron p = inputLayer.get(i);
            p.setInputValues(inputs.get(i));
        }
        
        // get sigmoid for each hidden neuron. Bias is part of the input, so will be updated as well.
        for (Perceptron p : hiddenLayer) {
            p.sigmoidFunction();
        }
        
        // get sigmoid for output
        double value = outputLayer.sigmoidFunction();
        int finalValue = value < 0.5 ? 0 : 1;
        
        return finalValue;
    }
    
    public void backpropagate() {
        // Not yet implemented. Probably won't need it, but leaving it here just in case.
    }

    public ArrayList<Perceptron> getInputLayer() {
        return inputLayer;
    }

    public void setInputLayer(ArrayList<Perceptron> inputLayer) {
        this.inputLayer = inputLayer;
    }

    public ArrayList<Perceptron> getHiddenLayer() {
        return hiddenLayer;
    }

    public void setHiddenLayer(ArrayList<Perceptron> hiddenLayer) {
        this.hiddenLayer = hiddenLayer;
    }

    public Perceptron getOutputLayer() {
        return outputLayer;
    }

    public void setOutputLayer(Perceptron outputLayer) {
        this.outputLayer = outputLayer;
    }
}
