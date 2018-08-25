package pl.wozniaktomek.neural.manage;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.operation.NeuralConnection;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.ArrayList;
import java.util.List;

public class NeuralStructure {
    private NeuralNetwork neuralNetwork;
    private List<Layer> layers;
    private List<Connection> connections;
    private Boolean isBias;

    public NeuralStructure(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        clearStructure();
    }

    public void clearStructure() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
        isBias = false;
    }

    public void addLayer(Integer numberOfNeurons) {
        layers.add(new Layer(numberOfNeurons));
        createConnections();
    }

    public void addBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).addNeuron();
        }

        isBias = true;
        createConnections();
    }

    public void deleteBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).getNeurons().remove(layers.get(i).getNeurons().size() - 1);
        }

        isBias = false;
        createConnections();
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Boolean isBias() {
        return isBias;
    }

    private void createConnections() {
        NeuralConnection neuralConnection = new NeuralConnection(neuralNetwork);

        if (layers.size() > 1) {
            if (isBias) {
                neuralConnection.createConnectionsWithBias();
            } else {
                neuralConnection.createConnectionsWithoutBias();
            }

            setNeuronNumbers();
        }
    }

    private void setNeuronNumbers() {
        int number = 0;
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                neuron.setNumber(++number);
            }
        }
    }
}
