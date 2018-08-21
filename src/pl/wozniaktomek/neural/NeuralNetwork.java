package pl.wozniaktomek.neural;

import pl.wozniaktomek.service.data.DataObject;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Layer> layers;
    private List<Connection> connections;
    private Boolean isBias;

    private ArrayList<DataObject> objectsLearning;
    private ArrayList<DataObject> objectsTesting;

    public NeuralNetwork() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
        isBias = false;
    }

    public void setObjects(ArrayList<DataObject> objectsLearning, ArrayList<DataObject> objectsTesting) {
        this.objectsLearning = objectsLearning;
        this.objectsTesting = objectsTesting;
    }

    public void addLayer(Integer numberOfNeurons) {
        layers.add(new Layer(numberOfNeurons));
        createConnections();
    }

    private void createConnections() {
        if (layers.size() > 1) {
            if (isBias) createConnectionsWithBias();
            else createConnectionsWithoutBias();
        }

        setNeuronNumbers();
    }

    private void createConnectionsWithBias() {
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

    private void createConnectionsWithoutBias() {
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

    public Boolean isBias() {
        return isBias;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    private void setNeuronNumbers() {
        int number = 0;
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                neuron.setNumber(++number);
            }
        }
    }

    /* just for debug */
    public void showNetwork() {
        System.out.println("## NETWORK ##");

        System.out.println("\n # NEURONS");
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron number " + neuron.getNumber());
            }
        }

        System.out.println("\n # CONNECTIONS");
        for (Connection connection : connections) {
            System.out.println("Connection between " + connection.getNeuronInput().getNumber() + " and " + connection.getNeuronOutput().getNumber());
        }
    }
}
