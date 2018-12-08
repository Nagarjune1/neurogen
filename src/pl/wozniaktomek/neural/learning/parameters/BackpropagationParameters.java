package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.neural.structure.Structure;

public class BackpropagationParameters extends LearningParameters {
    private Double learningFactor;
    private Double SSE; // sum of squared errors
    private Double totalError;
    private Integer objectsOutOfTolerance;

    private Boolean isRecordsMixing;

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

    public Double getTotalError() {
        return totalError;
    }

    public Integer getObjectsOutOfTolerance() {
        return objectsOutOfTolerance;
    }

    public Boolean getRecordsMixing() {
        return isRecordsMixing;
    }

    /* Setters */
    public void setLearningFactor(Double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public void setSSE(Double SSE) {
        this.SSE = SSE;
    }

    public void setTotalError(Double totalError) {
        this.totalError = totalError;
    }

    public void setObjectsOutOfTolerance(Integer objectsOutOfTolerance) {
        this.objectsOutOfTolerance = objectsOutOfTolerance;
    }

    public void setRecordsMixing(Boolean recordsMixing) {
        isRecordsMixing = recordsMixing;
    }
}
