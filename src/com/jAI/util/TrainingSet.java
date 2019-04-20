package com.jAI.util;

import java.util.ArrayList;

/**
 * Training set class.
 * This holds Training data as pairs of inputs and outputs.
 */
public class TrainingSet extends ArrayList<TrainingSet.Data> {

    /**
     * Constructor that initializes the data list with given inputs and outputs
     * @param inputs list of inputs
     * @param outputs list of outputs
     */
    public TrainingSet(double[][] inputs, double[][] outputs){
        super(inputs.length);
        for(int i =0;i<inputs.length;i++){
            add(new Data(inputs[i],outputs[i]));
        }
    }


    /**
     * Shuffle the data inside the set
     */
    public void shuffle(){
        for(int i = size()-1;i>0;i--){
            int j = (int)(Math.random()*i);
            Data temp = get(j);
            set(j,get(i));
            set(i,temp);
        }
    }

    /**
     * Pick random data from the set
     * @return Data object containing input and output
     */
    public Data pickRandom(){
        return get((int)(Math.random()*size()));
    }

    /**
     * Inner Class That stores data
     */
    public class Data{
        public double[] input,output; //Input and output of data

        /**
         * Constructor initializing the Data.
         * @param input input array
         * @param output output array
         */
        public Data(double[] input,double[] output){
            this.input = input;
            this.output = output;
        }
    }
}
