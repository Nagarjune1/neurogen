package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LearningService {
    private NeuralNetwork neuralNetwork;

    public LearningService(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /* Initialization */
    public ArrayList<NeuralObject> initializeLearningData() {
        return neuralNetwork.getParameters().getLearningData();
    }

    public void initializeBiasOutput() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();
        if (neuralNetwork.getStructure().isBias()) {
            for (int i = 0; i < layers.size() - 1; i++) {
                Layer layer = layers.get(i);
                Neuron biasNeuron = layer.getNeurons().get(layer.getNeurons().size() - 1);
                biasNeuron.setOutput(1d);
            }
        }
    }

    public void initializeConnectionWeights(List<Connection> connections) {
        for (Connection connection : connections) {
            connection.setWeight(ThreadLocalRandom.current().nextDouble(0d, 1d));
        }
    }

    /* Operations */
    /* put new input vector x */
    public void putInputData(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(0).getNeurons();

        if (neuralNetwork.getStructure().isBias()) {
            for (int i = 0; i < neurons.size() - 1; i++) {
                neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
            }
        } else {
            for (int i = 0; i < neurons.size(); i++) {
                neurons.get(i).setOutput(neuralObject.getInputValues().get(i));
            }
        }
    }

    /* count outputs for every neuron */
    public void countOutputs() {
        List<Layer> layers = neuralNetwork.getStructure().getLayers();

        if (!neuralNetwork.getStructure().isBias()) {
            for (int i = 1; i < layers.size(); i++) {
                Layer layer = layers.get(i);

                for (Neuron neuron : layer.getNeurons()) {
                    double net = 0d;

                    for (Connection connection : neuron.getConnectionsInput()) {
                        net += connection.getWeight() * connection.getNeuronInput().getOutput();
                    }

                    neuron.setOutput(neuron.getLayer().getActivationFunction().useFunction(net));
                }
            }
        } else {
            for (int i = 1; i < layers.size() - 1; i++) {
                Layer layer = layers.get(i);

                for (int j = 0; j < layer.getLayerSize() - 1; j++) {
                    Neuron neuron = layer.getNeurons().get(j);
                    double net = 0d;

                    for (Connection connection : neuron.getConnectionsInput()) {
                        net += connection.getWeight() * connection.getNeuronInput().getOutput();
                    }

                    neuron.setOutput(neuron.getLayer().getActivationFunction().useFunction(net));
                }
            }

            for (Neuron neuron : layers.get(layers.size() - 1).getNeurons()) {
                double net = 0d;

                for (Connection connection : neuron.getConnectionsInput()) {
                    net += connection.getWeight() * connection.getNeuronInput().getOutput();
                }

                neuron.setOutput(neuron.getLayer().getActivationFunction().useFunction(net));
            }
        }
    }

    /* count error for neurons in last layer */
    public void countLastLayerError(NeuralObject neuralObject) {
        List<Neuron> neurons = neuralNetwork.getStructure().getLayers().get(neuralNetwork.getStructure().getLayers().size() - 1).getNeurons();
        List<Double> correctAnswer = neuralObject.getCorrectAnswer();

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutputError(Math.pow(correctAnswer.get(i) - neurons.get(i).getOutput(), 2));
            neurons.get(i).setErrorSignal(neurons.get(i).getOutput() * (1d - neurons.get(i).getOutput()) * (correctAnswer.get(i) - neurons.get(i).getOutput()));
        }
    }

    /* interface updates */
    public void updateLearningParameters(Integer iteration, Double error, String objects) {
        neuralNetwork.getLearning().setIsNowInterfaceUpdating(true);
        neuralNetwork.getLearning().getLearningWidget().updateInterface(iteration, error, objects);

        while (neuralNetwork.getLearning().getIsNowInterfaceUpdating()) try {
            Thread.sleep(25);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void updateVisualization() {
        neuralNetwork.getLearning().setIsNowVisualizationUpdating(true);
        neuralNetwork.getLearning().getLearningWidget().drawLearningVisulization();

        while (neuralNetwork.getLearning().getIsNowVisualizationUpdating()) try {
            Thread.sleep(25);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
