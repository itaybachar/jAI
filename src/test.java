import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.util.MNISTReader;
import com.jAI.util.Matrix;
import com.jAI.util.TrainingSet;

import java.io.File;

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

        for (int i = 0; i < 20000; i++)
            brain.mini_batch(data, out, 1, false);

//       for(int i = 0; i<100000;i++){
//           int index = (int)(Math.random()*4);
//           brain.feedForward(data[index]);
//           brain.GD(out[index]);
//       }

        System.out.println("(1,1) =>0");
        brain.feedForward(data[0]).print();

        System.out.println("(1,0) =>1");
        brain.feedForward(data[1]).print();

        System.out.println("(0,1) =>1");
        brain.feedForward(data[2]).print();

        System.out.println("(0,0)=>0");
        brain.feedForward(data[3]).print();
    }

    public static void mnistTest() {
        File imgFile = new File("/home/itay/Desktop/HandwrittenDigits/train-images.idx3-ubyte");
        File labelFile = new File("/home/itay/Desktop/HandwrittenDigits/train-labels.idx1-ubyte");
        double[][] img = MNISTReader.loadImages(imgFile);
        double[][] label = MNISTReader.loadLabels(labelFile);

        TrainingSet t = new TrainingSet(img,label);
        ANN brain = new ANN(img[0].length, new int[]{15}, label[0].length, 0.1, Activations::sigmoid, Activations::sigmoidPrime);
        for(int j = 0;j<10;j++) {
//            t.shuffle();
//            for (int i = 0; i < t.getSize(); i++) {
//                TrainingSet.Data data = t.data.get(i);
//                brain.feedForward(data.input);
//                brain.SGD(data.output);
//            }
            brain.mini_batch(img,label,20,false);
        }

        TrainingSet.Data t2 = t.pickRandom();

        System.out.println("test");
        brain.feedForward(t2.input).print();

        System.out.println("desire");
        Matrix.fromArray(new double[][]{t2.output}).print();

    }

    public static void main(String[] args) {
        mnistTest();
    }
}
