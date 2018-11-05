package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.neural.util.Parameters;

import java.util.ArrayList;

public class ValidationService {
    private Parameters parameters;

    public ValidationService(Parameters parameters) {
        this.parameters = parameters;
    }

    public boolean validateObjects(ArrayList<NeuralObject> neuralObjects) {
        return validateEmptiness(neuralObjects) &&
                validateSize(neuralObjects) &&
                validateInputSize(neuralObjects) &&
                validateClassAmount(neuralObjects);
    }

    private boolean validateEmptiness(ArrayList<NeuralObject> neuralObjects) {
        return neuralObjects != null;
    }

    private boolean validateSize(ArrayList<NeuralObject> neuralObjects) {
        return neuralObjects.size() > 0;
    }

    private boolean validateInputSize(ArrayList<NeuralObject> neuralObjects) {
        int inputSize = neuralObjects.get(0).getInputValues().size();

        for (NeuralObject neuralObject : neuralObjects) {
            if (neuralObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        if (parameters.getInputSize() != 0) {
            return parameters.getInputSize() == inputSize;
        }

        parameters.setInputSize(inputSize);
        return inputSize > 0;
    }

    private boolean validateClassAmount(ArrayList<NeuralObject> neuralObjects) {
        int highestClass = 0;

        for (NeuralObject neuralObject : neuralObjects) {
            if (neuralObject.getClassNumber() > highestClass) {
                highestClass = neuralObject.getClassNumber();
            }
        }

        if (parameters.getOutputSize() != 0) {
            return parameters.getOutputSize() == highestClass;
        }

        parameters.setOutputSize(highestClass);
        return highestClass > 1;
    }
}
