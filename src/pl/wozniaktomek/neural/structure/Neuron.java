package pl.wozniaktomek.neural.structure;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private Layer layer;
    private Integer number;

    private List<Connection> connectionsInput;
    private List<Connection> connectionsOutput;

    private Double output;
    private Double outputError;
    private Double errorSignal;

    Neuron(Layer layer) {
        this.layer = layer;
        connectionsInput = new ArrayList<>();
        connectionsOutput = new ArrayList<>();
    }

    /* Getters */
    public Layer getLayer() {
        return layer;
    }

    public List<Connection> getConnectionsInput() {
        return connectionsInput;
    }

    public List<Connection> getConnectionsOutput() {
        return connectionsOutput;
    }

    public Integer getNumber() {
        return number;
    }

    public Double getOutput() {
        return output;
    }

    public Double getOutputError() {
        return outputError;
    }

    public Double getErrorSignal() {
        return errorSignal;
    }

    /* Setters */
    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    public void setOutputError(Double outputError) {
        this.outputError = outputError;
    }

    public void setErrorSignal(Double errorSignal) {
        this.errorSignal = errorSignal;
    }
}
