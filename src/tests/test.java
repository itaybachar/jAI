package tests;

import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.util.MNISTReader;
import com.jAI.util.Matrix;
import com.jAI.util.Pair;
import com.jAI.util.TrainingSet;

import java.io.File;
import java.util.Arrays;

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

        brain.SGD(data,outs,1,150000);
        brain.test_network(data,outs,100);
    }


    public static void mnistTest() {
        File imgFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-images.idx3-ubyte");
        File labelFile = new File("C:\\Users\\itayb\\Desktop\\MNIST\\train-labels.idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile, true);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        ANN ann = new ANN(img[0].length, new int[]{110}, outs[0].length, 0.1, Activations::sigmoid, Activations::sigmoidPrime);


        TrainingSet t =new TrainingSet(img,outs);
        TrainingSet.Data[][] d = ann.createMiniBatches(t,5000);

        for(int k = 0; k<100;k++) {
            for (int j = 0; j < 5000; j++) {
                ann.feedForward(d[0][j].input);

                Pair deltas = ann.backprop(d[0][j].input, d[0][j].output);
                deltas.multiply(0.01);
                //Update Weights and Biases
                for (int i = 0; i < ann.W.length; i++) {
                    ann.W[i].subtract(deltas.dCdW[i]);
                    ann.B[i].subtract(deltas.dCdB[i]);
                }
            }
            if(k%10 == 0)
            System.out.println(ann.MSE(d[0]));
        }
        ann.test_network(img,outs,5000);

        //        brain.SGD(new double[][]{d.input},new double[][]{d.output},1,1);

//        brain.feedForward(data[0].input).print();
//        System.out.println(Arrays.toString(data[0].output));

//        brain.saveNetwork("/home/itay/Desktop/", "MNIST");
    }



    public static void fashion(){
        File imgFile = new File("/home/itay/Desktop/Fashion/train-images-idx3-ubyte");
        File labelFile = new File("/home/itay/Desktop/Fashion/train-labels-idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile, true);
        double[] label = MNISTReader.loadLabels(labelFile);
        double[][] outs = MNISTReader.labelToNNOutputs(label);

        ANN brain = new ANN(img[0].length, new int[]{20}, outs[0].length, 0.1, Activations::sigmoid, Activations::sigmoidPrime);

        brain.SGD(img, outs, 1, 10);

        brain.saveNetwork("/home/itay/Desktop/", "FASH");
    }

    public static void main(String[] args){

        mnistTest();

//       ANN ann = new ANN(4,new int[]{3},1,1,Activations::sigmoid,Activations::sigmoidPrime);
//
//       double[][] in = new double[][]{{0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},
//               {0.2,0.1,0.5,0.1},{0.2,0.1,0.5,0.1},{0.2,0.1,0.5,0.1},{0.2,0.1,0.5,0.1}};
//       double[][] out = new double[][]{{0.7},{0.7},{0.7},{0.7},{0.7},{0.7},{0.7},{0.7},{0.7},{0.7}};
//
//       ann.SGD(in,out,1,1000);
//       ann.feedForward(in[0]).print();

//       ann.feedForward(in).print();
//       Pair deltas  = ann.backprop(in,out);
//       deltas.multiply(100);
//        //Update Weights and Biases
//        for(int i= 0; i<ann.W.length;i++){
//            ann.W[i].subtract(deltas.dCdW[i]);
//            ann.B[i].subtract(deltas.dCdB[i]);
//        }

//        ann.W[0].print();


    }

}
