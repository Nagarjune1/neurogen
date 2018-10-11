package pl.wozniaktomek.neural;

import pl.wozniaktomek.neural.manage.NeuralParameters;
import pl.wozniaktomek.neural.manage.NeuralStructure;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private NeuralStructure neuralStructure;
    private NeuralParameters neuralParameters;

    public NeuralNetwork() {
        neuralStructure = new NeuralStructure(this);
        neuralParameters = new NeuralParameters(this);
    }

    public NeuralStructure getNeuralStructure() {
        return neuralStructure;
    }

    public NeuralParameters getNeuralParameters() {
        return neuralParameters;
    }

    public boolean setObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        return neuralParameters.setObjects(objectsLearning, objectsTesting);
    }

    /* just for debug */
    public void showNetwork() {
        System.out.println("\n ## NETWORK ##");

        System.out.println("\n # NEURONS");
        List<Layer> layers = neuralStructure.getLayers();
        System.out.println("Layers amount: " + layers.size());
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron number " + neuron.getNumber());
            }
        }

        System.out.println("\n # CONNECTIONS");
        List<Connection> connections = neuralStructure.getConnections();
        System.out.println("Connections amount: " + connections.size());
        for (Connection connection : connections) {
            System.out.println("Connection between " + connection.getNeuronInput().getNumber() + " and " + connection.getNeuronOutput().getNumber());
        }
    }
}
