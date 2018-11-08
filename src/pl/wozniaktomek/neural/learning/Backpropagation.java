package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;

public class Backpropagation extends Thread {
    private NeuralNetwork neuralNetwork;
    private Structure structure;
    private Learning learning;
    private LearningService learningService;

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
        structure = neuralNetwork.getStructure();
        learning = neuralNetwork.getLearning();
        learningService = new LearningService(neuralNetwork);
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
        learningData = learningService.initializeLearningData();
        learningService.initializeBiasOutput(structure.getLayers());
        learningService.initializeConnectionWeights(structure.getConnections());
        iteration = 0;
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
                countOutputError();
                countHiddenLayersError();
                modifyWeights();
            }

            updateInterface();
        }

        showIteration();
        endLearning();
    }

    /* Ending conditions */
    private boolean conditions() {
        return iterationConditions() && toleranceConditions();
    }

    private boolean iterationConditions() {
        return iteration < iterationsAmount;
    }

    private boolean toleranceConditions() {
        return true;
    }

    /* Operations */
    private void putInputData(NeuralObject neuralObject) {
        learningService.putInputData(neuralObject);
    }

    private void countOutputs() {
        learningService.countOutputs();
    }

    private void countLastLayerError(NeuralObject neuralObject) {
        learningService.countLastLayerError(neuralObject);
    }

    private void countOutputError() {
        error = learningService.countOutputError(structure.getLayers().get(structure.getLayers().size() - 1));
    }

    /* backpropagation -  count error for neurons in every hidden layer */
    private void countHiddenLayersError() {
        List<Layer> layers = structure.getLayers();

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

    /* backpropagation - modify weights in network */
    private void modifyWeights() {
        List<Connection> connections = structure.getConnections();

        for (Connection connection : connections) {
            connection.setWeight(connection.getWeight() + (2 * learningFactor * connection.getNeuronOutput().getOutputError() * connection.getNeuronInput().getOutput()));
        }
    }

    /* interface update */
    private void updateInterface() {
        learning.getLearningWidget().updateInterface(iteration, error);
    }

    private void endLearning() {
        neuralNetwork.setLearned(true);
        learning.getLearningWidget().endLearning();
    }

    /* just for debug */
    private void showIteration() {
        System.out.println("\n Iteration " + iteration);

        int number = 0;
        for (Connection connection : structure.getConnections()) {
            System.out.println("Connection [" + (++number) + "] WEIGHT: " + connection.getWeight());
        }

        for (Layer layer : structure.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron [" + neuron.getNumber() + "] OUTPUT: " + neuron.getOutput() + " | OUTPUT ERROR: " + neuron.getOutputError());
            }
        }
    }
}
