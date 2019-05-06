package tests;

import com.jAI.util.MNISTReader;
import com.jAI.util.TrainingSet;
import jAI2.Network;
import jAI2.functions.activations.LeakyReLU;
import jAI2.functions.activations.ReLU;
import jAI2.functions.activations.Sigmoid;
import jAI2.functions.errors.MSE;
import jAI2.layers.ConvLayer;
import jAI2.layers.DenseLayer;
import jAI2.layers.InputLayer;
import jAI2.layers.OutputLayer;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;
import jAI2.util.TrainingSet3D;

import java.io.File;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        File imgFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-images.idx3-ubyte");
        File labelFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-labels.idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile, true);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        NetworkBuilder b = new NetworkBuilder(1,3,3);
        b.addLayer(new ConvLayer(1,1,true,3,3)
                .setBiasRange(0,0)
                .setKernelRange(0.5,0.5));

        Network n = b.createNetwork();

        double[][][] in = {
                {{1,1,1},{1,1,1},{1,1,1}}
        };

        double[][][] out = {
                {{1,1,1},{1,1,1},{1,1,1}}
        };

        for(int i = 0 ; i<10;i++) {
            NetworkTools.printArray(n.predict(in));
            n.learn(out, 0.1);
        }


    }
}
