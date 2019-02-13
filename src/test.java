import com.jAI.ANN;
import com.jAI.Activations;
import com.jAI.Matrix;

public class test {
    public static void main(String[] args) {
       ANN brain = new ANN(3,new int[]{2,3,3,5},2,0.1,Activations::sigmoid);
       brain.feedforward(new double[]{3,4,2}).print();
    }
}
