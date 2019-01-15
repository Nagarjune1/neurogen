package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;

abstract public class LearningParameters {
    /* References */
    private Structure structure;

    /* Learning data */
    private ArrayList<NeuralObject> learningData;

    /* General learning parameters */
    private Integer iterationsAmount;
    private Double learningTolerance;
    private Boolean isTotalTolerance;

    /* Status parameters */
    private Integer iteration;
    private Boolean isLearning;

    /* Getters */
    public Structure getStructure() {
        return structure;
    }

    public ArrayList<NeuralObject> getLearningData() {
        return learningData;
    }

    public Integer getIterationsAmount() {
        return iterationsAmount;
    }

    public Double getLearningTolerance() {
        return learningTolerance;
    }

    public Boolean getIsTotalTolerance() {
        return isTotalTolerance;
    }

    public Integer getIteration() {
        return iteration;
    }

    public Boolean getIsLearning() {
        return isLearning;
    }

    /* Setters */
    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public void setLearningData(ArrayList<NeuralObject> learningData) {
        this.learningData = learningData;
    }

    public void setIterationsAmount(Integer iterationsAmount) {
        this.iterationsAmount = iterationsAmount;
    }

    public void setLearningTolerance(Double learningTolerance) {
        this.learningTolerance = learningTolerance;
    }

    public void setIsTotalTolerance(Boolean isTotalTolerance) {
        this.isTotalTolerance = isTotalTolerance;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public void setIsLearning(Boolean learning) {
        isLearning = learning;
    }
}
