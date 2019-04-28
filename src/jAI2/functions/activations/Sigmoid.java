package jAI2.functions.activations;


public class Sigmoid extends ActivationFunction {
    @Override
    public void apply(double[][][] output, double[][][] output_derivative) {
        for(int d = 0; d<output.length;d++){
            for(int w= 0 ; w<output[0].length;w++){
                for(int h = 0; h<output[0][0].length;h++){
                    output_derivative[d][w][h] = activationPrime(output[d][w][h]);
                    output[d][w][h] = activation(output[d][w][h]);
                }
            }
        }
    }

    private double activation(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }

    private double activationPrime(double x) {
        return activation(x)*(1-activation(x));
    }
}
