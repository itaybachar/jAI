import com.jAI.Matrix;

public class test {
    public static void main(String[] args) {
        Matrix m = new Matrix(3,2);
        m.add(2);
        Matrix m2 = new Matrix(3,2);
        m.divide(m2);
        m.print();
    }
}
