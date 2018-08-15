package pl.wozniaktomek.neural;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Layer> layers;
    private List<Connection> connections;

    public NeuralNetwork() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
    }

    public void addLayer(Integer numberOfNeurons) {
        layers.add(new Layer(numberOfNeurons));
    }

    public void createConnections() {
        for (int i = 0; i < layers.size() - 1; i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                for (Neuron nextNeuron : layers.get(i + 1).getNeurons()) {
                    Connection connection = new Connection();
                    connection.setNeuronInput(neuron);
                    connection.setNeuronOutput(nextNeuron);

                    neuron.getConnectionsOutput().add(connection);
                    nextNeuron.getConnectionsInput().add(connection);

                    connections.add(connection);
                }
            }
        }
    }
}
