/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuralnetwork;

/**
 * Connections between layers in the neural net. Has a value, and a weight.
 * @author ad3-brown
 */
public class Connection {
    private double value;
    private double weight;
    
    /**
     * Create a connection with the given value, and weight. 
     * 
     * @param value
     * @param weight
     */
    public Connection(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public double getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    /**
     * return the value multiplied by the weight. 
     * 
     * @return
     */
    public double getOutput() {
        return weight * value;
    }
}
