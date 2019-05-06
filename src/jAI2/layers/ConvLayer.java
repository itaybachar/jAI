package jAI2.layers;

import jAI2.Network;
import jAI2.functions.activations.ActivationFunction;
import jAI2.functions.activations.ReLU;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

import java.util.Arrays;

public class ConvLayer extends Layer {

    private double[][][][] kernels;
    private double[] bias;
    private int stride;
    private int[] padding_size;
    private ActivationFunction activationFunction;
    private int[] kernel_dim;
    private double lowerBiasRange = -1, higherBiasRange = 1;
    private double lowerKernelRange = -1,higherKernelRange =1;

    /*
    Padding Formula(to keep same dimensions):
    P = (S(IN_SIZE-1)-IN_SIZE+F)/2

    Outputs Formula:
    O = (IN_SIZE - F + 2P)/S +1
     */

    public ConvLayer(int kernel_num, int stride, boolean zero_padding, int... kernel_dim) {
        if (kernel_dim.length != 2)
            throw new RuntimeException("Bad Kernel Dimensions! (width,height)");

        if (zero_padding && kernel_dim[0] % 2 == 0 && kernel_dim[1] % 2 == 0)
            throw new RuntimeException("Kernel size must be odd!");

        if (zero_padding)
            this.padding_size = new int[2];

        this.kernel_dim = kernel_dim;
        this.kernels = new double[kernel_num][][][];
        this.stride = stride;
        this.bias = new double[kernel_num];
    }

    public void initArrays(){
        for(int i = 0; i<kernels.length;i++)
            kernels[i] = new double[input_depth][kernel_dim[0]][kernel_dim[1]];

        if (padding_size != null) {
            //Set padding dimensions to retain output size
            padding_size[0] = (stride * (input_width - 1) - input_width + kernels[0][0].length) / 2;
            padding_size[1] = (stride * (input_height - 1) - input_height + kernels[0][0][0].length) / 2;

            this.output_height = input_height;
            this.output_width = input_width;

        } else if (isValidLayer()) {
            padding_size = new int[]{0, 0};
        } else throw new RuntimeException("Bad hyper-parameters: (IN_SIZE - F + 2P)/S +1 != INT");

        this.output_depth = kernels.length;

        super.initArrays();
    }

    @Override
    public void build() {
        //Initialize Kernels and bias
        kernels = NetworkTools.createRandomArray(kernels.length, kernels[0].length, kernels[0][0].length,kernels[0][0][0].length, lowerKernelRange, higherKernelRange);
        bias = NetworkTools.createRandomArray(bias.length, lowerBiasRange, higherBiasRange);

        if (activationFunction == null)
            activationFunction = new ReLU();
    }

    private boolean isValidLayer(){
        //Assumes no zero padding
        output_width = (this.input_width-kernels[0][0].length)/stride +1;
        double actual_w = (this.input_width-kernels[0][0].length)/(double)stride +1;

        output_height = (this.input_height-kernels[0][0][0].length)/stride +1;
        double actual_h = (this.input_height-kernels[0][0][0].length)/(double)stride +1;

        return output_width == actual_w && output_height == actual_h;
    }

    @Override
    public void feedForward() {
    //The output is the sum of the convolution of the channels. loop through the possible convolutions and add them up with a bias for that kernel
        for(int kernel = 0; kernel<kernels.length;kernel++){
            for(int depth = 0; depth<input_depth;depth++){
                for(int input_x = 0; input_x + kernel_dim[0]<=input_width+(2*padding_size[0]);input_x+=stride){
                    for(int input_y = 0; input_y+kernel_dim[1]<=input_height+(2*padding_size[1]);input_y+=stride){
                        outputs[kernel][input_x][input_y] = convolve(kernel,depth,input_x,input_y);
                    }
                }
            }
        }
        activationFunction.apply(outputs,output_derivatives);
    }

    private double convolve(int kernel_num,int kernel_depth,int input_x,int input_y) {
        double sum = bias[kernel_num];
        for (int x = input_x; x < input_x + kernels[0][0].length; x++) {
            for (int y = input_y; y < input_y + kernels[0][0][0].length; y++) {
                if (x >= padding_size[0] && x <= input_width + padding_size[0] / 2 &&
                        y >= padding_size[1] && y <= input_height + padding_size[1] / 2) {
                    int k_x = x % kernels[0][0].length;
                    int k_y = y % kernels[0][0][0].length;
                    sum += kernels[kernel_num][kernel_depth][k_x][k_y] * getPrevLayer().outputs[kernel_depth][x - padding_size[0]][y - padding_size[1]];

                }
            }
        }
        return sum;
    }

    private double[][][] rotateKernel(double[][][] kernelIn) {
        double[][][] rotated = new double[kernelIn.length][kernelIn[0].length][kernelIn[0][0].length];

        //Do per depth layer
        for (int d = 0; d < kernelIn.length; d++) {
            for (int x = 0; x < kernelIn[0].length; x++) {
                for (int y = 0; y < kernelIn[0][0].length; y++)
                    rotated[d][x][y] = kernelIn[d][kernelIn[0].length-y-1][x];
            }
        }

        return rotated;
    }

    @Override
    public void backprop() {

        for(int x = 0; x<kernel_dim[0]+errors[0].length -1;x++){
            for(int y = 0; y<kernel_dim[1]+errors[0][0].length-1;y++){
                int error_x = x%errors[0].length-1;
                int error_y = y%errors[0][0].length-1;


            }
        }

        for(int input_x = 0 ;input_x<input_width;input_x++){
            for(int input_y = 0;input_y<input_height;input_y++){

            }
        }


    }

    @Override
    public void updateWeights(double learning_rate) {
        for (int kernel_error = 0; kernel_error < output_depth; kernel_error++) {
            for (int depth = 0; depth < input_depth; depth++) {
                for (int input_x = 0; input_x + kernel_dim[0] <= input_width + (2 * padding_size[0]); input_x += stride) {
                    for (int input_y = 0; input_y + kernel_dim[1] <= input_height + (2 * padding_size[1]); input_y += stride) {
                        double error_sum = 0;
                        for (int x = input_x; x < input_x + kernel_dim[0]; x++) {
                            for (int y = input_y; y < input_y + kernel_dim[1]; y++) {
                                if (x >= padding_size[0] && x <= input_width + padding_size[0] / 2 &&
                                        y >= padding_size[1] && y <= input_height + padding_size[1] / 2) {
                                    error_sum += errors[kernel_error][input_x][input_y] * getPrevLayer().outputs[depth][x - padding_size[0]][y - padding_size[1]];
                                }
                            }
                        }
                        kernels[kernel_error][depth][input_x][input_y] += -learning_rate*error_sum;
                    }
                }
            }
        }
    }

    @Override
    public void printWeights() {

    }

    public ConvLayer setActivationFunction(ActivationFunction function){
        this.activationFunction = function;
        return this;
    }


    public ConvLayer setKernelRange(double lower,double higher){
        this.lowerKernelRange = lower;
        this.higherKernelRange = higher;

        return this;
    }

    public ConvLayer setBiasRange(double lower,double higher){
        this.lowerBiasRange = lower;
        this.higherBiasRange = higher;

        return this;
    }

    public static void main(String[] args) {
        double[][][] arr = {{
                {0,1,2},
                {3,4,5},
                {6,7,8}
        }};
        NetworkTools.printArray(arr);
    }
}
