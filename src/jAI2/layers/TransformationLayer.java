package jAI2.layers;

import jAI2.Network;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

public class TransformationLayer extends Layer{

    public void initArrays(){
        this.output_depth = 1;
        this.output_width =1;
        this.output_height = this.input_depth*this.input_width*this.input_height;

        super.initArrays();
    }

    @Override
    public void build() {
    }

    private int mapIndex(int depth,int width,int height){
        return depth*this.input_width*input_height + width*this.input_height + height;
    }

    @Override
    public void feedForward() {
        for(int i = 0; i<input_depth;i++){
            for (int j = 0;j<input_width;j++){
                for(int k =0;k<input_height;k++){
                    this.outputs[0][0][mapIndex(i,j,k)] = getPrevLayer().outputs[i][j][k];
                    this.output_derivatives[0][0][mapIndex(i,j,k)] = getPrevLayer().output_derivatives[i][j][k];
                }
            }
        }
    }

    @Override
    public void backprop() {
        for(int i = 0; i<input_depth;i++){
            for (int j = 0;j<input_width;j++){
                for(int k =0;k<input_height;k++){
                    this.getPrevLayer().errors[i][j][k] = this.errors[0][0][mapIndex(i,j,k)];
                }
            }
        }
    }

    @Override
    public void updateWeights(double learning_rate) {

    }

    @Override
    public void printWeights() {

    }
}
