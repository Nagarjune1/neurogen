package pl.wozniaktomek.genetic.mutation;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FlipBit extends Mutation {
    public FlipBit(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        char[] newGenome = chromosome.getfullGenome().toCharArray();

        for (int i = 0; i < newGenome.length; i++) {
            if (ThreadLocalRandom.current().nextDouble(0d, 1d) <= probability) {
                if (newGenome[i] == '1') {
                    newGenome[i] = '0';
                } else {
                    newGenome[i] = '1';
                }
            }
        }

        chromosome.setFullGenome(new String(newGenome));
    }
}
