package com.jAI;

public class Activations {
    public static double sigmoid(double x){
        return 1.0/(1.0+Math.exp(-1*x));
    }

    public static double sigmoidPrime(double x){
        return (sigmoid(x)*(1-sigmoid(x)));
    }
}
