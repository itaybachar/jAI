package jAI2.functions.errors;

import jAI2.layers.OutputLayer;

public abstract class ErrorFunction {
    public abstract void apply(OutputLayer in, double[][][] exp_out);
    public abstract double overall_error(OutputLayer in, double[][][] exp_out);

}
