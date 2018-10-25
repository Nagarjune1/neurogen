package pl.wozniaktomek.neural.manage;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.operation.ActivationFunction;
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

    void clearStructure() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
        isBias = false;
    }

    public void addLayer(Integer numberOfNeurons) {
        Layer newLayer = new Layer(numberOfNeurons);
        newLayer.setActivationFunction(new ActivationFunction());

        if (layers.size() >= 2) {
            Layer lastLayer = layers.get(layers.size() - 1);
            layers.remove(lastLayer);

            layers.add(newLayer);
            layers.add(lastLayer);
        } else {
            layers.add(newLayer);
        }

        createConnections();
    }

    public void deleteLayer(Integer layerNumber) {
        layers.remove(layerNumber.intValue());
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

    public void createConnections() {
        setNeuronNumbers();

        NeuralConnection neuralConnection = new NeuralConnection(neuralNetwork);
        if (layers.size() > 1) {
            if (isBias) {
                neuralConnection.createConnectionsWithBias();
            } else {
                neuralConnection.createConnectionsWithoutBias();
            }
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

    public List<Layer> getLayers() {
        return layers;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Boolean isBias() {
        return isBias;
    }
}
