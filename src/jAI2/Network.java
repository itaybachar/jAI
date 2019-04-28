package jAI2;

import jAI2.layers.InputLayer;
import jAI2.layers.Layer;
import jAI2.layers.OutputLayer;
import jAI2.util.TrainingSet3D;

import java.util.ArrayList;
import java.util.Arrays;

public class Network {
    private InputLayer input;
    private OutputLayer output;

    public InputLayer getInputLayer() {
        return input;
    }

    public Network setInputLayer(InputLayer input) {
        this.input = input;
        return this;
    }

    public OutputLayer getOutputLayer() {
        return output;
    }

    public Network setOutputLayer(OutputLayer output) {
        this.output = output;
        return this;
    }

    public double[][][] predict(double[][][] input){
        this.input.setInputs(input);
        this.input.feedForwardNetwork();
        return this.output.getOutputs();
    }

    public void calculateError(double[][][] exp){
        output.calculateError(exp);
    }


    /**
     * Trains the network using GD with mini-batches
     *
     * @param data Training set of data
     * @param minibatch_size data entries per batch
     * @param batches_per_epoch how many batches to train on the network, if negative then will train on the whole dataset
     * @param epochs number of epochs
     * @param learningRate the learning rate of the system
     */
    public void train(TrainingSet3D data,int minibatch_size,int batches_per_epoch, int epochs,double learningRate){
        //Calculate Batches per epoch if needed
        if(batches_per_epoch<0){
            batches_per_epoch = data.size()/minibatch_size;
        }

        //Loop Through Epochs
        for(int i = 0; i<epochs;i++){
            TrainingSet3D.Data[][] minibatches = data.createBatches(minibatch_size,batches_per_epoch);

            double totalError = 0;
            for(int j = 0;j<batches_per_epoch;j++){
                for(int k = 0; k<minibatch_size;k++){
                    input.setInputs(minibatches[j][k].input);
                    input.feedForwardNetwork();
                    output.calculateError(minibatches[j][k].output);
                    totalError += output.calculateOverallError(minibatches[j][k].output);
                    output.backpropNetwork();
                    input.updateWeightsNetwork(learningRate);
                }
            }
            totalError/=batches_per_epoch;
            System.out.println("Loss: " + totalError);
        }
    }
}
