package pl.wozniaktomek.neural.structure;

import pl.wozniaktomek.neural.util.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Neuron> neurons;
    private ActivationFunction activationFunction;

    Layer(Integer neuronsAmount, ActivationFunction activationFunction) {
        createLayer(neuronsAmount);
        this.activationFunction = activationFunction;
    }

    private void createLayer(Integer neuronsAmount) {
        neurons = new ArrayList<>();

        for (int i = 0; i < neuronsAmount; i++) {
            neurons.add(new Neuron(this));
        }
    }

    void addNeuron() {
        neurons.add(new Neuron(this));
    }

    /* Getters */
    public List<Neuron> getNeurons() {
        return neurons;
    }

    public Integer getLayerSize() {
        return neurons.size();
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /* Setters */
    public void setNumberOfNeurons(Integer neuronsAmount) {
        createLayer(neuronsAmount);
    }
}
