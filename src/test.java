import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.TrainingSet;

public class test {
    public static void main(String[] args) {
       double[][] data = {
               {1,1},
               {1,0},
               {0,1},
               {0,0}
       };

       double[][] out = {{0},{1},{1},{0}};

       ANN brain = new ANN(2,new int[]{5},1,0.1,Activations::sigmoid,Activations::sigmoidPrime);

       for(int i = 0; i<20000;i++)
       brain.SGD(data,out,2,false);

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
}
