package pl.wozniaktomek.neural;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Neuron> neurons;

    public Layer(Integer numberOfNeurons) {
        createLayer(numberOfNeurons);
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    private void createLayer(Integer numberOfNeurons) {
        neurons = new ArrayList<>();

        for (int i = 0; i < numberOfNeurons; i++)
            neurons.add(new Neuron());
    }
}
