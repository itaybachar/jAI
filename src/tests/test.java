package tests;

import com.jAI.util.MNISTReader;
import jAI2.Network;
import jAI2.layers.ConvLayer;
import jAI2.layers.DenseLayer;
import jAI2.layers.MaxPool;
import jAI2.layers.TransformationLayer;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

import java.io.File;


public class test {
    public static void main(String[] args) {
        File imgFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-images.idx3-ubyte");
        File labelFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-labels.idx1-ubyte");
        double[][][] img = MNISTReader.loadImages(imgFile, true,false);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        NetworkBuilder b = new NetworkBuilder(1,img[0].length,img[0][0].length);
        b.addLayer(new ConvLayer(20,1,false,3,3).setBiasRange(-2,2).setKernelRange(-2,2));
        b.addLayer(new MaxPool(2,2,2));
        b.addLayer(new ConvLayer(20,1,false,3,3).setBiasRange(-2,2).setKernelRange(-2,2));
        b.addLayer(new TransformationLayer());
        b.addLayer(new DenseLayer(outs[0].length));

        Network n = b.createNetwork();

        for(int i = 0; i<100;i++){
            System.out.println("NUmber: " + i);
            n.predict(new double[][][]{img[i%img.length]});
            n.learn(new double[][][]{{outs[i%img.length]}},0.01);
        }
        System.out.println(label[0]);
        NetworkTools.printArray(n.predict(new double[][][]{img[0]}));
        System.out.println(label[1]);
        NetworkTools.printArray(n.predict(new double[][][]{img[1]}));


    }
}
