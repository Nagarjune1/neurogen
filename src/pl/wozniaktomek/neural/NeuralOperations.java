package pl.wozniaktomek.neural;

import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;

import java.util.ArrayList;
import java.util.List;

class NeuralOperations {
    private List<Layer> layers;
    private List<Connection> connections;

    NeuralOperations(List<Layer> layers, List<Connection> connections) {
        this.layers = layers;
        this.connections = connections;
    }

    boolean validateObjects(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        return validateEmptiness(objectsLearning, objectsTesting) &&
                validateSize(objectsLearning, objectsTesting) &&
                validateInputSize(objectsLearning, objectsTesting) &&
                validateClassAmount(objectsLearning, objectsTesting);
    }

    private boolean validateEmptiness(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        return objectsLearning != null && objectsTesting != null;
    }

    private boolean validateSize(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        return objectsLearning.size() > 0 && objectsTesting.size() > 0;
    }

    private boolean validateInputSize(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        int inputSize = objectsLearning.get(0).getInputValues().size();

        for (NeuralObject neuralObject : objectsLearning) {
            if (neuralObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        for (NeuralObject neuralObject : objectsTesting) {
            if (neuralObject.getInputValues().size() != inputSize) {
                return false;
            }
        }

        return true;
    }

    private boolean validateClassAmount(ArrayList<NeuralObject> objectsLearning, ArrayList<NeuralObject> objectsTesting) {
        int highestLearningClass = 0;
        int highestTestingClass = 0;

        for (NeuralObject neuralObject : objectsLearning) {
            if (neuralObject.getClassNumber() > highestLearningClass) {
                highestLearningClass = neuralObject.getClassNumber();
            }
        }

        for (NeuralObject neuralObject : objectsTesting) {
            if (neuralObject.getClassNumber() > highestTestingClass) {
                highestTestingClass = neuralObject.getClassNumber();
            }
        }

        return highestLearningClass == highestTestingClass;
    }

    void createConnectionsWithBias() {
        for (int i = 0; i < layers.size() - 2; i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                List<Neuron> nextNeurons = layers.get(i + 1).getNeurons();
                for (int k = 0; k < nextNeurons.size() - 1; k++) {
                    createConnection(neuron, nextNeurons.get(k));
                }
            }
        }

        for (Neuron neuron : layers.get(layers.size() - 2).getNeurons()) {
            for (Neuron nextNeuron : layers.get(layers.size() - 1).getNeurons()) {
                createConnection(neuron, nextNeuron);
            }
        }
    }

    void createConnectionsWithoutBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            for (Neuron neuron : layers.get(i).getNeurons()) {
                for (Neuron nextNeuron : layers.get(i + 1).getNeurons()) {
                    createConnection(neuron, nextNeuron);
                }
            }
        }
    }

    private void createConnection(Neuron neuronFrom, Neuron neuronTo) {
        Connection connection = new Connection();
        connection.setNeuronInput(neuronFrom);
        connection.setNeuronOutput(neuronTo);

        neuronFrom.getConnectionsOutput().add(connection);
        neuronTo.getConnectionsInput().add(connection);

        connections.add(connection);
    }
}
