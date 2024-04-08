/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 94450
 */
public class Brain {
    private BrainLayer[] layers;
    
    public Brain(int[] sizes)
    {
        layers = new BrainLayer[sizes.length-1];
        int inputSize = sizes[0];
        for(int i = 1; i < sizes.length; i++)
        {
            int outputSize = sizes[i];
            layers[i-1]=new BrainLayer(inputSize, outputSize);
            inputSize=outputSize;
        }
    }
    public double[] think(double[] data)
    {
        for(BrainLayer layer : layers)
        {
            data = layer.feedForward(data);
        }
        return data;
    }
    public Brain mitose(double mutationRate)
    {
        BrainLayer[] newLayers = new BrainLayer[layers.length];
        Brain newBrain = new Brain(new int[]{1,1});
        for(int i = 0; i < layers.length; i++)
        {
            newLayers[i] = layers[i].mitose(mutationRate);
        }
        newBrain.layers = newLayers;
        return newBrain;
    }
    
}
