package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Backpropagation extends Thread {
    private NeuralNetwork neuralNetwork;
    private Learning learning;

    /* Learning parameters */
    private ArrayList<NeuralObject> learningData;
    private Double learningFactor;

    /* Ending conditions */
    private Integer iterationsAmount;
    private Double learningTolerance;

    /* Status parameters */
    private Integer iteration;
    private Double error;
    private Boolean isLearning;

    Backpropagation(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learning = neuralNetwork.getLearning();
        learningFactor = 0.1;
    }

    /* Initialization */
    void setEndingConditions(Integer iterationsAmount, Double learningTolerance) {
        this.iterationsAmount = iterationsAmount;
        this.learningTolerance = learningTolerance;
    }

    void setLearningParameters(Double learningFactor) {
        this.learningFactor = learningFactor;
    }

    /* Control */
    @Override
    public void run() {
        isLearning = true;
        startLearning();
    }

    private void startLearning() {
        initializeLearningData();
        initializeLearningParameters();
        initializeConnectionWeights();
        isLearning = true;
        learning();
    }

    void stopLearning() {
        isLearning = false;
    }

    private void learning() {
        while (isLearning && conditions()) {
            iteration++;

            for (NeuralObject neuralObject : learningData) {
                putInputData(neuralObject);
                countOutputs();
                countLastLayerError(neuralObject);
                countHiddenLayersError();
                modifyWeights();
            }

            updateInterface();
        }

        showIteration();
        endLearning();
    }

    /* Initialization */
    private void initializeLearningData() {
        learningData = neuralNetwork.getParameters().getLearningData();
    }

    private void initializeLearningParameters() {
        iteration = 0;

        if (neuralNetwork.getStructure().isBias()) {
            initializeBiasOutput();
        }
    }

    private void initializeConnectionWeights() {
        for (Connection connection : neuralNetwork.getStructure().getConnections()) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(-1.0, 1.0));
        }
    }

    private void initializeBiasOutput() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();

        for (int i = 0; i < layers.size() - 2; i++) {
            layers.get(i).getNeurons().get(layers.get(i).getNeurons().size() - 1).setOutput(1d);
        }
    }

    /* Ending conditions */
    private boolean conditions() {
        return iterationConditions();
    }

    private boolean iterationConditions() {
        return iteration < iterationsAmount;
    }

    /** Operations **/
    /* put new input vector x */
    private void putInputData(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(0).getNeurons();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
        }
    }

    /* count outputs for every neuron */
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

    /* count error for neurons in last layer */
    private void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(neuralNetwork.getStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        error = 0d;
        for (int i = 0; i < neurons.size(); i++) {
            double neuronError = Math.pow(correctAnswer.get(i) - neurons.get(i).getOutput(), 2);
            error += neuronError;
            neurons.get(i).setOutputError((neuronError));
        }
    }

    /* count error for neurons in every hidden layer */
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

    /* modify weights in network */
    private void modifyWeights() {
        List<Connection> connections = neuralNetwork.getStructure().getConnections();

        for (Connection connection : connections) {
            connection.setWeight(connection.getWeight() + (2 * learningFactor * connection.getNeuronOutput().getOutputError() * connection.getNeuronInput().getOutput()));
        }
    }

    /* interface update */
    private void updateInterface() {
        learning.getLearningWidget().updateInterface(iteration, error);
    }

    private void endLearning() {
        learning.getLearningWidget().endLearning();
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
