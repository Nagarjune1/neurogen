package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.neural.structure.Structure;

public class BackpropagationParameters extends LearningParameters {
    private Double learningFactor;
    private Double SSE; // sum of squared errors

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

    /* Setters */
    public void setLearningFactor(Double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public void setSSE(Double SSE) {
        this.SSE = SSE;
    }
}
