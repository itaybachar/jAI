package jAI2;

import com.jAI.util.MNISTReader;
import com.jAI.util.TrainingSet;
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

        TrainingSet3D set = new TrainingSet3D(NetworkTools.to3DArray(img),NetworkTools.to3DArray(outs));

        NetworkBuilder networkBuilder = new NetworkBuilder(1,1,784);
        networkBuilder.addLayer(new DenseLayer(75)
                .setActivationFunction(new Sigmoid()).setWeightRange(-1,1));
        networkBuilder.addLayer(new DenseLayer(10)
                .setActivationFunction(new Sigmoid()).setWeightRange(-1,1));

        Network net = networkBuilder.createNetwork();

        net.train(set,200,100,100,0.1);
       NetworkTools.printArray(net.predict(set.get(0).input));
        System.out.println(set.get(0));
    }
}
