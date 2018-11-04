package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.layout.widget.LearningWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Learning {
    private NeuralNetwork neuralNetwork;

    /* Learning methods */
    private LearningMethod learningMethod;
    private GeneticLearning geneticLearning;
    private Backpropagation backpropagation;

    /* General parameters */
    private Integer iterationsAmount;
    private Double learningTolerance;

    /* Executor */
    private ExecutorService executorService;

    /* Interface */
    private LearningWidget learningWidget;

    public Learning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        executorService = Executors.newSingleThreadExecutor();

        iterationsAmount = 10000;
        learningTolerance = 0.2;
        createLearning(LearningMethod.GENETIC);
    }

    /* General initialization */
    public void createLearning(LearningMethod learningMethod) {
        this.learningMethod = learningMethod;

        switch (learningMethod) {
            case GENETIC:
                geneticLearning = new GeneticLearning(neuralNetwork);
                break;

            case BACKPROPAGATION:
                backpropagation = new Backpropagation(neuralNetwork);
                break;
        }
    }

    /* Backpropagation initialization */
    public void setLearningFactor(Double learningFactor) {
        backpropagation.setLearningParameters(learningFactor);
    }

    /* Control */
    public void startLearning() {
        switch (learningMethod) {
            case GENETIC:
                geneticLearning.setEndingConditions(iterationsAmount, learningTolerance);
                executorService.submit(geneticLearning);
                break;

            case BACKPROPAGATION:
                backpropagation.setEndingConditions(iterationsAmount, learningTolerance);
                executorService.submit(backpropagation);
                break;
        }
    }

    public void stopLearning() {
        switch (learningMethod) {
            case GENETIC:
                geneticLearning.stopLearning();
                break;

            case BACKPROPAGATION:
                backpropagation.stopLearning();
                break;
        }
    }

    /* Interface */
    public void setLearningWidget(LearningWidget learningWidget) {
        this.learningWidget = learningWidget;
    }

    public LearningWidget getLearningWidget() {
        return learningWidget;
    }

    /* Getters */
    public LearningMethod getLearningMethod() {
        return learningMethod;
    }

    public Integer getIterationsAmount() {
        return iterationsAmount;
    }

    public void setIterationsAmount(Integer iterationsAmount) {
        this.iterationsAmount = iterationsAmount;
    }

    public Double getLearningTolerance() {
        return learningTolerance;
    }

    public void setLearningTolerance(Double learningTolerance) {
        this.learningTolerance = learningTolerance;
    }

    /* Learning method */
    public enum LearningMethod {
        GENETIC, BACKPROPAGATION
    }
}
