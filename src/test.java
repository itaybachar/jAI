import com.jAI.Activations;
import com.jAI.Matrix;

public class test {
    public static void main(String[] args) {
//       ANN brain = new ANN(3,{3,3},3);
//       brain.calcOutput;
        double[] in = {0,4,3,4,3};
        Matrix m = Matrix.fromArray(new double[][]{in});
        double[][] arr = Matrix.toArray(m);
        for(int y = 0;y< m.getRows();y++){
            for(int x = 0; x<m.getCols();x++){
                System.out.println(arr[y][x]);
            }
        }
    }
}
