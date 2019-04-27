package jAI2.util;

import com.jAI.util.TrainingSet;

import java.util.ArrayList;

/**
 * Training set class.
 * This holds Training data as pairs of inputs and outputs.
 */
public class TrainingSet3D extends ArrayList<TrainingSet3D.Data> {
    public int data_depth,data_width;
    /**
     * Constructor that initializes the data list with given inputs and outputs
     * @param inputs list of inputs
     * @param outputs list of outputs
     */
    public TrainingSet3D(double[][][] inputs, double[][][] outputs){
        super(inputs.length*inputs[0].length);

        this.data_depth = inputs.length;
        this.data_width = inputs[0].length;

        for(int i =0;i<inputs.length;i++){
            for(int j = 0; j<inputs[0].length;j++) {
                add(new Data(inputs[i][j], outputs[i][j]));
            }
        }
    }

    public TrainingSet3D(int data_depth,int data_width){
        this.data_depth = data_depth;
        this.data_width = data_width;
    }

    public Data get(int depth,int width){
        return get(depth*data_depth + width);
    }

    public TrainingSet3D split(int from_index,int amount){
        if(from_index+amount<size()){
            TrainingSet3D newSet = new TrainingSet3D(this.data_depth,this.data_width);

            for(int i = from_index; i<from_index+amount;i++)
                newSet.add(get(i));
            return newSet;
        }else return null;
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
