package pl.wozniaktomek.genetic.mutation;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;

public class FlipString extends Mutation {
    public FlipString(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        Integer[] newGenome = chromosome.getGenome();

        for (int i = 0; i < newGenome.length; i++) {
            if (newGenome[i] == 1) {
                newGenome[i] = 0;
            } else {
                newGenome[i] = 1;
            }
        }

        chromosome.setGenome(newGenome);
    }
}
