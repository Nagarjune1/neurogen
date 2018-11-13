package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePoint extends Crossover {
    public SinglePoint(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        crossPopulation();
    }

    @Override
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome) {
        String firstGenome = firstChrmosome.getfullGenome();
        String secondGenome = secondChromosome.getfullGenome();

        int point = ThreadLocalRandom.current().nextInt(1, firstGenome.length());

        String newFirstGenome = firstGenome.substring(0, point) + secondGenome.substring(point, firstGenome.length());
        String newSecondGenome = secondGenome.substring(0, point) + firstGenome.substring(point, secondGenome.length());

        firstChrmosome.setFullGenome(newFirstGenome);
        secondChromosome.setFullGenome(newSecondGenome);
    }
}
