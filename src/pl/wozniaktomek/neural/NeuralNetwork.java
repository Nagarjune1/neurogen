package pl.wozniaktomek.neural;

import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.neural.util.Parameters;

import java.util.ArrayList;

public class NeuralNetwork {
    private Structure structure;
    private Parameters parameters;
    private Learning learning;

    private Boolean isLearned;

    private NeuralControl neuralControl;

    public NeuralNetwork() {
        structure = new Structure(this);
        parameters = new Parameters(this);
        learning = new Learning(this);
        isLearned = false;
    }

    public boolean loadLearningData(ArrayList<NeuralObject> learningData) {
        return parameters.setLearningData(learningData);
    }

    /* Learning */
    public void createLearning(Learning.LearningMethod learningMethod) {
        learning.createLearning(learningMethod);
    }

    public void startLearning() {
        learning.startLearning();
    }

    public void stopLearning() {
        learning.stopLearning();
    }

    /* Interface control */
    public void setNeuralControl(NeuralControl neuralControl) {
        this.neuralControl = neuralControl;
    }

    /* Getters */
    public Structure getStructure() {
        return structure;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public Learning getLearning() {
        return learning;
    }

    public Boolean getLearned() {
        return isLearned;
    }

    /* Setter */
    public void setLearned(Boolean learned) {
        isLearned = learned;
        neuralControl.getStartupWidget().refreshWidget();
        neuralControl.getTestWidget().refreshWidget();
    }


}
