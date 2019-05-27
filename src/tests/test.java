package tests;

import com.jAI.util.MNISTReader;
import com.jAI.util.Matrix;
import jAI2.Network;
import jAI2.functions.activations.ActivationFunction;
import jAI2.functions.activations.ReLU;
import jAI2.functions.activations.Sigmoid;
import jAI2.layers.ConvLayer;
import jAI2.layers.DenseLayer;
import jAI2.layers.MaxPool;
import jAI2.layers.TransformationLayer;
import jAI2.util.NetworkBuilder;
import jAI2.util.NetworkTools;

import java.io.File;


public class test {

    public static void test1(){
        double[][][] input = {{ //width: 4, height: 6
                {1,0,0},
                {1,0,0},
                {1,0,0}
        }
        };


        double[][][] input2 = {{ //width: 4, height: 6
                {0,0,1},
                {0,1,0},
                {1,0,0}
        }
        };

        double[][][] exp_out = {{
                {1}
        }
        };

        double[][][] exp_out2 = {{
                {0}
        }
        };

        NetworkBuilder b = new NetworkBuilder(1,3,3);
        b.addLayer(new ConvLayer(5,1,false,2,2));
        b.addLayer(new TransformationLayer());
        b.addLayer(new DenseLayer(10));
        b.addLayer(new DenseLayer(5));
        b.addLayer(new DenseLayer(1));

        Network n = b.createNetwork();
        for(int i = 0; i<100000;i++) {
            n.learn(input, exp_out, 0.001);
            n.learn(input2, exp_out2, 0.001);
        }
        NetworkTools.printArray(n.predict(input2));
        NetworkTools.printArray(n.predict(input));

    }

    public static void mnist(){
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
            n.learn(NetworkTools.to3DArray(img[0]),NetworkTools.to3DArray(outs[0]),0.01);
            n.learn(NetworkTools.to3DArray(img[1]),NetworkTools.to3DArray(outs[1]),0.01);
        }
        System.out.println(label[0]);
        NetworkTools.printArray(n.predict(new double[][][]{img[0]}));
        System.out.println(label[1]);
        NetworkTools.printArray(n.predict(new double[][][]{img[1]}));
    }

    public static void main(String[] args) {
      test1();

    }

    public static void test3(){
        double[][][] filter = {{{1,1,1},{2,1,0},{-1,1,1}}};
        double[][][] in = {{{1,2,-3,2,0},{2,1,-3,1,-3},{2,2,1,-1,1},{-3,2,1,0,-1},{4,3,-1,1,0}}};
        double[][][] exp_out = {{{0.3}}};

        NetworkBuilder b=  new NetworkBuilder(1,5,5);
        b.addLayer(new ConvLayer(1,1,false,3,3)
                    .setKernel(0,filter,0).setActivationFunction(new ReLU()));
        b.addLayer(new ConvLayer(1,1,false,3,3)
                .setKernel(0,filter,0).setActivationFunction(new ReLU()));
        b.addLayer(new DenseLayer(1).setBiasRange(0,0).setWeightRange(0.1,0.1));
        Network n  = b.createNetwork();

//        n.predict(in);
        n.learn(in,exp_out,1);

        System.out.println("Feedforward");
        System.out.println(n.getInputLayer().toString());
        System.out.println(n.getInputLayer().getNextLayer().toString());
        System.out.println(n.getInputLayer().getNextLayer().getNextLayer().toString());
        System.out.println(n.getOutputLayer().getPrevLayer().toString());
        n.getInputLayer().getNextLayer().printWeights();
        n.getInputLayer().getNextLayer().getNextLayer().getNextLayer().printWeights();

    }
}