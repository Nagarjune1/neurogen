package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.neural.util.Parameters;

import java.util.ArrayList;

public class ValidationService {
    private Parameters parameters;

    public ValidationService(Parameters parameters) {
        this.parameters = parameters;
    }

    public boolean validateObjects(ArrayList<NeuralObject> learningData) {
        return validateEmptiness(learningData) &&
                validateSize(learningData) &&
                validateInputSize(learningData) &&
                validateClassAmount(learningData);
    }

    private boolean validateEmptiness(ArrayList<NeuralObject> learningData) {
        return learningData != null;
    }

    private boolean validateSize(ArrayList<NeuralObject> learningData) {
        return learningData.size() > 0;
    }

    private boolean validateInputSize(ArrayList<NeuralObject> learningData) {
        int inputSize = learningData.get(0).getInputValues().size();

        for (NeuralObject neuralObject : learningData) {
            if (neuralObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        parameters.setInputSize(inputSize);
        return inputSize > 0;
    }

    private boolean validateClassAmount(ArrayList<NeuralObject> learningData) {
        int highestClass = 0;

        for (NeuralObject neuralObject : learningData) {
            if (neuralObject.getClassNumber() > highestClass) {
                highestClass = neuralObject.getClassNumber();
            }
        }

        parameters.setOutputSize(highestClass);
        return highestClass > 1;
    }
}
