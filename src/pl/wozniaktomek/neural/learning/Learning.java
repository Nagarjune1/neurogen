package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.layout.widget.LearningWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Learning {
    private NeuralNetwork neuralNetwork;

    /* Learning methods */
    private LearningMethod learningMethod;
    private GeneticAlgorithm geneticAlgorithm;
    private Backpropagation backpropagation;

    /* General parameters */
    private Integer iterationsAmount;
    private Double learningTolerance;

    /* Executor */
    private ExecutorService executorService;

    /* Interface */
    private LearningWidget learningWidget;
    private Boolean interfaceUpdating;
    private Boolean weightsUpdating;

    private Boolean isInterfaceUpdating;

    public Learning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        executorService = Executors.newSingleThreadExecutor();
        interfaceUpdating = true;
        weightsUpdating = false;

        iterationsAmount = 10000;
        learningTolerance = 0.1;
        createLearning(LearningMethod.GENETIC);
    }

    /* General initialization */
    public void createLearning(LearningMethod learningMethod) {
        this.learningMethod = learningMethod;

        switch (learningMethod) {
            case GENETIC:
                geneticAlgorithm = new GeneticAlgorithm(neuralNetwork);
                break;

            case BACKPROPAGATION:
                backpropagation = new Backpropagation(neuralNetwork);
                break;
        }
    }

    /* Control */
    public void startLearning() {
        switch (learningMethod) {
            case GENETIC:
                geneticAlgorithm.setEndingConditions(iterationsAmount, learningTolerance);
                executorService.submit(geneticAlgorithm);
                break;

            case BACKPROPAGATION:
                backpropagation.setEndingConditions(iterationsAmount, learningTolerance);
                executorService.submit(backpropagation);
                break;
        }

        neuralNetwork.setLearned(false);
    }

    public void stopLearning() {
        switch (learningMethod) {
            case GENETIC:
                geneticAlgorithm.stopLearning();
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

    public Boolean getInterfaceUpdating() {
        return interfaceUpdating;
    }

    public void setInterfaceUpdating(Boolean interfaceUpdating) {
        this.interfaceUpdating = interfaceUpdating;
    }

    public Boolean getWeightsUpdating() {
        return weightsUpdating;
    }

    public void setWeightsUpdating(Boolean weightsUpdating) {
        this.weightsUpdating = weightsUpdating;
    }

    Boolean getIsInterfaceUpdating() {
        return isInterfaceUpdating;
    }

    public void setIsInterfaceUpdating(Boolean updating) {
        isInterfaceUpdating = updating;
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

    public GeneticAlgorithm getGeneticAlgorithm() {
        return geneticAlgorithm;
    }

    public Backpropagation getBackpropagation() {
        return backpropagation;
    }

    /* Learning method */
    public enum LearningMethod {
        GENETIC, BACKPROPAGATION
    }
}
