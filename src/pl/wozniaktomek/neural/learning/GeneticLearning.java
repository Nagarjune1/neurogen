package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;

public class GeneticLearning extends Thread {
    private NeuralNetwork neuralNetwork;

    /* Learning parameters */
    private ArrayList<NeuralObject> learningData;

    /* Ending conditions */
    private Integer iterationsAmount;
    private Double learningTolerance;

    /* Status parameters */
    private Integer iteration;
    private Boolean isLearning;

    GeneticLearning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learningData = neuralNetwork.getParameters().getLearningData();
    }

    void setEndingConditions(Integer iterationsAmount, Double learningTolerance) {
        this.iterationsAmount = iterationsAmount;
        this.learningTolerance = learningTolerance;
    }

    /* Control */
    public void startLearning() {

    }

    public void stopLearning() {
        isLearning = false;
    }
}
