package tests;

import java.util.ArrayList;
import java.util.Arrays;

public class ConvTests {

    public static double[] convolve(double[] in, double[] fil, int stride, int padding) {
        int outSize = (in.length - fil.length + 2 * padding) / stride + 1;
        double[] out = new double[outSize];

        for (int in_x = 0; in_x < in.length + (padding * 2) - 1; in_x += stride) {
            double sum = 0;
            for (int x = in_x; x < in_x + fil.length; x++) {
                if (x >= padding && x < in.length + padding) {
                    sum += in[x - padding] * fil[x % fil.length];
                }
            }
            out[in_x / stride] = sum;
        }

        return out;
    }

    public static double[] getError(double[] out, double[] exp) {
        double[] err = new double[out.length];
        for (int i = 0; i < out.length; i++)
            err[i] = out[i] - exp[i];
        return err;
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
        int pad = 1, stride = 2;
        double[] in = {1, 2, 3, 4, 5, 6};
        double[] fil = {1, 1};
        double[] exp = {0, 0, 0, 0};

        if (false) {
//        Minimize error changing filters
            for (int k = 0; k < 10000; k++) {
                double[] out = convolve(in, fil, stride, pad);
                double[] errors = getError(out, exp);
                double[] filError = getFilterError(errors, in, stride, pad);
                System.out.println(Arrays.toString(convolve(in, fil, stride, pad)));
                System.out.println(Arrays.toString(fil));
                for (int i = 0; i < fil.length; i++) {
                    fil[i] += -0.01 * filError[i];
                }
            }

            //Minimize error changing inputs

            for (int k = 0; k < 1000; k++) {
                double[] out = convolve(in, fil, stride, pad);
                double[] error = getError(out, exp);
                double[] inError = getInputError(error, fil, stride, pad);
                System.out.println(Arrays.toString(convolve(in, fil, stride, pad)));


                for (int i = 0; i < in.length; i++) {
                    in[i] += -0.01 * inError[i];
                }
            }
        }


    }
}
