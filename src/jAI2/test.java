package jAI2;

import com.jAI.util.MNISTReader;
import com.jAI.util.TrainingSet;
import jAI2.functions.activations.LeakyReLU;
import jAI2.functions.activations.ReLU;
import jAI2.functions.activations.Sigmoid;
import jAI2.functions.errors.MSE;
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

        LeakyReLU r = new LeakyReLU(0.3);
        System.out.println(r.activationPrime(-1));
        System.out.println(r.activationPrime(100));
    }
}
