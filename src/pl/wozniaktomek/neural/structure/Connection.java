package pl.wozniaktomek.neural.structure;

public class Connection {
    private Neuron neuronOutput;
    private Neuron neuronInput;
    private Double weight;

    public Connection() {
    }

    public Neuron getNeuronOutput() {
        return neuronOutput;
    }

    public void setNeuronOutput(Neuron neuronOutput) {
        this.neuronOutput = neuronOutput;
    }

    public Neuron getNeuronInput() {
        return neuronInput;
    }

    public void setNeuronInput(Neuron neuronInput) {
        this.neuronInput = neuronInput;
    }

    Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
