package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;

public class StartupService {
    private NeuralNetwork neuralNetwork;
    private Structure structure;

    public StartupService(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        structure = neuralNetwork.getStructure();
    }

    public List<Neuron> getLastLayerNeurons(NeuralObject neuralObject) {
        LearningService learningService = new LearningService(neuralNetwork);
        learningService.putInputData(neuralObject);
        learningService.countOutputs();
        learningService.countLastLayerError(neuralObject);

        return structure.getLayers().get(structure.getLayers().size() - 1).getNeurons();
    }

    public Double getLastLayerError(NeuralObject neuralObject) {
        List<Neuron> lastLayerNeurons = getLastLayerNeurons(neuralObject);

        double lastLayerError = 0d;
        for (Neuron neuron : lastLayerNeurons) {
            lastLayerError += neuron.getOutputError();
        }

        return lastLayerError;
    }

    public Double getTotalError(ArrayList<NeuralObject> learningData) {
        double totalError = 0d;

        for (NeuralObject neuralObject : learningData) {
            totalError += getLastLayerError(neuralObject);
        }

        return totalError;
    }

    public Integer getObjectsOutOfTolerance(ArrayList<NeuralObject> learningData) {
        Integer objectCounter = 0;

        for (NeuralObject neuralObject : learningData) {
            if (getLastLayerError(neuralObject) > neuralNetwork.getLearning().getLearningTolerance()) {
                objectCounter++;
            }
        }

        return objectCounter;
    }
}
