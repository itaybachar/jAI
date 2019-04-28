package jAI2.layers;

import jAI2.functions.errors.ErrorFunction;
import jAI2.functions.errors.MSE;

public class OutputLayer extends Layer{

    private ErrorFunction errorFunction;

    public OutputLayer(Layer prev) {
        super(prev.output_depth, prev.output_width, prev.output_height);
    }

    public OutputLayer(int output_depth,int output_width, int output_height) {
        super(output_depth, output_width, output_height);
    }

    public Layer setErrorFunction(ErrorFunction errorFunction){
        this.errorFunction =errorFunction;
        return this;
    }

    public ErrorFunction getErrorFunction(){return errorFunction;}

    public void calculateError(double[][][] exp){
        errorFunction.apply(this,exp);
    }

    public double calculateOverallError(double[][][] exp){return errorFunction.overall_error(this,exp);}

    @Override
    public void build() {
        if(errorFunction == null) errorFunction = new MSE();
    }

    @Override
    public void feedForward() {
        outputs = getPrevLayer().outputs;
        output_derivatives = getPrevLayer().output_derivatives;
    }

    @Override
    public void backprop() {
        getPrevLayer().errors = this.errors;
    }

    @Override
    public void updateWeights(double learning_rate) {

    }

    @Override
    public void printWeights(){}
}
