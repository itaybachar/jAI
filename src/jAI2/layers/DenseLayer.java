package jAI2.layers;

import jAI2.functions.activations.ActivationFunction;
import jAI2.functions.activations.Sigmoid;
import jAI2.functions.errors.MSE;
import jAI2.util.NetworkTools;

public class DenseLayer extends Layer {

    double[][] weights;
    double[] bias;

    private double lowerBiasRange =-1 ,higherBiasRange = 1;
    private double lowerWeightRange = Double.NaN,higherWeightRange = Double.NaN;

    private ActivationFunction activationFunction;

    public DenseLayer(int output_height){
        super(1,1,output_height);
    }

    public DenseLayer setActivationFunction(ActivationFunction function){
        this.activationFunction = function;
        return this;
    }


    public DenseLayer setWeightRange(double lower,double higher){
        this.lowerWeightRange = lower;
        this.higherWeightRange = higher;

        return this;
    }

    public DenseLayer setBiasRange(double lower,double higher){
        this.lowerBiasRange = lower;
        this.higherBiasRange = higher;

        return this;
    }

    public ActivationFunction getActivationFunction(){return activationFunction;}

    @Override
    public void build() throws Exception{
        if(input_depth>1 || input_width > 1){
            throw new Exception("Must Flatten Input!");
        }else{
            if(activationFunction == null){
                this.activationFunction = new Sigmoid();
            }
            if(weights == null || weights.length != output_height || weights[0].length != input_height){
                if(Double.isNaN(lowerWeightRange)|| Double.isNaN(higherWeightRange) ){
                    weights = NetworkTools.createRandomArray(output_height, input_height, -1/Math.sqrt(input_height), 1/Math.sqrt(input_height));
                } else {
                    weights = NetworkTools.createRandomArray(output_height, input_height, lowerWeightRange, higherWeightRange);
                }
            }
            if(bias == null || bias.length != output_height){
                bias = NetworkTools.createRandomArray(output_height,lowerBiasRange,higherBiasRange);
            }
        }
    }

    @Override
    public void feedForward() {
        for(int currentNeuron = 0; currentNeuron<output_height;currentNeuron++){
            double sum = bias[currentNeuron];
            for(int prevNeuron = 0; prevNeuron<input_height;prevNeuron++){
                sum+= weights[currentNeuron][prevNeuron]*getPrevLayer().outputs[0][0][prevNeuron];
            }
            outputs[0][0][currentNeuron] = sum;
        }
        activationFunction.apply(outputs,output_derivatives);
    }

    @Override
    public void backprop() {
        for(int prevNeuron = 0; prevNeuron<input_height;prevNeuron++){
            double sum = 0;
            for(int currentNeuron = 0; currentNeuron<output_height;currentNeuron++) {
                sum += errors[0][0][currentNeuron] * weights[currentNeuron][prevNeuron];
            }
            getPrevLayer().errors[0][0][prevNeuron] = getPrevLayer().getOutput_derivatives()[0][0][prevNeuron]*sum;
        }
    }

    @Override
    public void updateWeights(double learning_rate) {
        for(int currentNeuron = 0; currentNeuron<output_height;currentNeuron++){
            double delta = -learning_rate*errors[0][0][currentNeuron];
            bias[currentNeuron] += delta;
            for(int prevNeuron = 0; prevNeuron<input_height;prevNeuron++){
                weights[currentNeuron][prevNeuron] += delta*getPrevLayer().outputs[0][0][prevNeuron];
            }
        }
    }

    public void printWeights(){
        NetworkTools.printArray(new double[][][]{weights});
    }
}
