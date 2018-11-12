package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LearningService {
    private NeuralNetwork neuralNetwork;

    public LearningService(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /* Initialization */
    public ArrayList<NeuralObject> initializeLearningData() {
        return neuralNetwork.getParameters().getLearningData();
    }

    public void initializeBiasOutput(List<Layer> layers) {
        if (neuralNetwork.getStructure().isBias()) {
            for (int i = 0; i < layers.size() - 2; i++) {
                layers.get(i).getNeurons().get(layers.get(i).getNeurons().size() - 1).setOutput(1d);
            }
        }
    }

    public void initializeConnectionWeights(List<Connection> connections) {
        for (Connection connection : connections) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(-1.0, 1.0));
        }
    }

    /* Operations */
    /* put new input vector x */
    public void putInputData(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(0).getNeurons();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
        }
    }

    /* count outputs for every neuron */
    public void countOutputs() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();

        for (int i = 1; i < layers.size(); i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                double output = 0d;

                for (Connection connection : neuron.getConnectionsInput()) {
                    output += connection.getNeuronInput().getOutput() * connection.getWeight();
                }

                neuron.setOutput(neuron.getLayer().getActivationFunction().useFunction(output));
            }
        }
    }

    /* count error for neurons in last layer */
    public void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(neuralNetwork.getStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        for (int i = 0; i < neurons.size(); i++) {
            double neuronError = Math.pow(correctAnswer.get(i) - neurons.get(i).getOutput(), 2);
            neurons.get(i).setOutputError(neuronError);
        }
    }

    /* count error on network output */
    public Double countOutputError(Layer lastLayer) {
        double error = 0d;
        for (Neuron neuron : lastLayer.getNeurons()) {
            error += neuron.getOutputError();
        }

        return error;
    }
}
