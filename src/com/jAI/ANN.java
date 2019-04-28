package com.jAI;

import com.jAI.util.Matrix;
import com.jAI.util.Pair;
import com.jAI.util.TrainingSet;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * This is a simple artificial neural network class
 * that allows both SGD(mini-batches) and GD.
 * This supports multiple hidden layers but still uses GD which
 * does not work well with a deep net.
 */

public class ANN {

    private double learningRate;
    public Matrix[] B; //Bias per layer
    public Matrix[] W; //Weights per layer
    private Matrix[] H; //Hidden layer outputs
    private Matrix[] Z; //Weighted Sums from first hidden to output
    private Matrix I, O; //Input layer and Output layer
    private Function<Double, Double> activation, activationPrime; //Activation function and its derivative
    private boolean verbose = true; //NN will print outputs.

    /**
     * Constructor used to initialize a network with a
     * file, this is called from the loadNetwork Method.
     * Default Activation function is sigmoid.
     * @param I Input Matrix
     * @param W Weight Matrices
     * @param B Bias Matrices
     */
    private ANN(Matrix I, Matrix[] W, Matrix[] B) {
        this.I = I;
        this.W = W;
        this.B = B;

        H = new Matrix[W.length - 1];
        Z = new Matrix[W.length];

        activation = Activations::sigmoid;
        activationPrime = Activations::sigmoidPrime;
        learningRate = 0.1;
    }

    /**
     * Constructor used to initialize a new neural network
     * @param inputs Number of input neurons
     * @param hidden array indicating number of hidden neurons per hidden layer
     * @param outputs number of possible outputs
     * @param learningRate the learning rate of the network
     * @param activation activation function
     * @param activationPrime derivative of the activation
     */
    public ANN(int inputs, int[] hidden, int outputs, double learningRate, Function<Double, Double> activation, Function<Double, Double> activationPrime) {
        //Set Variables
        this.learningRate = learningRate;
        this.activation = activation;
        this.activationPrime = activationPrime;

        //Initialize Arrays
        this.I = new Matrix(1, inputs);
        this.H = new Matrix[hidden.length];
        this.W = new Matrix[hidden.length + 1];
        this.B = new Matrix[hidden.length + 1];
        this.Z = new Matrix[hidden.length + 1];

        //Initialize Matrices
        //Connection from input layer to first hidden
        this.W[0] = new Matrix(inputs, hidden[0]);
        this.B[0] = new Matrix(1, hidden[0]);

        //Connection from hidden layer to next layer
        for (int i = 1; i < hidden.length; i++) {

            //Initialize matrices
            this.W[i] = new Matrix(hidden[i - 1], hidden[i]);
            this.B[i] = new Matrix(1, hidden[i]);

        }

        //Connection of last hidden to output
        this.W[W.length - 1] = new Matrix(hidden[hidden.length - 1], outputs);
        this.B[B.length - 1] = new Matrix(1, outputs);

        //Randomize values
        for (int i = 0; i < W.length; i++) {
            this.W[i].randomize(-0.1, 0.1);
            this.B[i].randomize(-0.1, 0.1);
        }

    }

    /**
     * Compute the output of the network for a single input
     * @param inputs inputs per neuron
     * @return Output Matrix
     */
    public Matrix feedForward(double[] inputs) {
        //Initialize the inputs
        I.set(new double[][]{inputs});

        //Apply to first hidden layer
        Z[0] = Matrix.dot(I, W[0]);
        Z[0].add(B[0]);
        H[0] = Z[0].clone();
        H[0].applyFunction(activation);

        //Apply to rest of hidden layers
        for (int i = 1; i < H.length; i++) {
            Z[i] = Matrix.dot(H[i - 1], W[i]);
            Z[i].add(B[i]);
            H[i] = Z[i].clone();
            H[i].applyFunction(activation);
        }

        //Apply to output layer
        Z[Z.length - 1] = Matrix.dot(H[H.length - 1], W[W.length - 1]);
        Z[Z.length - 1].add(B[B.length - 1]);
        O = Z[Z.length - 1].clone();
        O.applyFunction(activation);
        return O;
    }

    /**
     * Set the learning rate
     * @param in new learning rate
     */
    public void setLearningRate(double in) {
        this.learningRate = in;
    }

    /**
     * Set the activation functions
     * @param activation new activation
     * @param activationPrime new derivative
     */
    public void setActivations(Function<Double, Double> activation, Function<Double, Double> activationPrime) {
        this.activation = activation;
        this.activationPrime = activationPrime;
    }

    public void learn(double[][] inputs,double[][] exp,int batchSize,int loops,int epochs){
        //Create training set
        TrainingSet trainingSet = new TrainingSet(inputs,exp);

        for(int epoch = 0; epoch<epochs;epoch++){
            //Print Progress
            print("\rTrained: " + epoch + "/" + epochs);

            //Shuffle training set
            trainingSet.shuffle();

            //Create the mini batches
            TrainingSet.Data[][] mini_batches = createMiniBatches(trainingSet,batchSize);

            for(int loop = 0; loop<loops;loop++){
                update_mini_batch(mini_batches[loop]);
            }
        }
        //Print Progress
        print("\rTrained: " + epochs + "/" + epochs);
        print("\nDone!");
    }

    /**
     * Teaches the network the data-set using
     * Stochastic Gradient Descent with mini batches
     * @param inputs network inputs
     * @param expected_outputs expected outputs
     * @param mini_batch_size size per mini batch
     * @param epochs number of epochs to train
     */
    public void SGD(double[][] inputs,double[][] expected_outputs,int mini_batch_size,int epochs){
        //Create training set
        TrainingSet trainingSet = new TrainingSet(inputs,expected_outputs);

        //Loop through Epochs
        for(int i = 0; i<epochs;i++){
            //Print Progress
            print("\rTrained: " + i + "/" + epochs);

            //Shuffle training set
            trainingSet.shuffle();

            //Create the mini batches
            TrainingSet.Data[][] mini_batches = createMiniBatches(trainingSet,mini_batch_size);

            //Loop through mini batches
            for(int j = 0; j<mini_batches.length;j++){
                update_mini_batch(mini_batches[j]);
            }
        }

        //Print Progress
        print("\rTrained: " + epochs + "/" + epochs);
        print("\nDone!");
    }

    public double MSE(TrainingSet.Data[] batch){
        double sum = 0;

        for(int i = 0; i<batch.length;i++) {
            Matrix out = feedForward(batch[i].input);

            for (int neuron = 0; neuron < out.getCols(); neuron++) {
                sum += (batch[i].output[neuron] - out.get(0,neuron)*(batch[i].output[neuron] - out.get(0,neuron)));
            }
        }

        return sum/(2*10*batch.length);
    }

    /**
     * Takes a dataset and returns a 2D array containing the data
     * split up into batches of n size
     * @param t training set
     * @param mini_batch_size size per mini batch
     * @return 2D array of mini batches
     */
    public TrainingSet.Data[][] createMiniBatches(TrainingSet t, int mini_batch_size){
        TrainingSet.Data[][] mini_batches = new TrainingSet.Data[t.size()/mini_batch_size][mini_batch_size];

        //Loop Through the entire training set to create batches
        for(int i = 0; i<mini_batches.length;i++){
            //Create mini batch array

            //Loop through and fill the array
            for(int j = 0; j<mini_batch_size;j++){
                mini_batches[i][j]= t.get(j+(i*mini_batch_size));
            }
        }
        return mini_batches;
    }

    /**
     * Does a whole pass(feed-forward and back-propagate)
     * with inputs and returns the delta W and B
     * for the certain input.
     * @param inputs inputs to net
     * @param target_outputs expected outputs
     * @return a pair containing two arrays with the deltas per layer for B and W
     */
    public Pair backprop(double[] inputs, double[] target_outputs){
        //Create Expected output column matrix
        Matrix EO = Matrix.fromArray(new double[][]{target_outputs});

        //Forward Propagate inputs
        feedForward(inputs);

        //Get the Errors which is also the Bias Delta
        Matrix[] Errors = calculateError(EO);

        //Weight Delta Matrix
        Matrix[] dCdW = new Matrix[Errors.length];

        //Calculate the Deltas
        //Calculating the first Layers Delta
        dCdW[0] = Matrix.dot(Matrix.transpose(I),Errors[0]);

        //Rest of network
        for (int i = 1; i < Errors.length; i++) {
            dCdW[i] = Matrix.dot(Matrix.transpose(H[i - 1]), Errors[i]);
        }

        return new Pair(dCdW,Errors);
    }

    /**
     * Updates the Weights and Biases of the network
     * for a given mini batch
     * @param mini_batch mini batch
     */
    public void update_mini_batch(TrainingSet.Data[] mini_batch){
        //Get first deltas
        Pair deltas = backprop(mini_batch[0].input,mini_batch[0].output);

        //Loop through mini batch and sum the deltas
        for(int i = 1; i< mini_batch.length;i++){
            deltas.add(backprop(mini_batch[i].input,mini_batch[i].output));
        }

        //Multiply deltas by the learning rate
        //and divide by the mini batch size to get
        //the mean of the deltas
        deltas.multiply(learningRate/mini_batch.length);

        //Update Weights and Biases
        for(int i= 0; i<W.length;i++){
            W[i].subtract(deltas.dCdW[i]);
            B[i].subtract(deltas.dCdB[i]);
        }
    }

    /**
     * Test the network on given inputs and get accuracy
     * @param inputs tests.test inputs
     * @param target_outputs expected outputs
     * @param test_num number of tests to run
     */
    public void test_network(double[][] inputs, double[][] target_outputs, int test_num) {
        setVerbose(true);
        TrainingSet trainingSet = new TrainingSet(inputs, target_outputs);

        int correct = 0;
        int tested = 0;
        for (int i = 0; i < test_num; i++) {
            TrainingSet.Data data = trainingSet.pickRandom();
            Matrix res = feedForward(data.input);

            tested++;
            if (res.getCoordsOfMax()[1] == Matrix.fromArray(new double[][]{data.output}).getCoordsOfMax()[1])
                correct++;

            DecimalFormat decimalFormat = new DecimalFormat("#.##%");
            print("\r" + decimalFormat.format((double) correct / tested) + " Correct");
        }
       print("\n");
    }


    /**
     * Calculate the Error of the network using a target output.
     * This uses gradient descent to get the Errors per layer of the network
      * @param target expected output
     * @return Error Matrix per layer
     */
    private Matrix[] calculateError(Matrix target) {
        Matrix[] Errors = new Matrix[H.length + 1];

        //Calculate output Error
        Errors[Errors.length - 1] = O.clone();
        Errors[Errors.length - 1].subtract(target);
        Errors[Errors.length - 1].hadamard(Matrix.applyFunction(Z[Z.length - 1], activationPrime));

        //Calculate hidden jAI2.functions.errors
        for (int i = Errors.length - 2; i >= 0; i--) {
            Errors[i] = Matrix.dot(Errors[i + 1], Matrix.transpose(W[i + 1]));
            Errors[i].hadamard(Matrix.applyFunction(Z[i], activationPrime));
        }
        return Errors;
    }

    /**
     * Prints out message to console if NN is verbose
     * @param message message to print
     */
    private void print(String message){
        if(verbose)
            System.out.print(message);
    }

    /**
     * Setting the verbosity of the Network
     * @param verbose is verbose
     */
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    /**
     * Save current Neural network as a .ann file.
     * @param path path to desired save locations
     * @param name name of the file/network
     */
    public void saveNetwork(String path, String name) {
        File file = new File(path + name + ".ann");

        StringBuilder sb = new StringBuilder();
        sb.append("I ");
        sb.append(I.getCols() + "\n");

        //Add Weights
        sb.append("W ");
        sb.append(W.length + "\n");
        for (int i = 0; i < W.length; i++) {
            sb.append(W[i].toString());
        }

        //Add Biases
        sb.append("B ");
        sb.append(B.length + "\n");
        for (int i = 0; i < B.length; i++) {
            sb.append(B[i].toString());
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a neural network from a .ann file
     * @param file File of saved network
     * @return ANN object that has saved values
     */
    public static ANN loadNetwork(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            Matrix I = null;
            Matrix[] W = null, B = null;
            int cols;
            while ((line = bufferedReader.readLine()) != null) {
                char c = line.charAt(0);

                switch (c) {
                    case 'I':
                        cols = Integer.parseInt(line.substring(2));
                        I = new Matrix(1, cols);
                        break;
                    case 'W':
                    case 'B':
                        int length = Integer.parseInt(line.substring(2));
                        if (c == 'B')
                            B = new Matrix[length];
                        else
                            W = new Matrix[length];

                        for (int i = 0; i < length; i++) {
                            ArrayList<String> s = new ArrayList<>();
                            line = bufferedReader.readLine();
                            if (line.equals("{")) {
                                while (!(line = bufferedReader.readLine()).equals("}")) {
                                    s.add(line);
                                }
                            }
                            if (c == 'B')
                                B[i] = Matrix.toMatrix(s);
                            else
                                W[i] = Matrix.toMatrix(s);

                        }

                        break;
                }
            }
            return new ANN(I, W, B);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not load network");
    }

}
