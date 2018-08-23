package pl.wozniaktomek.neural;

import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private NeuralOperations neuralOperations;

    private List<Layer> layers;
    private List<Connection> connections;
    private Boolean isBias;

    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    public NeuralNetwork() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
        isBias = false;

        neuralOperations = new NeuralOperations(layers, connections);
    }

    public void setObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        this.objectsLearning = objectsLearning;
        this.objectsTesting = objectsTesting;
    }

    public void addLayer(Integer numberOfNeurons) {
        layers.add(new Layer(numberOfNeurons));
        createConnections();
    }

    private void createConnections() {
        if (layers.size() > 1) {
            if (isBias) neuralOperations.createConnectionsWithBias();
            else neuralOperations.createConnectionsWithoutBias();
        }

        setNeuronNumbers();
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
