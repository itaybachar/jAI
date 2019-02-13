package com.jAI;

import com.sun.jdi.Method;

import java.util.function.Function;

public class Matrix {

    private double[][] mat;
    private int rows,cols;

    public Matrix(int rows, int cols){
        this.mat = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

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
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions",getLineNumber());
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
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions",getLineNumber());
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
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions",getLineNumber());
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] *= in.mat[y][x];
            }
        }
    }

    //Dot product Matrices
    static Matrix dot(Matrix m1,Matrix m2){
        Assert(m1.cols == m2.rows,"Bad Dimensions",getLineNumber());
        Matrix m = new Matrix(m1.rows,m2.cols);
        for(int y= 0; y<m1.rows;y++){
            for(int x = 0; x<m2.cols;x++){
                double sum = 0;
                for(int k = 0; k<m1.cols;k++){
                    sum += m1.mat[y][k]*m2.mat[k][x];
                }
                m.mat[y][x] = sum;
            }
        }
        return m;
    }

    //Divide scalar
    public void divide(double scalar) {
        Assert(scalar != 0.0, "Division By Zero",getLineNumber());
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] /= scalar;
            }
        }
    }

    //Divide matrices element wise
    public void divide(Matrix in) {
        Assert(rows == in.rows && cols == in.cols, "Bad Dimensions",getLineNumber());
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Assert(in.mat[y][x] != 0.0, "Division By Zero",getLineNumber());
                mat[y][x] /= in.mat[y][x];
            }
        }
    }

    //Randomize the matrix
    public void randomize(double low, double high){
        double range = high-low;

        for(int y = 0; y<rows;y++){
            for(int x = 0; x<cols; x++){
                mat[y][x] = (Math.random()*range) + low;
            }
        }
    }

    //Set element
    public void set(int row,int col,double val){
        mat[row][col] = val;
    }

    //Get element
    public double get(int row,int col){
        return mat[row][col];
    }

    //Apply function
    public void applyFunction(Function<Double,Double> func){
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mat[y][x] = func.apply(mat[y][x]);
            }
        }
    }

    //Convert array to matrix
    public static Matrix fromArray(double[][] arr){
        Matrix m = new Matrix(arr.length,arr[0].length);
        for(int y = 0; y<arr.length;y++) {
            for (int x = 0; x < arr[0].length; x++) {
                m.set(y, x, arr[y][x]);
            }
        }
        return m;
    }

    //Convert Matrix to Array
    public static double[][] toArray(Matrix m){
        double[][] arr = new double[m.rows][m.cols];
        for(int y = 0;y< m.rows;y++){
            for(int x = 0; x<m.cols;x++){
                arr[y][x] = m.get(y,x);
            }
        }
        return arr;
    }

    //Print out matrix
    public void print(){
        for(int y = 0; y<rows;y++){
            for(int x = 0; x<cols; x++){
                System.out.print(mat[y][x] +  " ");
            }
            System.out.println();
        }
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    static void Assert(boolean cond,String message,int line){
        if(!cond){
            System.err.println(message + " Line: " +line);
            System.exit(-1);
        }
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

}
