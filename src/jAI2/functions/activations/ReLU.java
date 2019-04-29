package jAI2.functions.activations;

public class ReLU extends ActivationFunction {
    @Override
    public double activation(double x) {
        return Math.max(0,x);
    }

    @Override
    public double activationPrime(double x) {
        return x<0? 0:1;
    }
}
