package pl.wozniaktomek.genetic.selection;

import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.structure.Structure;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Selection {
    NeuralNetwork neuralNetwork;
    Structure structure;

    ArrayList<Chromosome> oldPopulation;
    ArrayList<Chromosome> newPopulation;

    protected abstract void initializeSelection();

    protected abstract void selectPopulation();

    /* Fitness operations */
    void countFitness() {
        for (Chromosome chromosome : oldPopulation) {
            chromosome.setFitness(countNetworkError(chromosome.getWeights()));
        }
    }

    private Double countNetworkError(ArrayList<Double> weightsValues) {
        ArrayList<NeuralObject> learningData = neuralNetwork.getParameters().getLearningData();
        setConnectionsWeights(weightsValues);

        double error = 0d;
        for (NeuralObject neuralObject : learningData) {
            error += startupNetwork(neuralObject);
        }

        return error;
    }

    private Double startupNetwork(NeuralObject neuralObject) {
        StartupService startupService = new StartupService(neuralNetwork);
        return startupService.getLastLayerError(neuralObject);
    }

    private void setConnectionsWeights(ArrayList<Double> weightValues) {
        List<Connection> connections = structure.getConnections();

        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setWeight(weightValues.get(i));
        }
    }

    /* Sorting */
    void sortPopulation(ArrayList<Chromosome> population) {
        try {
            population.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    /* Getter */
    public ArrayList<Chromosome> getPopulation() {
        return newPopulation;
    }
}
