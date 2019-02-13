package com.jAI;

import java.util.function.Function;

/**
 * This is a simple artificial neural network class
 * that allows both stochastic GD and normal GD.
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
        this.H = new Matrix[hidden.length];
        this.W = new Matrix[hidden.length +1];
        this.B = new Matrix[hidden.length+1];

        //Initialize Matrices
        this.W[0] = new Matrix(inputs,hidden[0]);
        this.B[0] = new Matrix(1,hidden[0]);

        for(int i = 1; i<=hidden.length;i++){
            int currentLayerSize,nextLayerSize;
            //Get next layer's size in the network
            if(i<hidden.length-1){
                nextLayerSize = hidden[i+1];
            }else nextLayerSize = outputs;

            //Get the current layer's size
            if(i<hidden.length){
                currentLayerSize = hidden[i];
            }else currentLayerSize = outputs;

            //Initialize matrices
            System.out.println(currentLayerSize + " " + nextLayerSize);
            this.W[i] = new Matrix(currentLayerSize,nextLayerSize);
            this.B[i] = new Matrix(1,currentLayerSize);

            //Randomize values
            this.W[i].randomize(0.00001,1);
            this.B[i].randomize(0.00001,1);
        }
    }

    public Matrix feedforward(double[] inputs){
        I = Matrix.fromArray(new double[][]{inputs});

        H[0] = Matrix.dot(I,W[0]);
        H[0].add(B[0]);
        H[0].applyFunction(activation);

        O = Matrix.dot(H[0],W[1]);
        O.add(B[1]);
        O.applyFunction(activation);

//        //Apply to first hidden layer
//        H[0] = Matrix.dot(I,W[0]);
//        H[0].add(B[0]);
//        H[0].applyFunction(activation);
//
//        //Apply to rest of hidden layers
//        for(int i = 1; i<H.length;i++){
//            H[i] = Matrix.dot(W[i],H[i-1]);
//            H[i].add(B[i]);
//            H[i].applyFunction(activation);
//            H[i].print();
//        }
//
//        //Apply to output layer
//        O = Matrix.dot(H[H.length-1],W[W.length-1]);
//        O.add(B[B.length-1]);
//        O.applyFunction(activation);
        return O;
    }
}
