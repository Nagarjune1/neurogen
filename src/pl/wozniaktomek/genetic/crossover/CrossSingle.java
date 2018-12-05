package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class CrossSingle extends Crossover {
    public CrossSingle(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        crossPopulation();
    }

    @Override
    protected void crossGenome(Chromosome firstChromosome, Chromosome secondChromosome) {
        Integer[] firstGenome = firstChromosome.getGenome();
        Integer[] secondGenome = secondChromosome.getGenome();

        int genomeLength = firstGenome.length;

        Integer[] newFirstGenome = new Integer[genomeLength];
        Integer[] newSecondGenome = new Integer[genomeLength];

        int point = ThreadLocalRandom.current().nextInt(1, genomeLength);

        for (int i = 0; i < point; i++) {
            newFirstGenome[i] = firstGenome[i];
            newSecondGenome[i] = secondGenome[i];
        }

        for (int i = point; i < genomeLength; i++) {
            newFirstGenome[i] = secondGenome[i];
            newSecondGenome[i] = firstGenome[i];
        }

        firstChromosome.setGenome(newFirstGenome);
        secondChromosome.setGenome(newSecondGenome);
    }
}
