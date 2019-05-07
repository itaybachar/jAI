package jAI2.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NetworkTools {

    public static double[][][][] createRandomArray(int num,int depth, int width,int height,double lowerLimit,double higherLimit){
        double[][][][] arr = new double[num][depth][width][height];
        for(int z = 0; z<num;z++)
            arr[z] = createRandomArray(depth,width,height,lowerLimit,higherLimit);
        return arr;
    }

    public static double[][][] createRandomArray(int depth,int width,int height,double lowerLimit,double higherLimit){
        double[][][] arr = new double[depth][width][height];
        for(int z = 0; z<depth;z++)
            arr[z] = createRandomArray(width,height,lowerLimit,higherLimit);
        return arr;
    }

    public static double[][] createRandomArray(int width,int height,double lowerLimit,double higherLimit){
        double[][] arr = new double[width][height];
        for(int w = 0; w<width;w++)
                arr[w]= createRandomArray(height,lowerLimit,higherLimit);
        return arr;
    }

    public static double[] createRandomArray(int size,double lowerLimit,double higherLimit){
        double[] arr = new double[size];
        for(int i = 0; i<size;i++)
            arr[i] = getRandomNumber(lowerLimit,higherLimit);
        return arr;
    }

    public static double getRandomNumber(double lowerLimit, double higherLimit){
        return Math.random()*(higherLimit-lowerLimit) + lowerLimit;
    }

    public static void printArray(double[] array){
        printArray(new double[][]{array});
    }

    public static void printArray(double[][] array){
        printArray(new double[][][]{array});
    }

    public static void printArray(double[][][] array) {
        StringBuilder sb = new StringBuilder();
        String dim = array.length+"x"+array[0].length+"x"+array[0][0].length + "\n";
        sb.append(dim);
        sb.append("{\n");
        for(int z = 0; z<array.length;z++) {
            sb.append("\t{\n");
            for (int y = 0; y < array[0].length; y++) {
                sb.append("\t\t{");
                for (int x = 0; x < array[0][0].length; x++) {
                    if(x!=0)
                        sb.append(", ");
                    sb.append(array[z][y][x]);

                }
                sb.append("}\n");
            }
            sb.append("\t}\n");
        }
        sb.append("}\n");
        System.out.println(sb);
    }

    public static double[][][] to3DArray(double[][] in){
        return new double[][][]{in};
    }

    public static double[][][] to3DArray(double[] in){
        return new double[][][]{{in}};
    }

    public static double[][] to2DArray(double[] in){
        return new double[][]{in};
    }
}
