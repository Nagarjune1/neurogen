package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.neural.structure.Structure;

public class BackpropagationParameters extends LearningParameters {
    private Double learningFactor;
    private Double error;

    public BackpropagationParameters(Structure structure) {
        setStructure(structure);
    }

    /* Getters */
    public Double getLearningFactor() {
        return learningFactor;
    }

    public Double getError() {
        return error;
    }

    /* Setters */
    public void setLearningFactor(Double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public void setError(Double error) {
        this.error = error;
    }
}
