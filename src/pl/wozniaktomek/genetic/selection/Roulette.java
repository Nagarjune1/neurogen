package pl.wozniaktomek.genetic.selection;

import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Roulette extends Selection {
    private Double sumoOfDistribution;

    public Roulette(ArrayList<Chromosome> oldPopulation, NeuralNetwork neuralNetwork, Double fitnessScalingFactor) {
        this.oldPopulation = oldPopulation;
        this.neuralNetwork = neuralNetwork;
        this.fitnessScalingFactor = fitnessScalingFactor;
        structure = neuralNetwork.getStructure();

        initializeSelection();
        selectPopulation();
    }

    @Override
    protected void initializeSelection() {
        sumoOfDistribution = 0d;

        countFitness();
        sortPopulation(oldPopulation);
        countDistribution();
        countPercent();
        countSumOfPercent();
    }

    @Override
    protected void selectPopulation() {
        newPopulation = new ArrayList<>();
        int counter = 0;

        while (counter != oldPopulation.size()) {
            double roulette = ThreadLocalRandom.current().nextDouble(0d, 100d);

            for (int i = 0; i < oldPopulation.size() - 1; i++) {
                if (roulette >= oldPopulation.get(i).getPercent() && roulette <= oldPopulation.get(i + 1).getPercent()) {
                    if (oldPopulation.get(i + 1).getPercent() < (roulette - oldPopulation.get(i).getPercent())) {
                        newPopulation.add(oldPopulation.get(i).clone());
                    } else {
                        newPopulation.add(oldPopulation.get(i + 1).clone());
                    }

                    counter++;
                }
            }
        }
    }

    /* Roulette wheel methods */
    private void countDistribution() {
        double bestFitness = oldPopulation.get(0).getFitness();

        for (Chromosome chromosome : oldPopulation) {
            chromosome.setDistribution(bestFitness - chromosome.getFitness() + 1.0);
            sumoOfDistribution += chromosome.getDistribution();
        }
    }

    private void countPercent() {
        for (Chromosome chromosome : oldPopulation) {
            chromosome.setPercent((chromosome.getDistribution() / sumoOfDistribution) * 100.0);
        }
    }

    private void countSumOfPercent() {
        double sumOfPercent = oldPopulation.get(0).getPercent();

        for (int i = 1; i < oldPopulation.size(); i++) {
            sumOfPercent += oldPopulation.get(i).getPercent();
            oldPopulation.get(i).setPercent(sumOfPercent);
        }
    }
}
