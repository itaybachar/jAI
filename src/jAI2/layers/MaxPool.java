package jAI2.layers;

import jAI2.Network;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

public class MaxPool extends Layer {

    private int stride;
    private int[] kernel_size;
    private int[][][] mask;

    public MaxPool(int stride, int... kernel_size){
        this.stride = stride;
        this.kernel_size = kernel_size;
    }

    public void initArrays(){
        this.output_depth = input_depth;
        this.output_width = (input_width-kernel_size[0])/stride + 1;
        this.output_height = (input_height-kernel_size[1])/stride +1;
        super.initArrays();
    }

    @Override
    public void build() {
        mask = new int[input_depth][input_width][input_height];
    }

    private void updateMask(int depth, int[] oldMax, int[] newMax){
        mask[depth][oldMax[0]][oldMax[1]] = 0;
        mask[depth][newMax[0]][newMax[1]] = 1;
    }

    private void updateMask(int depth, int[] newMax){
        mask[depth][newMax[0]][newMax[1]] = 1;
    }

    @Override
    public void feedForward() {
        for(int d = 0; d<input_depth;d++){
            for(int input_x = 0; input_x<input_width;input_x+=stride){
                for(int input_y = 0; input_y<input_height;input_y+=stride){
                    double maxVal = getPrevLayer().outputs[d][input_x][input_y];
                    int[] maxIndex = {input_x,input_y};
                    updateMask(d,maxIndex);
                    for(int x = input_x; x<input_x+kernel_size[0];x++){
                        for(int y = input_y; y<input_y+kernel_size[1];y++){

                            if(maxVal<getPrevLayer().outputs[d][x][y]){
                                updateMask(d,maxIndex,new int[]{x,y});
                                maxVal = getPrevLayer().outputs[d][x][y];
                                maxIndex = new int[]{x,y};
                            }
                        }
                    }
                    outputs[d][input_x/stride][input_y/stride] = maxVal;
                }
            }
        }
    }

    @Override
    public void backprop() {

        for(int d = 0; d<input_depth;d++){
            for(int x = 0; x<input_width;x++){
                for(int y = 0; y<input_height;y++){
                    if(mask[d][x][y] == 1){
                        getPrevLayer().errors[d][x][y] = errors[d][x/stride][y/stride];
                    }else getPrevLayer().errors[d][x][y] = 0;
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

    public static void main(String[] args) {
        NetworkBuilder b = new NetworkBuilder(1,6,6);
        b.addLayer(new MaxPool(3,3,3));
        Network n = b.createNetwork();

        double[][][] in = {
                {
                        {1, 2, 1, 1, 1, 1},
                        {1, 1, 5, 1, 4, 1},
                        {1, 1, 1, 1, 1, 1},
                        {1, 7, 1, 1, 6, 1},
                        {6, 1, 4, 1, 1, 1},
                        {1, 2, 1, 1, 6, 1}
                }
        };

        double[][][] out = {
                {
                        {1,1},
                        {1,1}
                }
        };

        NetworkTools.printArray(n.predict(in));
//        n.learn(out,1);
        NetworkTools.printArray(n.getInputLayer().errors);


    }
}
