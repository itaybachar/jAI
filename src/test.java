import com.jAI.ANN;
import com.jAI.Activations;

public class test {
    public static void main(String[] args) {
       double[][] data = {
               {1,1},
               {1,0},
               {0,1},
               {0,0}
       };

       double[] out = {0,1,1,0};

       ANN brain = new ANN(2,new int[]{5},1,0.1,Activations::tanh,Activations::tanhPrime);

       for(int i = 0; i<10000;i++){
           int index = (int)(Math.random()*4);
           brain.feedForward(data[index]);
           brain.GD(new double[]{out[index]});
       }

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
