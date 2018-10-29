package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;

public abstract class Learning extends Thread {
    protected NeuralNetwork neuralNetwork;

    /* Data objects */
    ArrayList<NeuralObject> objectsLearning;
    ArrayList<NeuralObject> objectsTesting;

    /* Status parameters */
    Boolean isLearning;
    Integer iteration;
    Double error;

    /* Ending parameters */
    private Integer maxIterations;
    private Double learningTolerance;

    /* Control */
    public abstract void run();
    public abstract void startLearning();
    public abstract void stopLearning();

    /* Learning */
    abstract void learning();

    /* Initialization */
    public void setEndingParameters(Integer maxIterations, Double learningTolerance) {
        this.maxIterations = maxIterations;
        this.learningTolerance = learningTolerance;
    }

    /* Ending parameters */
    boolean conditions() {
        return isIteration() && isTolerance();
    }

    private boolean isIteration() {
        return iteration < maxIterations;
    }

    private boolean isTolerance() {
        return true;
    }

    /* Getters */
    public Integer getIteration() {
        return iteration;
    }

    public Double getError() {
        return error;
    }

    /* Learning method */
    public enum LearningMethod {GENETIC, BACKPROPAGATION}
}
