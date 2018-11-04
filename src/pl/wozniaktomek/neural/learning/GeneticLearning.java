package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;

public class GeneticLearning extends Learning {
    public GeneticLearning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learningData = neuralNetwork.getParameters().getLearningData();
        initializeEndingParameters();
    }

    /* Control */
    @Override
    public void run() {
        isLearning = true;
        startLearning();
    }

    @Override
    public void startLearning() {
        initializeGeneticAlgorithm();
        learning();
    }

    @Override
    public void stopLearning() {
        isLearning = false;
    }

    @Override
    void learning() {
    }

    /* Initialization */
    private void initializeGeneticAlgorithm() {

    }
}
