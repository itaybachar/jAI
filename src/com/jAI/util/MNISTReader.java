package com.jAI.util;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class MNISTReader {
    private static ByteArrayInputStream inputStream;
    public static int rows,cols;

        public static double[][][] loadImages(File imageFile,boolean normalize,boolean flatten) {
            try {
                inputStream = new ByteArrayInputStream(new FileInputStream(imageFile).readAllBytes());

                //Skip Magic number
                inputStream.skip(4);

                //Read Image Number
                int imageNum = nextNByte(4);

                //Get Image dimensions
                rows = nextNByte(4);
                cols = nextNByte(4);

                //Initialize the image array
                double[][][] images;
                if(flatten)
                    images = new double[imageNum][1][rows*cols];
                else
                    images = new double[imageNum][rows][cols];
                //Place the input
                for(int i = 0; i<imageNum;i++){
                    for(int row = 0; row<rows;row++){
                        for(int col = 0; col<cols;col++){
                            if(flatten)
                                images[i][0][row*cols + col] = inputStream.read();
                            else
                                images[i][col][row] = inputStream.read();
                        }
                    }
                }

                //Close Input Stream
                inputStream.close();

                //Normalize Array to values between 0-1
                if(normalize)
                    for(int i =0;i<imageNum;i++)
                    Matrix.normalize_array(images[i],0,255,0,1);

                //Verbose Output
                System.out.println("Images Loaded!");

                return images;
            } catch (IOException e) {
              e.getCause();
            }

            throw new RuntimeException("Could not load Images");
        }

    public static double[] loadLabels(File labelFile) {
        try {
            inputStream = new ByteArrayInputStream(new FileInputStream(labelFile).readAllBytes());

            //Skip Magic number
            inputStream.skip(4);

            //Read Image Number
            int labelNum = nextNByte(4);

            //Initialize the image array
            double[] labels = new double[labelNum];

            //Place the input
            for(int i = 0; i<labelNum;i++){
                labels[i] = nextNByte(1);
            }

            //Close Input Stream
            inputStream.close();

            //Verbose Output
            System.out.println("Labels Loaded!");

            return labels;
        } catch (IOException e) {
            e.getCause();
        }

        throw new RuntimeException("Could not load Labels");
    }

    private static int nextNByte(int n) throws IOException {
        int k=inputStream.read()<<((n-1)*8);
        for(int i =n-2;i>=0;i--){
            k+=inputStream.read()<<(i*8);
        }
        return k;
    }

    public static double[][] labelToNNOutputs(double[] labels) {
        double[][] outputs = new double[labels.length][10];

        for (int i = 0; i < labels.length; i++) {
            outputs[i][(int) labels[i]] = 1;
        }
        return outputs;
    }

    public static String renderImage(double[] image) {
        StringBuffer sb = new StringBuffer();

        for (int row = 0; row < rows; row++) {
            sb.append("|");
            for (int col = 0; col < cols; col++) {
                double pixelVal = image[row*cols + col];
                if (pixelVal == 0)
                    sb.append(" ");
                else if (pixelVal < 256 / 3)
                    sb.append(".");
                else if (pixelVal < 2 * (256 / 3))
                    sb.append("x");
                else
                    sb.append("X");
            }
            sb.append("|\n");
        }

        return sb.toString();
    }

}
