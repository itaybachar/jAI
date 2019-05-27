package jAI2.layers;

import jAI2.util.NetworkTools;

public abstract class Layer {

    int input_depth,input_width,input_height;
    int output_depth,output_width,output_height;
    double[][][] outputs;
    protected double[][][] output_derivatives;
    protected double[][][] errors;

    private Layer prevLayer;
    private Layer nextLayer;

    public Layer(){

    }

    public Layer(int output_depth,int output_width, int output_height){
        this.output_depth = output_depth;
        this.output_width = output_width;
        this.output_height = output_height;
    }

    public void connectToNextLayer(Layer _nextLayer){

        this.nextLayer = _nextLayer;
        _nextLayer.prevLayer = this;

        _nextLayer.input_depth = this.output_depth;
        _nextLayer.input_width = this.output_width;
        _nextLayer.input_height = this.output_height;

        if(this.output_depth < 0 || this.output_width<0 || this.output_height<0){
            throw new RuntimeException("Bad Dimensions!");
        }

        _nextLayer.initArrays();
        _nextLayer.build();
    }

    public void initArrays(){
        outputs = new double[output_depth][output_width][output_height];
        output_derivatives = new double[output_depth][output_width][output_height];
        errors = new double[output_depth][output_width][output_height];
    }

    public abstract void build();

    public abstract void feedForward();
    public void feedForwardNetwork(){
        feedForward();

        if(nextLayer != null){
             nextLayer.feedForwardNetwork();
        }
    }

    public abstract void backprop();
    public void backpropNetwork(){
        backprop();

        if(prevLayer != null){
             prevLayer.backpropNetwork();
        }
    }

    public abstract void updateWeights(double learning_rate);
    public void updateWeightsNetwork(double learning_rate){
        updateWeights(learning_rate);
        if(nextLayer != null){
            nextLayer.updateWeightsNetwork(learning_rate);
        }
    }


    public double[][][] getOutputs() {
        return outputs;
    }

    public boolean inMatchingDimensions(double[][][] in){
        return in.length == input_depth &&
                in[0].length == input_width &&
                in[0][0].length == input_height;
    }

    public boolean outMatchingDimensions(double[][][] in){
        return in.length == output_depth &&
                in[0].length == output_width &&
                in[0][0].length == output_height;
    }


    public double[][][] getOutput_derivatives() {
        return output_derivatives;
    }

    public double[][][] getErrors() {
        return errors;
    }

    public Layer getPrevLayer() {
        return prevLayer;
    }

    public Layer getNextLayer() {
        return nextLayer;
    }

    public int getOutput_depth() {
        return output_depth;
    }

    public int getOutput_width() {
        return output_width;
    }

    public int getOutput_height() {
        return output_height;
    }

    public abstract void printWeights();

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append('\n');
        sb.append("Output:\n");
        sb.append(NetworkTools.arrayToString(outputs));
        sb.append("\nDerivatives:\n");
        sb.append(NetworkTools.arrayToString(output_derivatives));
        sb.append("\nErrors:\n");
        sb.append(NetworkTools.arrayToString(errors));
        sb.append('\n');
        return sb.toString();
    }
}
