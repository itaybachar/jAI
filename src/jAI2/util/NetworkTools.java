package jAI2.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NetworkTools {
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
                    sb.append(array[z][y][x]);
                    sb.append(' ');
                }
                sb.append("\b}\n");
            }
            sb.append("\t}\n");
        }
        sb.append("}\n");
        System.out.println(sb);
    }
}
