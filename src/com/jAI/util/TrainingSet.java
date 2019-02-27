package com.jAI.util;

import java.util.ArrayList;

//Class that handles training data as a couple(x,y)
public class TrainingSet {
    public ArrayList<Data> data;

    //Initialize Training set
    public TrainingSet(double[][] inputs, double[][] outputs){
        data = new ArrayList<>(inputs.length);
        for(int i =0;i<inputs.length;i++){
            data.add(new Data(inputs[i],outputs[i]));
        }
    }

    //Shuffles the training set
    public void shuffle(){
        for(int i = data.size()-1;i>0;i--){
            int j = (int)(Math.random()*i);
            Data temp = data.get(j);
            data.set(j,data.get(i));
            data.set(i,temp);
        }
    }

    public Data pickRandom(){
        return data.get((int)(Math.random()*data.size()));
    }

    public int getSize(){
        return data.size();
    }

    //Data Class stores an output and input
    public class Data{
        public double[] input,output;
        public Data(double[] input,double[] output){
            this.input = input;
            this.output = output;
        }
    }
}
