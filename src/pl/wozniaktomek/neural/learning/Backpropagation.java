package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.parameters.BackpropagationParameters;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.Collections;
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
    void setEndingConditions(Integer iterationsAmount, Double learningTolerance, Boolean isTotalTolerance) {
        backpropagationParameters.setIterationsAmount(iterationsAmount);
        backpropagationParameters.setLearningTolerance(learningTolerance);
        backpropagationParameters.setIsTotalTolerance(isTotalTolerance);
    }

    private void initializeBasicParameters() {
        backpropagationParameters.setLearningFactor(0.4);
        backpropagationParameters.setRecordsMixing(false);
    }

    /* Control */
    @Override
    public void run() {
        backpropagationParameters.setIsLearning(true);
        backpropagationParameters.setIteration(0);
        backpropagationParameters.setTotalError(1d);
        backpropagationParameters.setLearningData(learningService.initializeLearningData());
        backpropagationParameters.setObjectsOutOfTolerance(backpropagationParameters.getLearningData().size());

        learningService.initializeBiasOutput();
        learningService.initializeConnectionWeights(backpropagationParameters.getStructure().getConnections());

        learning();
    }

    void stopLearning() {
        backpropagationParameters.setIsLearning(false);
    }

    private void learning() {
        while (backpropagationParameters.getIsLearning() && conditions()) {
            backpropagationParameters.setIteration(backpropagationParameters.getIteration() + 1);

            List<NeuralObject> learningData = backpropagationParameters.getLearningData();
            if (backpropagationParameters.getRecordsMixing()) {
                Collections.shuffle(learningData);
            }

            for (NeuralObject neuralObject : learningData) {
                putInputData(neuralObject);
                countOutputs();
                countLastLayerError(neuralObject);
                countHiddenLayersError();
                modifyWeights();
            }

            countError();
            countObjectsOutOfTolerance();

            if (learning.getInterfaceUpdating()) {
                updateInterface();
            }

            if (learning.getLearningVisualization()) {
                updateVisualization();
            }

        }

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

    private void modifyWeights() {
        List<Connection> connections = backpropagationParameters.getStructure().getConnections();

        for (Connection connection : connections) {
            connection.setWeight(connection.getWeight() + (backpropagationParameters.getLearningFactor() * connection.getNeuronOutput().getErrorSignal() * connection.getNeuronInput().getOutput()));
        }
    }

    /* Parameters counting */
    private void countError() {
        backpropagationParameters.setTotalError(new StartupService(neuralNetwork).getTotalError(neuralNetwork.getParameters().getLearningData()));
    }

    private void countObjectsOutOfTolerance() {
        backpropagationParameters.setObjectsOutOfTolerance(new StartupService(neuralNetwork).getObjectsOutOfTolerance(neuralNetwork.getParameters().getLearningData()));
    }

    /* Ending conditions */
    private boolean conditions() {
        return iterationCondition() && toleranceCondition();
    }

    private boolean iterationCondition() {
        return backpropagationParameters.getIteration() < backpropagationParameters.getIterationsAmount();
    }

    private boolean toleranceCondition() {
        if (backpropagationParameters.getIsTotalTolerance()) {
            return backpropagationParameters.getTotalError() > learning.getLearningTolerance();
        } else {
            return backpropagationParameters.getObjectsOutOfTolerance() > 0;
        }
    }

    /* Interface updating */
    private void updateInterface() {
        learningService.updateLearningParameters(backpropagationParameters.getIteration(), backpropagationParameters.getTotalError(),
                backpropagationParameters.getObjectsOutOfTolerance().toString() + " / " + neuralNetwork.getParameters().getLearningData().size());
    }

    private void updateVisualization() {
        learningService.updateVisualization();
    }

    private void endLearning() {
        updateInterface();
        updateVisualization();
        neuralNetwork.setLearned(true);
        learning.getLearningWidget().endLearning();
    }

    /* Getter */
    public BackpropagationParameters getBackpropagationParameters() {
        return backpropagationParameters;
    }
}
