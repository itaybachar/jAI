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
    private Matrix[] Z; //Weighted Sums from first hidden to output
    private Matrix[] Error; //Errors per layer
    private Matrix I,O; //Input layer and Output layer
    private Function<Double,Double> activation,activationPrime;

    public ANN(int inputs, int[] hidden, int outputs,double learningRate,Function<Double,Double> activation,Function<Double,Double> activationPrime){
        //Set Variables
        this.learningRate = learningRate;
        this.activation = activation;
        this.activationPrime = activationPrime;

        //Initialize Arrays
        this.I = new Matrix(1,inputs);
        this.H = new Matrix[hidden.length];
        this.W = new Matrix[hidden.length+1];
        this.B = new Matrix[hidden.length+1];
        this.Z = new Matrix[hidden.length+1];
        this.Error = new Matrix[hidden.length+1];

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
    public Matrix feedForward(double[] inputs){
        //Initialize the inputs
        I.set(new double[][]{inputs});

        //Apply to first hidden layer
        Z[0] = Matrix.dot(I,W[0]);
        Z[0].add(B[0]);
        H[0] = Z[0].clone();
        H[0].applyFunction(activation);

        //Apply to rest of hidden layers
        for(int i = 1; i<H.length;i++){
            Z[i] = Matrix.dot(H[i-1],W[i]);
            Z[i].add(B[i]);
            H[i] = Z[i].clone();
            H[i].applyFunction(activation);
        }

        //Apply to output layer
        Z[Z.length-1] = Matrix.dot(H[H.length-1],W[W.length-1]);
        Z[Z.length-1].add(B[B.length-1]);
        O = Z[Z.length-1].clone();
        O.applyFunction(activation);
        return O;
    }

    //Set the learning rate
    public void setLearningRate(double in){
        this.learningRate = in;
    }

    public void GD(double[] target_outputs) {
        Matrix.Assert(target_outputs.length == O.getCols(), "Bad Dimensions", Matrix.getLineNumber());
        Matrix EO = Matrix.fromArray(new double[][]{target_outputs});

        //Calculate output Error
        Error[Error.length-1] = O.clone();
        Error[Error.length-1].subtract(EO);
        Error[Error.length-1].hadamard(Matrix.applyFunction(Z[Z.length-1],activationPrime));

        //Calculate hidden errors
        for(int i = Error.length-2; i>=0;i--){
            Error[i] = Matrix.dot(Error[i+1],Matrix.transpose(W[i+1]));
            Error[i].hadamard(Matrix.applyFunction(Z[i],activationPrime));
        }

        //Apply Error to layers
        //Start with first weight and bias
        Matrix dCdW = Matrix.dot(Matrix.transpose(I),Error[0]);

        W[0].subtract(dCdW); //Adjust Weight
        B[0].subtract(Matrix.multiply(Error[0],learningRate)); //Adjust Bias

        //Rest of network
        for(int i = 1; i<Error.length;i++){
            dCdW = Matrix.dot(Matrix.transpose(H[i-1]),Error[i]);

            W[i].subtract(dCdW); //Adjust Weight
            B[i].subtract(Matrix.multiply(Error[i],learningRate)); //Adjust Bias
        }
    }

}
