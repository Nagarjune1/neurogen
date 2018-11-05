package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;

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
}
