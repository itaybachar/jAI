package com.jAI.util;


import java.util.ArrayList;
import java.util.function.Function;

public class Matrix {

    private double[][] mat;
    private int rows, cols;

    public Matrix(int rows, int cols) {
        this.mat = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    //======================================APPLY TO MATRIX==================================//
    //Add scalar
    public void add(double scalar) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] += scalar;
            }
        }
    }

    //Add matrices element wise
    public void add(Matrix in) {
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] += in.mat[y][x];
            }
        }
    }

    //Subtract scalar
    public void subtract(double scalar) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] -= scalar;
            }
        }
    }

    //Subtract matrices element wise
    public void subtract(Matrix in) {
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] -= in.mat[y][x];
            }
        }
    }

    //Multiply scalar
    public void multiply(double scalar) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] *= scalar;
            }
        }
    }

    //Multiply matrices element wise
    public void hadamard(Matrix in) {
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] *= in.mat[y][x];
            }
        }
    }

    //Divide scalar
    public void divide(double scalar) {
        Assert(scalar != 0.0, "Division By Zero");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] /= scalar;
            }
        }
    }

    //Divide matrices element wise
    public void divide(Matrix in) {
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Assert(in.mat[y][x] != 0.0, "Division By Zero");
                mat[y][x] /= in.mat[y][x];
            }
        }
    }

    //Randomize the matrix
    public void randomize(double low, double high) {
        double range = high - low;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] = (Math.random() * range) + low;
            }
        }
    }

    //Set element
    public void set(int row, int col, double val) {
        mat[row][col] = val;
    }

    //Set elements
    public void set(double[][] arr) {
        Assert(arr.length == rows && arr[0].length == cols, "Bad Dimensions");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] = arr[y][x];
            }
        }
    }

    //Get element
    public double get(int row, int col) {
        return mat[row][col];
    }

    //Apply function
    public void applyFunction(Function<Double, Double> func) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] = func.apply(mat[y][x]);
            }
        }
    }

    public boolean equals(Matrix in){
        Assert(in.cols == cols && in.rows == rows,"Bad Dimensions");
        boolean match = true;

        for(int y = 0;y<rows;y++){
            for(int x = 0; x<cols;x++){
                if(mat[y][x] != in.mat[y][x])
                    return false;
            }
        }
        return true;
    }

    public void normalize(double in_min,double in_max,double out_min, double out_max){
        double ratio = in_min/in_max;

        for(int y = 0; y<rows;y++){
            for(int x = 0;x<cols;x++){
                double val = mat[y][x]/(in_max-in_min);
                mat[y][x] = out_min + (out_max-out_min)*val;
            }
        }
    }

    //=====================================STATIC===========================================//
    public static void normalize_array(double[][] arr, double in_min,double in_max,double out_min, double out_max){
        double ratio = in_min/in_max;

        for(int y = 0; y<arr.length;y++){
            for(int x = 0;x<arr[0].length;x++){
                double val = arr[y][x]/(in_max-in_min);
                arr[y][x] = out_min + (out_max-out_min)*val;
            }
        }
    }
    //Dot product Matrices
    public static Matrix dot(Matrix m1, Matrix m2) {
        Assert(m1.cols == m2.rows, "Bad Dimensions");
        Matrix m = new Matrix(m1.rows, m2.cols);
        for (int y = 0; y < m1.rows; y++) {
            for (int x = 0; x < m2.cols; x++) {
                double sum = 0;
                for (int k = 0; k < m1.cols; k++) {
                    sum += m1.mat[y][k] * m2.mat[k][x];
                }
                m.mat[y][x] = sum;
            }
        }
        return m;
    }

    //Transpose the Matrix
    public static Matrix transpose(Matrix in) {
        Matrix m = new Matrix(in.cols, in.rows);
        for (int y = 0; y < in.rows; y++) {
            for (int x = 0; x < in.cols; x++) {
                m.mat[x][y]=in.mat[y][x];
            }
        }
        return m;
    }

    //Add scalar
    public static Matrix add(Matrix in, double scalar) {
        Matrix out = in.clone();
        out.add(scalar);

        return out;
    }

    //Add matrices element wise
    public static Matrix add(Matrix m1,Matrix m2) {
        Matrix out = m1.clone();
        out.add(m2);

        return out;
    }

    //Subtract scalar
    public static Matrix subtract(Matrix in, double scalar) {
        Matrix out = in.clone();
        out.subtract(scalar);

        return out;
    }

    //Subtract matrices element wise
    public static Matrix subtract(Matrix m1, Matrix m2) {
        Matrix out = m1.clone();
        out.subtract(m2);

        return out;
    }

    //Multiply scalar
    public static Matrix multiply(Matrix in, double scalar) {
        Matrix out = in.clone();
        out.multiply(scalar);

        return out;
    }

    //Multiply matrices element wise
    public static Matrix hadamard(Matrix m1, Matrix m2) {
        Matrix out = m1.clone();
        out.hadamard(m2);

        return out;
    }

    //Divide scalar
    public static Matrix divide(Matrix in, double scalar) {
        Matrix out = in.clone();
        out.divide(scalar);

        return out;
    }

    //Divide matrices element wise
    public static Matrix divide(Matrix m1, Matrix m2) {
        Matrix out = m1.clone();
        out.divide(m2);

        return m2;
    }

    //Apply function
    public static Matrix applyFunction(Matrix in, Function<Double, Double> func) {
        Matrix out = in.clone();
        out.applyFunction(func);

        return out;
    }

    //Convert array to matrix
    public static Matrix fromArray(double[][] arr) {
        Matrix m = new Matrix(arr.length, arr[0].length);
        for (int y = 0; y < arr.length; y++) {
            for (int x = 0; x < arr[0].length; x++) {
                m.set(y, x, arr[y][x]);
            }
        }
        return m;
    }

    //Convert Matrix to Array
    public static double[][] toArray(Matrix m) {
        double[][] arr = new double[m.rows][m.cols];
        for (int y = 0; y < m.rows; y++) {
            for (int x = 0; x < m.cols; x++) {
                arr[y][x] = m.get(y, x);
            }
        }
        return arr;
    }

    public static Matrix toMatrix(ArrayList<String> matString){
        int xIndex = matString.get(0).indexOf("x");
        int rows = Integer.parseInt(matString.get(0).substring(0,xIndex));
        int cols = Integer.parseInt(matString.get(0).substring(xIndex+1));

        Matrix m = new Matrix(rows,cols);

        for(int y = 0; y<rows;y++){
            int begIndex = 0;
            for(int x = 0; x<cols;x++){
                int endOfNum = matString.get(y+1).indexOf(" ",begIndex);
                double val = Double.parseDouble(matString.get(y+1).substring(begIndex,endOfNum));
                m.mat[y][x] = val;
                begIndex = endOfNum+1;
            }
        }
        return m;
    }

    /**
     * Finds the coordinates of the largest value in array
     * @return int[] of 2 elements, row and col
     */
    public int[] getCoordsOfMax(){
        int[] coords = new int[2];
        double max = Double.MIN_VALUE;
        for(int y = 0; y<rows;y++){
            for(int x = 0; x<cols;x++){
               if(mat[y][x]>max){
                   max = mat[y][x];
                   coords[0] = y;
                   coords[1] = x;
               }
            }
        }
        return coords;
    }

    /**
     * Finds the coordinates of the smallest value in array
     * @return int[] of 2 elements, row and col
     */
    public int[] getCoordsOfMin(){
        int[] coords = new int[2];
        double min = Double.MAX_VALUE;
        for(int y = 0; y<rows;y++){
            for(int x = 0; x<cols;x++){
                if(mat[y][x]<min){
                    min = mat[y][x];
                    coords[0] = y;
                    coords[1] = x;
                }
            }
        }
        return coords;
    }

    //Print out matrix
    public void print() {
        System.out.println(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        String dim = rows+"x"+cols+"\n";
        sb.append("{\n");
        sb.append(dim);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sb.append(mat[y][x]);
                sb.append(' ');
            }
            sb.append('\n');
        }
        sb.append("}\n");
        return sb.toString();
    }

    public Matrix clone(){
        Matrix m = new Matrix(rows,cols);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                m.mat[y][x] = mat[y][x];
            }
        }
        return m;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public static void Assert(boolean cond, String message) {
        if (!cond) {
            throw new IllegalArgumentException(message);
        }
    }

}
