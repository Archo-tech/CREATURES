/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 94450
 */
public class BrainLayer {
    private double[][] neurons;
    private double[] biases;
    public BrainLayer(int in, int out)
    {
        neurons = new double[in][out];
        biases = new double[out];
        for(int r = 0; r < in; r++)
        {
            for(int c = 0;c < out; c++)
            {
                neurons[r][c] = Math.random() * 2 - 1;
            }
        }
        for(int i = 0; i < out; i++)
        {
            biases[i] = Math.random() * 2 - 1;
        }
    }
    
    public double[] feedForward(double[] input)
    {
        double[] output = new double[neurons[0].length];
        for(int i = 0 ;i < output.length; i++)
        {
          double sum = 0;
          for(int j = 0; j < input.length; j++)
          {
              sum+=input[j] * neurons[j][i];
              
          }
          output[i] =  Math.tanh(sum + biases[i]);
        }
        return output;
    }
    
    public BrainLayer mitose(double mutationRate)
    {
        BrainLayer newLayer = new BrainLayer(neurons.length, neurons[0].length);
        for(int i = 0; i < neurons.length; i++)
        {
            for(int j = 0; j < neurons[0].length; j++)
            {
                double rand = Math.random()*2-1;
                newLayer.neurons[i][j] = neurons[i][j] + rand * mutationRate;
            }
        }
        for(int i = 0; i < biases.length; i++)
        {
          for(int j = 0; j < biases.length; j++)
            {
                double rand = Math.random()*2-1;
                newLayer.biases[i] = biases[i] + rand * mutationRate;
            }  
        }
        return newLayer;
    }
}
