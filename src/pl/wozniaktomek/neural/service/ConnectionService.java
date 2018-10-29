package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.List;

public class ConnectionService {
    private List<Layer> layers;
    private List<Connection> connections;

    public ConnectionService(NeuralNetwork neuralNetwork) {
        Structure structure = neuralNetwork.getStructure();
        this.layers = structure.getLayers();
        this.connections = structure.getConnections();
        clearConnections();
    }

    public void createConnectionsWithBias() {
        for (int i = 0; i < layers.size() - 2; i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                List<Neuron> nextNeurons = layers.get(i + 1).getNeurons();
                for (int k = 0; k < nextNeurons.size() - 1; k++) {
                    createConnection(neuron, nextNeurons.get(k));
                }
            }
        }

        for (Neuron neuron : layers.get(layers.size() - 2).getNeurons()) {
            for (Neuron nextNeuron : layers.get(layers.size() - 1).getNeurons()) {
                createConnection(neuron, nextNeuron);
            }
        }
    }

    public void createConnectionsWithoutBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                for (Neuron nextNeuron : layers.get(i + 1).getNeurons()) {
                    createConnection(neuron, nextNeuron);
                }
            }
        }
    }

    private void createConnection(Neuron neuronFrom, Neuron neuronTo) {
        Connection connection = new Connection();
        connection.setNeuronInput(neuronFrom);
        connection.setNeuronOutput(neuronTo);

        neuronFrom.getConnectionsOutput().add(connection);
        neuronTo.getConnectionsInput().add(connection);

        connections.add(connection);
    }

    private void clearConnections() {
        connections.clear();

        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                neuron.getConnectionsInput().clear();
                neuron.getConnectionsOutput().clear();
            }
        }
    }
}
