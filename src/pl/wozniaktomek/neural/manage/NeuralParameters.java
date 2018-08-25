package pl.wozniaktomek.neural.manage;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.NeuralObject;
import pl.wozniaktomek.neural.operation.NeuralValidation;

import java.util.ArrayList;

public class NeuralParameters {
    private NeuralNetwork neuralNetwork;

    private ArrayList<NeuralObject> objectsLearning;
    private ArrayList<NeuralObject> objectsTesting;

    private Integer inputSize;
    private Integer outputSize;

    public NeuralParameters(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        inputSize = outputSize = 0;
    }

    public boolean setObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        NeuralValidation neuralValidation = new NeuralValidation(this);

        if (neuralValidation.validateObjects(objectsLearning, objectsTesting)) {
            approveData(objectsLearning, objectsTesting);
            return true;
        } else {
            denyData();
            return false;
        }
    }

    public void setInputSize(Integer inputSize) {
        this.inputSize = inputSize;
    }

    public Integer getInputSize() {
        return inputSize;
    }

    public void setOutputSize(Integer outputSize) {
        this.outputSize = outputSize;
    }

    public Integer getOutputSize() {
        return outputSize;
    }

    private void approveData(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        this.objectsLearning = objectsLearning;
        this.objectsTesting = objectsTesting;

        neuralNetwork.getNeuralStructure().addLayer(inputSize);
        neuralNetwork.getNeuralStructure().addLayer(outputSize);
    }

    private void denyData() {
        objectsLearning = null;
        objectsTesting = null;
        inputSize = outputSize = 0;
        neuralNetwork.getNeuralStructure().clearStructure();
    }

}
