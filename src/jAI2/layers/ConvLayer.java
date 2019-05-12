package jAI2.layers;

import jAI2.Network;
import jAI2.functions.activations.ActivationFunction;
import jAI2.functions.activations.ReLU;
import jAI2.functions.activations.Sigmoid;
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
        for (int kernel = 0; kernel < kernels.length; kernel++) {
            for (int i = 0; i < output_width; i++) {
                for (int j = 0; j < output_height; j++) {
                    outputs[kernel][i][j] += bias[kernel];
                }
            }

            for (int depth = 0; depth < input_depth; depth++) {
                double[][] out = convolve(getPrevLayer().outputs[depth], kernels[kernel][depth], stride, padding_size);

                for (int i = 0; i < output_width; i++) {
                    for (int j = 0; j < output_height; j++) {
                        outputs[kernel][i][j] += out[i][j];
                        output_derivatives[kernel][i][j] = outputs[kernel][i][j];
                    }
                }
            }
        }
        activationFunction.apply(outputs, output_derivatives);
    }

    private double[][] convolve(double[][] in, double[][] fil, int stride, int[] padding) {
        double[][] out = new double[output_width][output_height];

        for (int in_x = 0; in_x +fil.length <= in.length + (padding[0] * 2); in_x += stride) {
            for (int in_y = 0; in_y + fil[0].length <= in[0].length + (padding[1] * 2); in_y += stride) {
                double sum = 0;
                for (int x = in_x; x < in_x + fil.length; x++) {
                    for (int y = in_y; y < in_y + fil[0].length; y++) {

                        if (x >= padding[0] && x < in.length + padding[0] &&
                                y>= padding[1] && y<in.length+padding[1] ) {
                            sum += in[x - padding[0]][y-padding[1]] * fil[(x+in_x) % (fil.length)][(y+in_y)%(fil[0].length)];
                        }

                    }
                }

                out[in_x / stride][in_y / stride] = sum;
            }
        }

        return out;
    }

    @Override
    public void backprop() {
        for(int kernel = 0; kernel <output_depth;kernel++){
            for(int depth = 0; depth<input_depth;depth++){
                getPrevLayer().errors[depth] = getInputError(errors[kernel],kernels[kernel][depth],stride,padding_size,depth);
            }
        }
    }

    private double[][] getInputError(double[][] err, double[][] fil, int stride, int[] padding,int depth) {
        double[][] in_error = new double[input_width][input_height];

        for(int error_x = 0; error_x < err.length; error_x++){
            for(int error_y = 0; error_y<err[0].length; error_y++){
                for(int filter_x = 0; filter_x <fil.length;filter_x++){
                    for(int filter_y = 0; filter_y<fil[0].length;filter_y++) {
                        int index_x = stride * error_x - 2 * padding[0] + filter_x;
                        int index_y = stride * error_y - 2 * padding[1] + filter_y;

                        if(padding[0]>0){
                            index_x++;
                        }
                        if(padding[1]>0){
                            index_y++;
                        }

                        if(index_x> -1 && index_x<in_error.length &&
                                index_y>-1 && index_y< in_error[0].length) {

                            in_error[index_x][index_y] +=err[error_x][error_y]*fil[filter_x][filter_y]*getPrevLayer().getOutput_derivatives()[depth][index_x][index_y];
                        }
                    }
                }
            }
        }

        return in_error;
    }


    @Override
    public void updateWeights(double learning_rate) {
        for (int kernel = 0; kernel < output_depth; kernel++) {
            //update bias
            double error_sum = 0;
            for(int x = 0;x<output_width;x++){
                for(int y= 0; y<output_height;y++){
                    error_sum+=errors[kernel][x][y];
                }
            }
            bias[kernel] += -learning_rate*error_sum;

            for (int depth = 0; depth < input_depth; depth++) {
                double[][]kernelError = getKernelErrors(errors[kernel],getPrevLayer().outputs[depth],stride,padding_size);

                for(int i = 0; i<kernel_dim[0];i++){
                    for(int j=0;j<kernel_dim[1];j++){
                        kernels[kernel][depth][i][j] += -learning_rate*kernelError[i][j];
                    }
                }
            }
        }
    }

    private double[][] getKernelErrors(double[][] err,double[][] in,int stride,int[] padding) {
        double[][] kernel_error = new double[-stride * (err.length - 1) + in.length + 2 * padding[0]][-stride * (err[0].length - 1) + in[0].length + 2 * padding[1]];

        for (int error_x = 0; error_x < err.length; error_x++) {
            for (int error_y = 0; error_y < err[0].length; error_y++) {
                for (int input_x = 0; input_x < in.length; input_x++) {
                    for (int input_y = 0; input_y < in[0].length; input_y++) {
                        int index_x = -stride * error_x + input_x + 2 * padding[0] - 1;
                        int index_y = -stride * error_y + input_y + 2 * padding[1] - 1;

                        if (padding[0] == 0)
                            index_x++;

                        if (padding[1] == 0)
                            index_y++;

                        if (index_x > -1 && index_x < kernel_error.length &&
                                index_y > -1 && index_y < kernel_error[0].length) {
                            kernel_error[index_x][index_y] += err[error_x][error_y] * in[input_x][input_y];
                        }

                    }
                }
            }
        }

        return kernel_error;
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
        double[][][] arr = {
                {
                        {1, 1, 1},
                        {1, 0, 1},
                        {1, 1, 1}
                },
                {
                        {0, 0, 0},
                        {1, 1, 1},
                        {0, 0, 0}
                },
                {
                        {0, 0, 0},
                        {0, 1, 0},
                        {0, 0, 0}
                },
                {
                        {0, 0, 1},
                        {0, 1, 0},
                        {1, 0, 0}
                },
                {
                        {1, 0, 0},
                        {0.5, 1, 0},
                        {0, 1, 0}
                }
        };

        double[][] outs = {
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1}
        };

        NetworkBuilder b= new NetworkBuilder(1,3,3);
        b.addLayer(new ConvLayer(4,1,false,2,2)
                .setActivationFunction(new ReLU()).setKernelRange(-1,1).setBiasRange(-1,1));
        b.addLayer(new TransformationLayer());
        b.addLayer(new DenseLayer(5).setActivationFunction(new Sigmoid()).setBiasRange(-1,1).setWeightRange(-1,1));

        Network n = b.createNetwork();

        for(int i = 0;i<10000;i++) {
            n.predict(new double[][][]{arr[i%outs.length]});
            n.learn(new double[][][]{{outs[i%outs.length]}},0.1);
        }

        for(int i =0;i<outs.length;i++){
            NetworkTools.printArray( n.predict(new double[][][]{arr[i]}));
        }

    }
}
