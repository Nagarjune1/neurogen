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

    Neuron(Layer layer) {
        this.layer = layer;
        connectionsInput = new ArrayList<>();
        connectionsOutput = new ArrayList<>();
    }

    public void countOutput() {
        // showConnections();

        double rawOutput = 0d;
        for (Connection connection : connectionsInput) {
            rawOutput += connection.getNeuronInput().getOutput() * connection.getWeight();
        }

        output = layer.getActivationFunction().getOutput(rawOutput);

        // System.out.println("Neuron [" + number + "] ma na wyjsciu: " + output);
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public List<Connection> getConnectionsInput() {
        return connectionsInput;
    }

    public List<Connection> getConnectionsOutput() {
        return connectionsOutput;
    }

    public void setOutput(Double output) {
        this.output = output;
        // System.out.println("Neuron [" + number + "] dostaje na wejście " + output);
    }

    public Double getOutput() {
        return output;
    }

    public Double getOutputError() {
        return outputError;
    }

    public void setOutputError(Double outputError) {
        this.outputError = outputError;
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
