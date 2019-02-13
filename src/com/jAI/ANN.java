package com.jAI;

import java.util.function.Function;

/**
 * This is a simple artificial neural network class
 * that allows both SGD(mini-batches) and GD.
 * This supports multiple hidden layers but still uses GD which
 * does not work well with a deep net.
 */
public class ANN {

    private double learningRate;
    private Matrix[] B; //Bias per layer
    private Matrix[] W; //Weights per layer
    private Matrix[] H; //Hidden layer outputs
    private Matrix I,O; //Input layer and Output layer
    private Function<Double,Double> activation;

    public ANN(int inputs, int[] hidden, int outputs,double learningRate,Function<Double,Double> activation){
        this.learningRate = learningRate;
        this.activation = activation;
        //Initialize Arrays
        this.I = new Matrix(1,inputs);
        this.H = new Matrix[hidden.length];
        this.W = new Matrix[hidden.length+1];
        this.B = new Matrix[hidden.length+1];

        //Initialize Matrices
        //Connection from input layer to first hidden
        this.W[0] = new Matrix(inputs,hidden[0]);
        this.B[0] = new Matrix(1,hidden[0]);

        //Connection from hidden layer to next layer
        for(int i = 1; i<hidden.length;i++){

            //Initialize matrices
            this.W[i] = new Matrix(hidden[i-1],hidden[i]);
            this.B[i] = new Matrix(1,hidden[i]);

        }

        //Connection of last hidden to output
        this.W[W.length-1] = new Matrix(hidden[hidden.length-1],outputs);
        this.B[B.length-1] = new Matrix(1,outputs);

        //Randomize values
        for(int i = 0; i<W.length;i++) {
            this.W[i].randomize(0.00001, 1);
            this.B[i].randomize(0.00001, 1);
        }

    }

    //Compute the output of an input
    public Matrix feedforward(double[] inputs){
        //Initialize the inputs
        I.set(new double[][]{inputs});

        //Apply to first hidden layer
        H[0] = Matrix.dot(I,W[0]);
        H[0].add(B[0]);
        H[0].applyFunction(activation);

        //Apply to rest of hidden layers
        for(int i = 1; i<H.length;i++){
            H[i] = Matrix.dot(H[i-1],W[i]);
            H[i].add(B[i]);
            H[i].applyFunction(activation);
        }

        //Apply to output layer
        O = Matrix.dot(H[H.length-1],W[W.length-1]);
        O.add(B[B.length-1]);
        O.applyFunction(activation);
        return O;
    }

    //Set the learning rate
    public void setLearningRate(double in){
        this.learningRate = in;
    }

    
}
