package pl.wozniaktomek.neural.structure;

import pl.wozniaktomek.neural.operation.ActivationFunction;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Neuron> neurons;
    private ActivationFunction activationFunction;

    public Layer(Integer numberOfNeurons) {
        createLayer(numberOfNeurons);
    }

    private void createLayer(Integer numberOfNeurons) {
        neurons = new ArrayList<>();

        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(new Neuron(this));
        }
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public void setNumberOfNeurons(Integer numberOfNeurons) {
        createLayer(numberOfNeurons);
    }

    public Integer getLayerSize() {
        return neurons.size();
    }

    public void addNeuron() {
        neurons.add(new Neuron(this));
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }
}
