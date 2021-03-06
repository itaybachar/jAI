package jAI2.functions.activations;


public class Sigmoid extends ActivationFunction {
    public double activation(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }
    public double activationPrime(double x) {
        return activation(x)*(1-activation(x));
    }
}
