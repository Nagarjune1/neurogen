package pl.wozniaktomek.neural;

import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.neural.util.Parameters;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private Structure structure;
    private Parameters parameters;
    private Learning learning;

    private Boolean isLearned;

    /* Interface */
    private NeuralControl neuralControl;

    public NeuralNetwork() {
        structure = new Structure(this);
        parameters = new Parameters(this);
        learning = new Learning(this);
        isLearned = false;
    }

    /* Learning data */
    public boolean loadLearningData(ArrayList<NeuralObject> learningData) {
        return parameters.setLearningData(learningData);
    }

    /* Learning */
    public void createLearning(Learning.LearningMethod learningMethod) {
        learning.createLearning(learningMethod);
    }

    public void startLearning() {
        learning.startLearning();
    }

    public void stopLearning() {
        learning.stopLearning();
    }

    /* Getters */
    public Structure getStructure() {
        return structure;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Learning getLearning() {
        return learning;
    }

    public Boolean getLearned() {
        return isLearned;
    }

    /* Setters */
    public void setLearned(Boolean learned) {
        isLearned = learned;
        neuralControl.getStartupWidget().refreshWidget();
        neuralControl.getTestWidget().refreshWidget();
    }

    /* Interface control */
    public void setNeuralControl(NeuralControl neuralControl) {
        this.neuralControl = neuralControl;
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
