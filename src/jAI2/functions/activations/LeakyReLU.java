package jAI2.functions.activations;

public class LeakyReLU extends ActivationFunction{

    private double slope_leak;

    public LeakyReLU(double slope_leak) {
        this.slope_leak = slope_leak;
    }

    @Override
    public double activation(double x) {
        return Math.max(slope_leak*x,x);
    }

    @Override
    public double activationPrime(double x) {
        return x<0?slope_leak:1;
    }


}
