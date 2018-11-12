package pl.wozniaktomek.genetic.mutation;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Mutation {
    ArrayList<Chromosome> population;
    Double probability;

    protected abstract void mutateChromosome(Chromosome chromosome);

    void mutatePopulation() {
        for (Chromosome chromosome : population) {
            if (ThreadLocalRandom.current().nextDouble(0.0, 1.0) <= probability) {
                mutateChromosome(chromosome);
            }

            chromosome.decodeGenome();
        }
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
