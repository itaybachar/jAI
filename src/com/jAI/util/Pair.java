package com.jAI.util;

/**
 * This class holds a pair of Matrix arrays.
 * This have the Weight error and the bias error per layer.
 */

public class Pair{
    public Matrix[] dCdW; //Weight Delta
    public Matrix[] dCdB; //Bias Delta

    /**
     * Constructor to initialize Pair with the errors
     * @param first Weight Errors
     * @param second Bias Errors
     */
    public Pair(Matrix[] first, Matrix[] second){
        this.dCdW = first;
        this.dCdB = second;
    }

    /**
     * Add two pairs together
     * @param p pair to add
     */
    public void add(Pair p){
        for(int i = 0; i<dCdW.length;i++){
            dCdW[i].add(p.dCdW[i]);
            dCdB[i].add(p.dCdB[i]);
        }
    }

    /**
     * Multiply by a scalar value
     * @param scalar value to multiply
     */
    public void multiply(double scalar){
        for(int i = 0; i<dCdW.length;i++){
            dCdW[i].multiply(scalar);
            dCdB[i].multiply(scalar);
        }
    }
}
