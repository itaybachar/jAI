package com.jAI.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MNISTReader {
    private static FileInputStream inputStream;

    public static double[][] loadImages(File imageFile) {
        try {
            inputStream = new FileInputStream(imageFile);

            //Skip Magic number
            inputStream.skip(4);

            //Read Image Number
            int imageNum = nextNByte(4);

            //Get Image dimensions
            int rows = nextNByte(4);
            int cols = nextNByte(4);

            //Initialize the image array
            double[][] images = new double[imageNum][rows*cols];

            //Place the input
            for(int i = 0; i<imageNum;i++){
                for(int k = 0; k<cols*rows;k++){
                    images[i][k]= nextNByte(1);
                }
            }

            //Close Input Stream
            inputStream.close();

            return images;
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        return null;
    }

    public static double[][] loadLabels(File labelFile) {
        try {
            inputStream = new FileInputStream(labelFile);

            //Skip Magic number
            inputStream.skip(4);

            //Read Image Number
            int labelNum = nextNByte(4);

            //Initialize the image array
            double[][] labels = new double[labelNum][10];

            //Place the input
            for(int i = 0; i<labelNum;i++){
                int label = nextNByte(1);
                labels[i][label] = 1;
            }

            //Close Input Stream
            inputStream.close();

            return labels;
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        return null;
    }

    private static int nextNByte(int n) throws IOException {
        int k=inputStream.read()<<((n-1)*8);
        for(int i =n-2;i>=0;i--){
            k+=inputStream.read()<<(i*8);
        }
        return k;
    }


}
