package jAI2.functions.activations;

public abstract class ActivationFunction {
    public void apply(double[][][] output, double[][][] output_derivative){
        for(int d = 0; d<output.length;d++){
            for(int w= 0 ; w<output[0].length;w++){
                for(int h = 0; h<output[0][0].length;h++){
                    output_derivative[d][w][h] = activationPrime(output[d][w][h]);
                    output[d][w][h] = activation(output[d][w][h]);
                }
            }
        }
    }

    public abstract double activation(double x);
    public abstract double activationPrime(double x);
}
