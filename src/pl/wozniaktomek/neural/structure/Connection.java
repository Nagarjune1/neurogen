package pl.wozniaktomek.neural.structure;

public class Connection {
    private Neuron neuronOutput;
    private Neuron neuronInput;
    private Double weight;

    /* Getters */
    public Neuron getNeuronOutput() {
        return neuronOutput;
    }

    public Neuron getNeuronInput() {
        return neuronInput;
    }

    public Double getWeight() {
        return weight;
    }

    /* Setters */
    public void setNeuronOutput(Neuron neuronOutput) {
        this.neuronOutput = neuronOutput;
    }

    public void setNeuronInput(Neuron neuronInput) {
        this.neuronInput = neuronInput;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
