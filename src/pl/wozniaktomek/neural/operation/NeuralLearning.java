package pl.wozniaktomek.neural.operation;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NeuralLearning extends Thread {
    private NeuralNetwork neuralNetwork;

    /* Data objects */
    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    /* Status parameters */
    private Boolean isLearning;
    private Integer iteration;

    /* Learning parameters */
    private Double learningFactor;
    private Double learningTolerance;
    private Integer maxIterations;

    public NeuralLearning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        objectsLearning = neuralNetwork.getNetworkParameters().getObjectsLearning();
        objectsTesting = neuralNetwork.getNetworkParameters().getObjectsTesting();
    }

    public void setLearningParameters(Double learningFactor, Double learningTolerance, Integer maxIterations) {
        this.learningFactor = learningFactor;
        this.learningTolerance = learningTolerance;
        this.maxIterations = maxIterations;
    }

    /* Control */
    @Override
    public void run() {
        isLearning = true;
        startLearning();
    }

    private void startLearning() {
        initializeLearningParameters();
        initializeConnectionWeights();
        learning();
    }

    public void stopLearning() {
        isLearning = false;
    }

    /* Learning */
    private void learning() {
        while (isLearning && conditions()) {
            learningIteration();
            iteration++;

            // showIteration();
        }

        showIteration();
    }

    private void learningIteration() {
        for (NeuralObject neuralObject : objectsLearning) {
            learningObject(neuralObject);
        }
    }

    private void learningObject(NeuralObject neuralObject) {
        putInputData(neuralObject);
        countOutputs();
        countLastLayerError(neuralObject);
        countHiddenLayersError();
        modifyWeights();
    }

    /* Ending conditions */
    private boolean conditions() {
        return isIteration();
    }

    private boolean isIteration() {
        return iteration < maxIterations;
    }

    private boolean isTolerance() {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(neuralNetwork.getStructure().getLayers().size() - 1).getNeurons();

        for (Neuron neuron : neurons) {
            if (neuron.getOutputError() > learningTolerance) {
                return false;
            }
        }

        return true;
    }

    /* Initialization */
    private void initializeLearningParameters() {
        iteration = 1;

        if (neuralNetwork.getStructure().isBias()) {
            initializeBiasOutput();
        }
    }

    private void initializeConnectionWeights() {
        for (Connection connection : neuralNetwork.getStructure().getConnections()) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(-0.5, 0.5));
        }
    }

    private void initializeBiasOutput() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();

        for (int i = 0; i < layers.size() - 2; i++) {
            layers.get(i).getNeurons().get(layers.get(i).getNeurons().size() - 1).setOutput(1d);
        }
    }

    /**
     * Operations
     */
    /* put new input vector x */
    private void putInputData(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(0).getNeurons();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
        }
    }

    /* count outputs for every neuron
     * net = sum(output * weight)
     * out = f(net)
     */
    private void countOutputs() {
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

    /* count delta for neurons in last layer
     * delta = 1/2 * (d - y)^2 * derivative
     */
    private void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(neuralNetwork.getStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutputError((Math.pow(correctAnswer.get(i) - neurons.get(i).getOutput(), 2)) /* * neurons.get(i).getOutputSigmoidDerivative() */);
        }
    }

    /* count delta for neurons in every hidden layer
     * delta = sum(outputError * weight) * derivative
     */
    private void countHiddenLayersError() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();

        for (int i = layers.size() - 2; i >= 0; i--) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                double outputError = 0d;

                for (Connection connection : neuron.getConnectionsOutput()) {
                    outputError += (connection.getNeuronOutput().getOutputError() * connection.getWeight());
                }

                neuron.setOutputError(outputError * neuron.getOutputSigmoidDerivative());
            }
        }
    }

    private void modifyWeights() {
        List<Connection> connections = neuralNetwork.getStructure().getConnections();

        for (Connection connection : connections) {
            connection.setWeight(connection.getWeight() + (2 * learningFactor * connection.getNeuronOutput().getOutputError() * connection.getNeuronInput().getOutput()));
        }
    }

    /* just for debug */
    private void showIteration() {
        System.out.println("\n Iteration " + iteration);

        int number = 0;
        for (Connection connection : neuralNetwork.getStructure().getConnections()) {
            System.out.println("Connection [" + (++number) + "] WEIGHT: " + connection.getWeight());
        }

        for (Layer layer : neuralNetwork.getStructure().getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron [" + neuron.getNumber() + "] OUTPUT: " + neuron.getOutput() + " | OUTPUT ERROR: " + neuron.getOutputError());
            }
        }
    }
}
