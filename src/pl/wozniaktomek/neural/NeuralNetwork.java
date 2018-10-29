package pl.wozniaktomek.neural;

import pl.wozniaktomek.neural.util.NetworkParameters;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.operation.NeuralLearning;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private Structure structure;
    private NetworkParameters networkParameters;
    private NeuralLearning neuralLearning;

    public NeuralNetwork() {
        structure = new Structure(this);
        networkParameters = new NetworkParameters(this);
    }

    public boolean setObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        return networkParameters.setObjects(objectsLearning, objectsTesting);
    }

    public void startLearning(Double learningFactor, Double learningTolerance, Integer maxIterations) {
        neuralLearning = new NeuralLearning(this);
        neuralLearning.setLearningParameters(learningFactor, learningTolerance, maxIterations);
        neuralLearning.run();
    }

    public void stopLearning() {
        neuralLearning.stopLearning();
    }

    public Structure getStructure() {
        return structure;
    }

    public NetworkParameters getNetworkParameters() {
        return networkParameters;
    }

    public NeuralLearning getNeuralLearning() {
        return neuralLearning;
    }

    /* just for debug */
    public void showNetwork() {
        System.out.println("\n ## NETWORK ##");

        System.out.println("\n # NEURONS");
        List<Layer> layers = structure.getLayers();
        System.out.println("Layers amount: " + layers.size());
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron number " + neuron.getNumber());
            }
        }

        System.out.println("\n # CONNECTIONS");
        List<Connection> connections = structure.getConnections();
        System.out.println("Connections amount: " + connections.size());
        for (Connection connection : connections) {
            System.out.println("Connection between " + connection.getNeuronInput().getNumber() + " and " + connection.getNeuronOutput().getNumber());
        }
    }
}
