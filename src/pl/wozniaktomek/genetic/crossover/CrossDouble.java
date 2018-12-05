package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class CrossDouble extends Crossover {
    public CrossDouble(ArrayList<Chromosome> population, Double probability) {
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

        int point1 = ThreadLocalRandom.current().nextInt(0,firstGenome.length);
        int point2 = ThreadLocalRandom.current().nextInt(0, firstGenome.length);

        if (point2 < point1) {
            int tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        for (int i = 0; i < point1; i++) {
            newFirstGenome[i] = firstGenome[i];
            newSecondGenome[i] = secondGenome[i];
        }

        for (int i = point1; i < point2; i++) {
            newFirstGenome[i] = secondGenome[i];
            newSecondGenome[i] = firstGenome[i];
        }

        for (int i = point2; i < genomeLength; i++) {
            newFirstGenome[i] = firstGenome[i];
            newSecondGenome[i] = secondGenome[i];
        }

        firstChromosome.setGenome(newFirstGenome);
        secondChromosome.setGenome(newSecondGenome);
    }
}
