package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.neural.structure.Structure;

public class BackpropagationParameters extends LearningParameters {
    private Double learningFactor;
    private Double SSE; // sum of squared errors
    private Double totalEror;

    public BackpropagationParameters(Structure structure) {
        setStructure(structure);
    }

    /* Getters */
    public Double getLearningFactor() {
        return learningFactor;
    }

    public Double getSSE() {
        return SSE;
    }

    public Double getTotalEror() {
        return totalEror;
    }

    /* Setters */
    public void setLearningFactor(Double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public void setSSE(Double SSE) {
        this.SSE = SSE;
    }

    public void setTotalEror(Double totalEror) {
        this.totalEror = totalEror;
    }
}
