package pl.wozniaktomek.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private List<Connection> connectionsInput;
    private List<Connection> connectionsOutput;

    public Neuron() {
        connectionsInput = new ArrayList<>();
        connectionsOutput = new ArrayList<>();
    }

    public List<Connection> getConnectionsInput() {
        return connectionsInput;
    }

    public List<Connection> getConnectionsOutput() {
        return connectionsOutput;
    }
}
