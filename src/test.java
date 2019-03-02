import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.util.MNISTReader;
import com.jAI.util.Matrix;
import com.jAI.util.TrainingSet;

import java.io.File;
import java.util.List;

public class test {

    public static void annTest() {
        double[][] data = {
                {1, 1},
                {1, 0},
                {0, 1},
                {0, 0}
        };

        double[][] out = {{0}, {1}, {1}, {0}};

        ANN brain = new ANN(2, new int[]{5}, 1, 0.1, Activations::sigmoid, Activations::sigmoidPrime);

        brain.learn_SGD(data,out,10);

        brain.test(data,out,100);
        brain.saveNetwork("/home/itay/Desktop/","XOR");
    }

    public static void mnistTest() {
        File imgFile = new File("/home/itay/Desktop/HandwrittenDigits/train-images.idx3-ubyte");
        File labelFile = new File("/home/itay/Desktop/HandwrittenDigits/train-labels.idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile,true);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        ANN brain = new ANN(img[0].length, new int[]{14}, 10, 0.1, Activations::sigmoid, Activations::sigmoidPrime);

        brain.learn_SGD(img,outs,30);

        brain.test(img,outs,1000);

        brain.saveNetwork("/home/itay/Desktop/","MNIST");
    }

    public static void main(String[] args){
        mnistTest();
//        File imgFile = new File("/home/itay/Desktop/HandwrittenDigits/train-images.idx3-ubyte");
//        File labelFile = new File("/home/itay/Desktop/HandwrittenDigits/train-labels.idx1-ubyte");
//        double[][] img = MNISTReader.loadImages(imgFile, true);
//        double[] label = MNISTReader.loadLabels(labelFile);
//        double[][] outs = MNISTReader.labelToNNOutputs(label);
//
//        ANN net = ANN.loadNetwork(new File("/home/itay/Desktop/MNIST.ann"));
////        net.setActivations(Activations::tanh,Activations::tanhPrime);
//        net.feedForward(img[0]).print();
//        net.feedForward(img[10]).print();
//
//        net.test(img,outs,100);
    }
}
