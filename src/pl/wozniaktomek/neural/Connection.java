package pl.wozniaktomek.neural;

public class Connection {
    private Neuron neuronOutput;
    private Neuron neuronInput;

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
}
