package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.parameters.BackpropagationParameters;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.List;

public class Backpropagation extends Thread {
    private NeuralNetwork neuralNetwork;
    private Learning learning;
    private LearningService learningService;
    private BackpropagationParameters backpropagationParameters;

    Backpropagation(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learning = neuralNetwork.getLearning();
        learningService = new LearningService(neuralNetwork);
        backpropagationParameters = new BackpropagationParameters(neuralNetwork.getStructure());
        initializeBasicParameters();
    }

    /* Initialization */
    void setEndingConditions(Integer iterationsAmount, Double learningTolerance) {
        backpropagationParameters.setIterationsAmount(iterationsAmount);
        backpropagationParameters.setLearningTolerance(learningTolerance);
    }

    private void initializeBasicParameters() {
        backpropagationParameters.setLearningFactor(0.1);
    }

    /* Control */
    @Override
    public void run() {
        backpropagationParameters.setIsLearning(true);
        backpropagationParameters.setIteration(0);
        backpropagationParameters.setTotalEror(1d);
        backpropagationParameters.setLearningData(learningService.initializeLearningData());

        learningService.initializeBiasOutput(backpropagationParameters.getStructure().getLayers());
        learningService.initializeConnectionWeights(backpropagationParameters.getStructure().getConnections());

        learning();
    }

    void stopLearning() {
        backpropagationParameters.setIsLearning(false);
    }

    private void learning() {
        while (backpropagationParameters.getIsLearning() && conditions()) {
            backpropagationParameters.setIteration(backpropagationParameters.getIteration() + 1);

            for (NeuralObject neuralObject : backpropagationParameters.getLearningData()) {
                backpropagationParameters.setSSE(0d);

                putInputData(neuralObject);
                countOutputs();
                countLastLayerError(neuralObject);
                countSSE();
                countHiddenLayersError();
                modifyWeights();
            }

            updateInterface();
            backpropagationParameters.setTotalEror(new StartupService(neuralNetwork).getTotalError(neuralNetwork.getParameters().getLearningData()));
        }

        showIteration();
        endLearning();
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

    private void countSSE() {
        double currentSSE = backpropagationParameters.getSSE();

        Layer lastLayer = backpropagationParameters.getStructure().getLayers().get(backpropagationParameters.getStructure().getLayers().size() - 1);
        for (Neuron neuron : lastLayer.getNeurons()) {
            currentSSE += neuron.getOutputError();
        }

        backpropagationParameters.setSSE(currentSSE);
    }

    /* backpropagation -  count error for neurons in every hidden layer */
    private void countHiddenLayersError() {
        List<Layer> layers = backpropagationParameters.getStructure().getLayers();

        for (int i = layers.size() - 2; i >= 0; i--) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                double errorSignal = 0d;

                for (Connection connection : neuron.getConnectionsOutput()) {
                    errorSignal += (connection.getNeuronOutput().getErrorSignal() * connection.getWeight());
                }

                errorSignal = neuron.getOutput() * (1 - neuron.getOutput()) * errorSignal;
                neuron.setErrorSignal(errorSignal);
            }
        }
    }

    /* backpropagation - modify weights in network */
    private void modifyWeights() {
        List<Connection> connections = backpropagationParameters.getStructure().getConnections();

        for (Connection connection : connections) {
            connection.setWeight(connection.getWeight() + (backpropagationParameters.getLearningFactor() * connection.getNeuronOutput().getErrorSignal() * connection.getNeuronInput().getOutput()));
        }
    }

    /* Ending conditions */
    private boolean conditions() {
        return iterationCondition() && toleranceCondition();
    }

    private boolean iterationCondition() {
        return backpropagationParameters.getIteration() < backpropagationParameters.getIterationsAmount();
    }

    private boolean toleranceCondition() {
        return backpropagationParameters.getTotalEror() > learning.getLearningTolerance();
    }

    /* interface update */
    private void updateInterface() {
        learning.getLearningWidget().updateInterface(backpropagationParameters.getIteration(), backpropagationParameters.getTotalEror());
    }

    private void endLearning() {
        neuralNetwork.setLearned(true);
        learning.getLearningWidget().endLearning();
    }

    /* get parameters */
    public BackpropagationParameters getBackpropagationParameters() {
        return backpropagationParameters;
    }

    /* just for debug */
    private void showIteration() {
        System.out.println("\n Iteration " + backpropagationParameters.getIteration());

        int number = 0;
        for (Connection connection : backpropagationParameters.getStructure().getConnections()) {
            System.out.println("Connection [" + (++number) + "] WEIGHT: " + connection.getWeight());
        }

        for (Layer layer : backpropagationParameters.getStructure().getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                System.out.println("Neuron [" + neuron.getNumber() + "] OUTPUT: " + neuron.getOutput() + " | OUTPUT ERROR: " + neuron.getOutputError());
            }
        }
    }
}
