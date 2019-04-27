package jAI2;

import jAI2.functions.activations.Sigmoid;
import jAI2.functions.errors.MSE;
import jAI2.layers.DenseLayer;
import jAI2.layers.InputLayer;
import jAI2.layers.OutputLayer;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        NetworkBuilder networkBuilder = new NetworkBuilder(1,1,2);
        networkBuilder.addLayer(new DenseLayer(1)
                .setActivationFunction(new Sigmoid()).setBiasRange(-1,1));
        Network net = networkBuilder.createNetwork();


    }
}
