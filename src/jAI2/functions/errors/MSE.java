package jAI2.functions.errors;

import jAI2.layers.OutputLayer;

public class MSE extends ErrorFunction{
    @Override
    public void apply(OutputLayer in, double[][][] exp_out) {
        if(in.isMatchingDimensions(exp_out))
        for(int d = 0; d< in.getOutput_depth();d++){
            for(int w = 0; w<in.getOutput_width();w++){
                for(int h = 0; h<in.getOutput_height();h++){
                    in.getErrors()[d][w][h] = in.getOutputs()[d][w][h]-exp_out[d][w][h];
                }
            }
        }
    }

    @Override
    public double overall_error(OutputLayer in, double[][][] exp_out) {
        if(in.isMatchingDimensions(exp_out)) {
            double sum = 0;
            for (int d = 0; d < in.getOutput_depth(); d++) {
                for (int w = 0; w < in.getOutput_width(); w++) {
                    for (int h = 0; h < in.getOutput_height(); h++) {
                        sum += Math.pow(in.getOutputs()[d][w][h]-exp_out[d][w][h],2);
                    }
                }
            }
            return sum /(2*in.getOutput_depth()*in.getOutput_width()*in.getOutput_height());
        }else return -500;
    }
}
