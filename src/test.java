import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.util.MNISTReader;
import com.jAI.util.Matrix;
import com.jAI.util.TrainingSet;

import java.io.File;

public class test {

    public static void xor(){
        double[][] data ={
                {1,1},
                {0,0},
                {1,0},
                {0,1}
        };

        double[][] outs = {
                {0},
                {0},
                {1},
                {1}
        };


        ANN brain = new ANN(2,new int[]{5},1,0.01,Activations::sigmoid,Activations::sigmoidPrime);

        brain.SGD(data,outs,1,150000,true);
        brain.test_network(data,outs,100);
    }

    public static void mnistTest() {
        File imgFile = new File("/home/itay/Desktop/HandwrittenDigits/train-images.idx3-ubyte");
        File labelFile = new File("/home/itay/Desktop/HandwrittenDigits/train-labels.idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile, true);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        ANN brain = new ANN(img[0].length, new int[]{14}, outs[0].length, 0.01, Activations::sigmoid, Activations::sigmoidPrime);

        brain.SGD(img, outs, 1, 30, true);

        brain.test_network(img, outs, 1000);

        brain.saveNetwork("/home/itay/Desktop/", "MNIST");
    }

    public static void main(String[] args){
       mnistTest();
    }
}
