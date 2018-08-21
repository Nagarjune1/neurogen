package pl.wozniaktomek.neural.structure;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private Integer number;
    private List<Connection> connectionsInput;
    private List<Connection> connectionsOutput;

    Neuron() {
        number = 0;
        connectionsInput = new ArrayList<>();
        connectionsOutput = new ArrayList<>();
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
}
