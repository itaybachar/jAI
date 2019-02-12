package com.jAI;

public class Activations {
    public static double sigmoid(double x){
        return 1.0/(1.0+Math.exp(-1*x));
    }
}
