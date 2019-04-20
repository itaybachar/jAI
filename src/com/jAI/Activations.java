package com.jAI;

/**
 * Class that stores Activation functions and their derivatives
 * can be accessed as such:
 * Activations::*Function name*
 */
public class Activations {
    public static double sigmoid(double x){
        return 1.0/(1.0+Math.exp(-x));
    }

    public static double sigmoidPrime(double x){
        return (sigmoid(x)*(1-sigmoid(x)));
    }

    public static double tanh(double x){
        return (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
    }

    public static double tanhPrime(double x){
        return (1-Math.pow(tanh(x),2));
    }

    public static double rectifier(double x){
        return Math.max(0,x);
    }

    public static double rectifierPrime(double x){
        if(x>=0)
            return 1;
        else return 0;
    }

    public static double step(double x){
        if(x>=0.9)
            return 1;
        else return 0;
    }
}
