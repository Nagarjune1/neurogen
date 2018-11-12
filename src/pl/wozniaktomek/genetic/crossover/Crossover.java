package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Crossover {
    ArrayList<Chromosome> population;
    Double probability;

    protected abstract void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome, Integer genIndex);

    void crossPopulation() {
        for (int i = 0; i < population.size(); i++) {
            if (ThreadLocalRandom.current().nextDouble(0d, 1d) <= probability) {
                crossChromosomes();
            }
        }
    }

    private void crossChromosomes() {
        Chromosome firstChromosome = selectRandomChromosome();
        Chromosome secondsChromosome = selectRandomChromosome();

        for (int i = 0; i < firstChromosome.getGenome().size(); i++) {
            if (ThreadLocalRandom.current().nextDouble(0d, 1d) <= probability) {
                crossGen(firstChromosome, secondsChromosome, i);
            }
        }

        firstChromosome.decodeGenome();
        secondsChromosome.decodeGenome();
    }

    private Chromosome selectRandomChromosome() {
        return population.get(ThreadLocalRandom.current().nextInt(0, population.size()));
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
