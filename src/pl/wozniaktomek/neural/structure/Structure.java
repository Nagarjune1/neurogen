package pl.wozniaktomek.neural.structure;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.ActivationFunction;
import pl.wozniaktomek.neural.service.ConnectionService;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    private NeuralNetwork neuralNetwork;

    private List<Layer> layers;
    private List<Connection> connections;

    private Boolean isBias;

    public Structure(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        clearStructure();
    }

    public void clearStructure() {
        layers = new ArrayList<>();
        connections = new ArrayList<>();
        isBias = false;
    }

    public void addLayer(Integer numberOfNeurons) {
        Layer newLayer = new Layer(numberOfNeurons, new ActivationFunction(ActivationFunction.FunctionType.SIGMOID));

        if (layers.size() >= 2) {
            Layer lastLayer = layers.get(layers.size() - 1);
            layers.remove(lastLayer);

            layers.add(newLayer);
            layers.add(lastLayer);
        } else {
            layers.add(newLayer);
        }

        createConnections();
    }

    public void deleteLayer(Integer layerNumber) {
        layers.remove(layerNumber.intValue());
    }

    public void addBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).addNeuron();
        }

        isBias = true;
        createConnections();
    }

    public void deleteBias() {
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i).getNeurons().remove(layers.get(i).getNeurons().size() - 1);
        }

        isBias = false;
        createConnections();
    }

    public void createConnections() {
        setNeuronNumbers();

        ConnectionService connectionService = new ConnectionService(neuralNetwork);
        if (layers.size() > 1) {
            if (isBias) {
                connectionService.createConnectionsWithBias();
            } else {
                connectionService.createConnectionsWithoutBias();
            }
        }
    }

    private void setNeuronNumbers() {
        int number = 0;
        for (Layer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                neuron.setNumber(++number);
            }
        }
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Boolean isBias() {
        return isBias;
    }
}
