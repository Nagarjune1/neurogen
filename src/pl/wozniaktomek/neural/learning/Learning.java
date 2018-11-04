package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;

public abstract class Learning extends Thread {
    protected NeuralNetwork neuralNetwork;

    /* Data objects */
    ArrayList<NeuralObject> learningData;

    /* Status parameters */
    Boolean isLearning;
    Integer iteration;
    Double error;

    /* Ending parameters */
    private Integer iterationsAmount;
    private Double learningTolerance;

    /* Control */
    public abstract void run();
    public abstract void startLearning();
    public abstract void stopLearning();

    /* Learning */
    abstract void learning();

    /* Initialization */
    void initializeEndingParameters() {
        iterationsAmount = 10000;
        learningTolerance = 0.2;
    }

    /* Ending parameters */
    public void setEndingIterations(Integer maxIterations) {
        this.iterationsAmount = maxIterations;
    }

    public void setEndingLearningTolerance(Double learningTolerance) {
        this.learningTolerance = learningTolerance;
    }

    boolean conditions() {
        return isIteration() && isTolerance();
    }

    private boolean isIteration() {
        return iteration < iterationsAmount;
    }

    private boolean isTolerance() {
        return true;
    }

    /* Getters */
    public Integer getIterationsAmount() {
        return iterationsAmount;
    }

    public Double getLearningTolerance() {
        return learningTolerance;
    }

    public Integer getIteration() {
        return iteration;
    }

    public Double getError() {
        return error;
    }

    /* Learning method */
    public enum LearningMethod {GENETIC, BACKPROPAGATION}
}
