package jAI2.functions.activations;

public abstract class ActivationFunction {
    public abstract void apply(double[][][] output, double[][][] output_derivative);
}
