package pl.wozniaktomek.neural.operation;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.manage.NeuralStructure;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.List;

public class NeuralConnection {
    private List<Layer> layers;
    private List<Connection> connections;

    public NeuralConnection(NeuralNetwork neuralNetwork) {
        NeuralStructure neuralStructure = neuralNetwork.getNeuralStructure();
        this.layers = neuralStructure.getLayers();
        this.connections = neuralStructure.getConnections();
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
}
