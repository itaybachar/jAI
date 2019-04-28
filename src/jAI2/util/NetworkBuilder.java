package jAI2.util;

import jAI2.Network;
import jAI2.layers.InputLayer;
import jAI2.layers.Layer;
import jAI2.layers.OutputLayer;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkBuilder {
    private InputLayer input;
    private OutputLayer output;
    private ArrayList<Layer> layers;

    public NetworkBuilder(int input_depth,int input_width,int input_height){
        input = new InputLayer(input_depth,input_width,input_height);
        layers = new ArrayList<>();
    }

    public Network createNetwork() {
        try {
            Layer cur = input;
            for (Layer l : layers) {
                cur.connectToNextLayer(l);
                cur = l;
            }
            output = new OutputLayer(cur);
            cur.connectToNextLayer(output);
            return new Network().setInputLayer(input).setOutputLayer(output);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addLayer(Layer layer){
        layers.add(layer);
    }

    public void addLayers(Layer... _layers){
        Collections.addAll(layers,_layers);
    }


}
