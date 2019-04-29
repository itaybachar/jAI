package jAI2.layers;

public class InputLayer extends Layer {

    public InputLayer(int input_depth, int input_width, int input_height) {
        super(input_depth, input_width, input_height);
        this.input_depth = input_depth;
        this.input_width = input_width;
        this.input_height = input_height;
        initArrays();
    }

    public void setInputs(double[][][] inputs) {
        if (inMatchingDimensions(inputs))
            outputs = inputs;
        else throw new RuntimeException("Bad Dimensions!");
    }

    @Override
    public void build() {
    }

    @Override
    public void feedForward() {
    }

    @Override
    public void backprop() {
    }

    @Override
    public void updateWeights(double learning_rate) {
    }

    @Override
    public void printWeights() {
    }
}
