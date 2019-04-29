package jAI2.layers;

import jAI2.functions.activations.ActivationFunction;
import jAI2.functions.activations.ReLU;
import jAI2.util.NetworkTools;

public class ConvLayer extends Layer {

    private double[][][][] kernels;
    private double[] bias;
    private int stride;
    private int[] padding_size;
    private ActivationFunction activationFunction;

    private double lowerBiasRange = -1, higherBiasRange = 1;
    private double lowerKernelRange = -1,higherKernelRange =1;

    /*
    Padding Formula(to keep same dimensions):
    P = (S(IN_SIZE-1)-IN_SIZE+F)/2

    Outputs Formula:
    O = (IN_SIZE - F + 2P)/S +1
     */

    public ConvLayer(int kernel_num,int kernel_depth, int stride, boolean zero_padding, int... kernel_dim) {
        if (kernel_dim.length == 2) {
            if (zero_padding)
                this.padding_size = new int[2];

            this.kernels = new double[kernel_num][kernel_depth][kernel_dim[0]][kernel_dim[1]];
            this.stride = stride;
            this.bias = new double[kernel_num];
        } else throw new RuntimeException("Bad Kernel Dimensions! (width,height)");
    }

    public void initArrays(){
        if (padding_size != null) {
            //Set padding dimensions to retain output size
            padding_size[0] = (stride * (input_width - 1) - input_width + kernels[0].length) / 2;
            padding_size[1] = (stride * (input_height - 1) - input_height + kernels[0][0].length) / 2;

            this.output_height = this.input_height;
            this.output_width = this.input_width;
            this.output_depth = kernels.length;

        } else if (isValidLayer()) {
            padding_size = new int[]{0, 0};
        } else throw new RuntimeException("Bad hyper-parameters: (IN_SIZE - F + 2P)/S +1 != INT");
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
        int output_width = (this.input_width-kernels[0][0].length)/stride;
        double actual_w = (this.input_width-kernels[0][0].length)/(double)stride;

        int output_height = (this.input_width-kernels[0][0][0].length)/stride;
        double actual_h = (this.input_width-kernels[0][0][0].length)/(double)stride;

        return output_width == actual_w && output_height == actual_h;
    }

    @Override
    public void feedForward() {
    //The output is the sum of the convolution of the channels. loop through the possible convolutions and add them up with a bias for that kernel
    }

    private void convolve(int kernel_num,int kernel_depth,int input_x,int input_y){

    }

    @Override
    public void backprop() {

    }

    @Override
    public void updateWeights(double learning_rate) {

    }

    @Override
    public void printWeights() {

    }
}
