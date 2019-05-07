package tests;

import jAI2.util.NetworkTools;

import java.util.ArrayList;
import java.util.Arrays;

public class ConvTests {

    public static double[][] convolve(double[][] in, double[][] fil, int stride, int padding) {
        int outWidth = (in.length - fil.length + 2 * padding) / stride + 1;
        int outHeight = (in[0].length - fil[0].length + 2*padding)/stride +1;
        double[][] out = new double[outWidth][outHeight];


        for (int in_x = 0; in_x +fil.length <= in.length + (padding * 2); in_x += stride) {
            for (int in_y = 0; in_y + fil[0].length <= in[0].length + (padding * 2); in_y += stride) {
                double sum = 0;
                for (int x = in_x; x < in_x + fil.length; x++) {
                    for (int y = in_y; y < in_y + fil[0].length; y++) {

                        if (x >= padding && x < in.length + padding &&
                            y>= padding && y<in.length+padding ) {
                            sum += in[x - padding][y-padding] * fil[(x+in_x) % (fil.length)][(y+in_y)%(fil[0].length)];
                        }

                    }
                }

                out[in_x / stride][in_y / stride] = sum;
            }
        }

        return out;
    }

    public static double[] convolve(double[] in, double[] fil, int stride, int padding) {
        int outSize = (in.length - fil.length + 2 * padding) / stride + 1;
        double[] out = new double[outSize];

        for (int in_x = 0; in_x + fil.length <= in.length + (padding * 2) ; in_x += stride) {
            double sum = 0;
            for (int x = in_x; x < in_x + fil.length; x++) {
                if (x >= padding && x < in.length + padding) {
                    sum += in[x - padding] * fil[x % fil.length];
                }
            }
            out[in_x/stride] = sum;
        }

        return out;
    }

    public static double[] getError(double[] out, double[] exp) {
        double[] err = new double[out.length];
        for (int i = 0; i < out.length; i++)
            err[i] = out[i] - exp[i];
        return err;
    }

    public static double[][] getError(double[][] out, double[][] exp) {
        double[][] err = new double[out.length][out[0].length];
        for (int i = 0; i < out.length; i++)
            for(int j = 0;j<out[0].length;j++)
            err[i][j] = out[i][j] - exp[i][j];
        return err;
    }


    public static double[][] getInputError(double[][] err, double[][] fil, int stride, int[] padding) {
        double[][] in_error = new double[stride * (err.length - 1) - (2 * padding[0]) + fil.length][stride * (err[0].length - 1) - (2 * padding[1]) + fil[0].length];

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

                            in_error[index_x][index_y] +=err[error_x][error_y]*fil[filter_x][filter_y];
                        }
                    }
                }
            }
        }

        return in_error;
    }

    public static double[] getInputError(double[] err, double[] fil, int stride, int padding) {
        double[] in_error = new double[stride * (err.length - 1) - (2 * padding) + fil.length];

        for (int errorIndex = 0; errorIndex < err.length; errorIndex++) {
            for (int filterIndex = 0; filterIndex < fil.length; filterIndex++) {
                int index = stride * (errorIndex) - 2 * padding + filterIndex;
                if (padding > 0)
                    index++;
                if (index > -1 && index < in_error.length) {
                    in_error[index] += err[errorIndex] * fil[filterIndex];
                }
            }
        }

        return in_error;
    }

    public static double[][] getKernelErrors(double[][] err,double[][] in,int stride,int[] padding) {
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

    public static double[] getFilterError(double[] err, double[] in, int stride, int padding) {
        double[] fil_error = new double[-stride * (err.length - 1) + in.length + 2 * padding];

        for (int errorIndex = 0; errorIndex < err.length; errorIndex++) {
            for (int inputIndex = 0; inputIndex < in.length; inputIndex++) {
                int index = -stride * (errorIndex) + inputIndex + 2 * padding - 1;

                if (padding == 0)
                    index++;

                if (index > -1 && index < fil_error.length) {
                    fil_error[index] += err[errorIndex] * in[inputIndex];
                }
            }
        }
        return fil_error;
    }


    public static void main(String[] args) {

        double[][] in2 = {
                {1,5,2},
                {2,2,3},
                {6,2,3}

        };
        double[][] fil2 = NetworkTools.createRandomArray(2,2,-1,1);

        double[][] exp ={
                {4,2},
                {1,0}
        };

        int stride = 1;
        int pad = 0;

//        for (int k = 0; k < 10000; k++) {
//            double[][] out = convolve(in2, fil2, stride, pad);
//            double[][] error = getError(out, exp);
//            double[][] inError = getInputError(error, fil2, stride, new int[]{pad,pad});
//            NetworkTools.printArray(convolve(in2, fil2, stride, pad));
//
//
//
//            for (int i = 0; i < in2.length; i++) {
//                for(int j=0;j<in2[0].length;j++)
//                in2[i][j] += -0.01 * inError[i][j];
//            }
//        }

        for (int k = 0; k < 1000; k++) {
            double[][] out = convolve(in2, fil2, stride, pad);
            double[][] error = getError(out, exp);
            double[][] filError = getKernelErrors(error, in2, stride, new int[]{pad,pad});
            NetworkTools.printArray(convolve(in2, fil2, stride, pad));
//            NetworkTools.printArray(fil2);
//            NetworkTools.printArray(filError);

            for (int i = 0; i < fil2.length; i++) {
                for(int j=0;j<fil2[0].length;j++)
                    fil2[i][j] += -0.01 * filError[i][j];
            }
        }


//        int pad = 1, stride = 2;
//        double[] in = {1, 2, 3, 4, 5, 6};
//        double[] fil = NetworkTools.createRandomArray(2,-1,1);
//        double[] exp = {1, 0, 0, 1};

        if (true) {
//        Minimize error changing filters
//            for (int k = 0; k < 10000; k++) {
//                double[] out = convolve(in, fil, stride, pad);
//                double[] errors = getError(out, exp);
//                double[] filError = getFilterError(errors, in, stride, pad);
//                System.out.println(Arrays.toString(convolve(in, fil, stride, pad)));
////                System.out.println(Arrays.toString(fil));
//                for (int i = 0; i < fil.length; i++) {
//                    fil[i] += -0.01 * filError[i];
//                }
//            }
//
//            //Minimize error changing inputs
//
//            for (int k = 0; k < 1000; k++) {
//                double[] out = convolve(in, fil, stride, pad);
//                double[] error = getError(out, exp);
//                double[] inError = getInputError(error, fil, stride, pad);
//                System.out.println(Arrays.toString(convolve(in, fil, stride, pad)));
//
//
//                for (int i = 0; i < in.length; i++) {
//                    in[i] += -0.01 * inError[i];
//                }
//            }
        }


    }
}
