package pl.wozniaktomek.neural.util;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.neural.service.ParametersService;
import pl.wozniaktomek.neural.service.ValidationService;

import java.util.ArrayList;

public class Parameters {
    private NeuralNetwork neuralNetwork;

    /* Learning data parameters */
    private ArrayList<NeuralObject> learningData;
    private Learning.LearningMethod learningMethod;

    /* Structure parameters */
    private Integer inputSize;
    private Integer outputSize;

    public Parameters(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learningData = null;
        learningMethod = null;
        inputSize = outputSize = 0;
    }

    public boolean setLearningData(ArrayList<NeuralObject> learningData) {
        ValidationService validationService = new ValidationService(this);
        ParametersService parametersService = new ParametersService(neuralNetwork);

        if (validationService.validateObjects(learningData)) {
            parametersService.approveData(learningData);
            this.learningData = learningData;
            return true;
        } else {
            parametersService.denyData();
            this.learningData = null;
            return false;
        }
    }

    public void setInputSize(Integer inputSize) {
        this.inputSize = inputSize;
    }

    public void setOutputSize(Integer outputSize) {
        this.outputSize = outputSize;
    }

    public ArrayList<NeuralObject> getLearningData() {
        return learningData;
    }

    public Integer getInputSize() {
        return inputSize;
    }

    public Integer getOutputSize() {
        return outputSize;
    }
}
