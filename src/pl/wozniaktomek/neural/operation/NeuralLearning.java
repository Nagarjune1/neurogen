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
                    putInputData(neuralObject);
                    countOutputs();

                    countErrors(neuralObject);
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
    }

    private void initializeConnectionWeights() {
        int number = 1;

        for (Connection connection : neuralNetwork.getNeuralStructure().getConnections()) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(-1.0, 1.0));

            // System.out.println("Połączenie [" + (number++) + "] ma wagę " + connection.getWeight());
        }
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
                neuron.countOutput();
            }
        }
    }

    private void countErrors(NeuralObject neuralObject) {
        countLastLayerError(neuralObject);
        countHiddenLayersError(neuralObject);
    }

    private void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getNeuralStructure().getLayers().get(neuralNetwork.getNeuralStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutputError(correctAnswer.get(i) - neurons.get(i).getOutput());
            // System.out.println("Wyjscie: " + neurons.get(i).getOutput() + " / wartość oczekiwana: " + correctAnswer.get(i) + " / błąd: " + neurons.get(i).getOutputError());
        }
    }

    private void countHiddenLayersError(NeuralObject neuralObject) {

    }

}
