package pl.wozniaktomek.neural.operation;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.NeuralObject;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NeuralLearning extends Thread {
    private NeuralNetwork neuralNetwork;

    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    private Boolean isLearning;
    private Integer iteration;

    private static Integer GENERATIONS = 2;

    public NeuralLearning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        objectsLearning = neuralNetwork.getNeuralParameters().getObjectsLearning();
        objectsTesting = neuralNetwork.getNeuralParameters().getObjectsTesting();
    }

    /* Control */
    private void startLearning() {
        initializeLearningParameters();
        initializeConnectionWeights();
        learning();
    }

    private void stopLearning() {
        isLearning = false;
    }

    @Override
    public void run() {
        startLearning();
    }

    /* Learning */
    private void learning() {
        if (isLearning) {
            while (conditions()) {
                for (NeuralObject neuralObject : objectsLearning) {
                    System.out.println();
                    putInputData(neuralObject);

                    countOutputs();

                    countLastLayerError(neuralObject);
                    countHiddenLayersError();

                    modifyWeights();
                }

                iteration++;
            }

            stopLearning();
        }
    }

    /* Ending conditions */
    private boolean conditions() {
        return iteration < GENERATIONS;
    }

    /* Initialization */
    private void initializeLearningParameters() {
        iteration = 1;
        isLearning = true;

        if (neuralNetwork.getNeuralStructure().isBias()) {
            initializeBiasOutput();
        }
    }

    private void initializeConnectionWeights() {
        int number = 1;

        for (Connection connection : neuralNetwork.getNeuralStructure().getConnections()) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(-1.0, 1.0));

            // System.out.println("Połączenie [" + (number++) + "] ma wagę " + connection.getWeight());
        }
    }

    private void initializeBiasOutput() {
    }

    /* Operations */
    private void putInputData(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getNeuralStructure().getLayers().get(0).getNeurons();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
        }
    }

    private void countOutputs() {
        List<Layer> layers = neuralNetwork.getNeuralStructure().getLayers();

        for (int i = 1; i < layers.size(); i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                double rawOutput = 0d;

                for (Connection connection : neuron.getConnectionsInput()) {
                    rawOutput += connection.getNeuronInput().getOutput() * connection.getWeight();
                }

                neuron.setOutput(neuron.getLayer().getActivationFunction().getSigmoid(rawOutput));
            }
        }
    }

    private void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getNeuralStructure().getLayers().get(neuralNetwork.getNeuralStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutputError(Math.pow(correctAnswer.get(i) - neurons.get(i).getOutput(), 2));
            System.out.println("Neuron " + neurons.get(i).getNumber() + " / wyjście: " + neurons.get(i).getOutput() + " / błąd: " + neurons.get(i).getOutputError() + " / oczekiwane: " + correctAnswer.get(i));
        }
    }

    private void countHiddenLayersError() {
        List<Layer> layers = neuralNetwork.getNeuralStructure().getLayers();

        for (int i = layers.size() - 2; i >= 0; i--) {

            for (Neuron neuron : layers.get(i).getNeurons()) {
                double outputError = 0d;

                for (Connection connection : neuron.getConnectionsOutput()) {
                    outputError += connection.getNeuronOutput().getOutputError() * connection.getWeight();
                }

                neuron.setOutputError(outputError);
                System.out.println("Neuron " + neuron.getNumber() + " / wyjście: " + neuron.getOutput() + " / błąd: " + neuron.getOutputError());
            }
        }
    }

    private void modifyWeights() {

    }

}
