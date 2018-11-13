package pl.wozniaktomek.neural.structure;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private Layer layer;
    private Integer number;

    private List<Connection> connectionsInput;
    private List<Connection> connectionsOutput;

    private Double input;
    private Double output;
    private Double outputError;
    private Double errorSignal;

    Neuron(Layer layer) {
        this.layer = layer;
        connectionsInput = new ArrayList<>();
        connectionsOutput = new ArrayList<>();
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setInput(Double input) {
        this.input = input;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    public void setOutputError(Double outputError) {
        this.outputError = outputError;
    }

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

    public Double getOutputSigmoidDerivative() {
        return output * (1 - output);
    }

    public Double getErrorSignal() {
        return errorSignal;
    }

    public void setErrorSignal(Double errorSignal) {
        this.errorSignal = errorSignal;
    }

    /* just for debug */
    private void showConnections() {
        System.out.println("\n # NEURON [" + number + "]");

        System.out.println("Połączenia wchodzące (" + connectionsInput.size() + ")");
        for (Connection connection : connectionsInput) {
            System.out.println("    połączenie z neuronem: " + connection.getNeuronInput().getNumber() + " o wyjsciu " + connection.getNeuronInput().getOutput());
        }

        System.out.println("Połączenia wychodzące (" + connectionsOutput.size() + ")");
        for (Connection connection : connectionsOutput) {
            System.out.println("    połączenie z neuronem: " + connection.getNeuronOutput().getNumber());
        }
    }
}
