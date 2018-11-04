package pl.wozniaktomek.neural;

import pl.wozniaktomek.neural.learning.Backpropagation;
import pl.wozniaktomek.neural.learning.GeneticLearning;
import pl.wozniaktomek.neural.util.Parameters;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private Structure structure;
    private Parameters parameters;
    private Learning learning;

    public NeuralNetwork() {
        structure = new Structure(this);
        parameters = new Parameters(this);
        learning = null;
    }

    public boolean setObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        if (parameters.setObjects(objectsLearning, objectsTesting)) {
            createLearning(Learning.LearningMethod.GENETIC);
            return true;
        } else {
            return false;
        }
    }

    public void createLearning(Learning.LearningMethod learningMethod) {
        if (learningMethod.equals(Learning.LearningMethod.GENETIC)) {
            learning = new GeneticLearning(this);
        } else if (learningMethod.equals(Learning.LearningMethod.BACKPROPAGATION)) {
            learning = new Backpropagation(this);
        } else {
            learning = null;
        }
    }

    public void setLearningParameters(Double learningFactor) {
        ((Backpropagation) learning).setLearningParameters(learningFactor);
    }

    public void startLearning() {
        learning.startLearning();
        // #TODO replace by executor
    }

    public void stopLearning() {
        learning.stopLearning();
    }

    public Structure getStructure() {
        return structure;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Learning getLearning() {
        return learning;
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
