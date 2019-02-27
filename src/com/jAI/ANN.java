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

    //Apply gradient descent to the network, this will train on one epoch.
    public void SGD(double[] target_outputs) {
        Matrix.Assert(target_outputs.length == O.getCols(), "Bad Dimensions");
        Matrix EO = Matrix.fromArray(new double[][]{target_outputs});

        //Get the Errors
        Matrix[] Errors = calculateError(EO);

        //Apply the Errors
        applyErrors(Errors);
    }

    public void learn(double[][] inputs,double[][] target_outputs,int minibatch_size){
        //Create and shuffle training set
        TrainingSet trainingSet = new TrainingSet(inputs, target_outputs);

    }

    //Apply Stochastic Gradient descent to a network given a dataset, this will train on one epoch.
    public void mini_batch(double[][] inputs,double[][] target_outputs,int minibatch_size,boolean verbose) {
        //Create and shuffle training set
        TrainingSet trainingSet = new TrainingSet(inputs, target_outputs);
        trainingSet.shuffle();

        //Train through the minibatches
        for (int i = 0; i < trainingSet.getSize() / minibatch_size; i++) {
            Matrix[] Errors = new Matrix[H.length + 1];
            Matrix[] wErrors = new Matrix[H.length+1];

            //Forward propagate through the data and add errors
            for (int j = 0; j < minibatch_size; j++) {
                TrainingSet.Data data = trainingSet.data.get(i * minibatch_size + j);

                //Feed Forward
                feedForward(data.input);

                //Set the Expected Output
                Matrix EO = Matrix.fromArray(new double[][]{data.output});

                //Set the Errors if first iteration
                if (j == 0) {
                    Errors = calculateError(EO);

                    wErrors[0] = Matrix.dot(Matrix.transpose(I),Errors[0]);
                    for(int l = 1; l<Errors.length;l++) {
                        wErrors[l] = Matrix.dot(Matrix.transpose(H[l - 1]), Errors[l]);
                    }
                } else {
                    Matrix[] currentErrors = calculateError(EO);
                    Matrix[] currentWErrors = new Matrix[H.length+1];

                    currentWErrors[0] = Matrix.dot(Matrix.transpose(I),currentErrors[0]);
                    for(int l = 1; l<Errors.length;l++) {
                        currentWErrors[l] = Matrix.dot(Matrix.transpose(H[l - 1]), currentErrors[l]);
                    }

                    //Sum up the Errors
                    for (int k = 0; k < currentErrors.length; k++) {
                        Errors[k].add(currentErrors[k]);
                    }
                }
            }

            //Average the values
            for (int j = 0; j < Errors.length; j++) {
                Errors[j].divide(minibatch_size);
                wErrors[j].divide(minibatch_size);
            }

            //Apply the errors
//            applyErrors(Errors);

            W[0].subtract(wErrors[0]); //Adjust Weight
            B[0].subtract(Matrix.multiply(Errors[0],learningRate)); //Adjust Bias

            //Rest of network
            for(int l = 1; l<Errors.length;l++){
                W[l].subtract(wErrors[l]); //Adjust Weight
                B[l].subtract(Matrix.multiply(Errors[l],learningRate)); //Adjust Bias
            }

            if (verbose)
                System.out.println("Finished minibatch #" + (i + 1));
        }
    }

    //Calculate the layer error with backpropagation
    private Matrix[] calculateError(Matrix target){
        Matrix[] Errors = new Matrix[H.length+1];

        //Calculate output Error
        Errors[Errors.length-1] = O.clone();
        Errors[Errors.length-1].subtract(target);
        Errors[Errors.length-1].hadamard(Matrix.applyFunction(Z[Z.length-1],activationPrime));

        //Calculate hidden errors
        for(int i = Errors.length-2; i>=0;i--){
            Errors[i] = Matrix.dot(Errors[i+1],Matrix.transpose(W[i+1]));
            Errors[i].hadamard(Matrix.applyFunction(Z[i],activationPrime));
        }
        return Errors;
    }

    private void applyErrors(Matrix[] Errors){
        //Apply Error to layers
        //Start with first weight and bias
        Matrix dCdW = Matrix.dot(Matrix.transpose(I),Errors[0]);

        W[0].subtract(dCdW); //Adjust Weight
        B[0].subtract(Matrix.multiply(Errors[0],learningRate)); //Adjust Bias

        //Rest of network
        for(int i = 1; i<Errors.length;i++){
            dCdW = Matrix.dot(Matrix.transpose(H[i-1]),Errors[i]);

            W[i].subtract(dCdW); //Adjust Weight
            B[i].subtract(Matrix.multiply(Errors[i],learningRate)); //Adjust Bias
        }
    }


}
