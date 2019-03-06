package com.jAI.util;

/**
 * This is a pair class, which holds two error deltas,
 * This was part of javafx but since it was removed I made one.
 *
 */

public class Pair{
    public final Matrix[] dCdW;
    public final Matrix[] dCdB;

    public Pair(Matrix[] first, Matrix[] second){
        this.dCdW = first;
        this.dCdB = second;
    }

    public void add(Pair p){
        for(int i = 0; i<dCdW.length;i++){
            dCdW[i].add(p.dCdW[i]);
            dCdB[i].add(p.dCdB[i]);
        }
    }

    public void multiply(double scalar){
        for(int i = 0; i<dCdW.length;i++){
            dCdW[i].multiply(scalar);
            dCdB[i].multiply(scalar);
        }
    }
}
