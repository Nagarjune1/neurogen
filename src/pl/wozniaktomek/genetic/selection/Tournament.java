package pl.wozniaktomek.genetic.selection;

import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Structure;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Tournament extends Selection {
    private Integer tournamentSize;

    public Tournament(ArrayList<Chromosome> oldPopulation, Integer tournamentSize, NeuralNetwork neuralNetwork) {
        this.oldPopulation = oldPopulation;
        this.tournamentSize = tournamentSize;
        this.neuralNetwork = neuralNetwork;
        structure = neuralNetwork.getStructure();

        initializeSelection();
        selectPopulation();
    }

    @Override
    protected void initializeSelection() {
        countFitness();
    }

    @Override
    protected void selectPopulation() {
        newPopulation = new ArrayList<>();

        for (int i = 0; i < oldPopulation.size(); i++) {
            newPopulation.add(makeTournament());
        }
    }

    /* Tournament methods */
    private Chromosome makeTournament() {
        ArrayList<Chromosome> tmpPopulation = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            tmpPopulation.add(selectChromosome());
        }

        sortPopulation(tmpPopulation);

        return tmpPopulation.get(tmpPopulation.size() - 1);
    }

    private Chromosome selectChromosome() {
        return oldPopulation.get(ThreadLocalRandom.current().nextInt(0, oldPopulation.size())).clone();
    }
}
