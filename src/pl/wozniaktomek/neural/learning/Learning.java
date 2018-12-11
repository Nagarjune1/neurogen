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
    private Boolean learningVisualization;

    private Boolean isNowInterfaceUpdating;
    private Boolean isNowVisualizationUpdating;

    public Learning(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        executorService = Executors.newSingleThreadExecutor();
        interfaceUpdating = true;
        learningVisualization = false;

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

    public Boolean getLearningVisualization() {
        return learningVisualization;
    }

    public void setLearningVisualization(Boolean learningVisualization) {
        this.learningVisualization = learningVisualization;
    }

    public Boolean getIsNowInterfaceUpdating() {
        return isNowInterfaceUpdating;
    }

    public void setIsNowInterfaceUpdating(Boolean updating) {
        isNowInterfaceUpdating = updating;
    }

    public Boolean getIsNowVisualizationUpdating() {
        return isNowVisualizationUpdating;
    }

    public void setIsNowVisualizationUpdating(Boolean nowVisualizationUpdating) {
        isNowVisualizationUpdating = nowVisualizationUpdating;
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
