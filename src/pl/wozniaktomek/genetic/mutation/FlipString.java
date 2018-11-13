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
        String newGenome = chromosome.getfullGenome();
        newGenome = newGenome.replace("1", "0");
        chromosome.setFullGenome(newGenome);
    }
}
