package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CrossEvenly extends Crossover {
    public CrossEvenly(ArrayList<Chromosome> population, Double probability) {
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

        Integer[] crossPattern = generateCrossPattern(genomeLength);

        for (int i = 0; i < genomeLength; i++) {
            if (crossPattern[i].equals(1)) {
                newFirstGenome[i] = firstGenome[i];
                newSecondGenome[i] = secondGenome[i];
            } else {
                newFirstGenome[i] = secondGenome[i];
                newSecondGenome[i] = firstGenome[i];
            }
        }

        firstChromosome.setGenome(newFirstGenome);
        secondChromosome.setGenome(newSecondGenome);
    }

    private Integer[] generateCrossPattern(Integer genomeLength) {
        Integer[] crossPattern = new Integer[genomeLength];

        for (int i = 0; i < crossPattern.length; i++) {
            if (ThreadLocalRandom.current().nextInt(0, 100) > 50) {
                crossPattern[i] = 1;
            } else {
                crossPattern[i] = 0;
            }
        }

        return crossPattern;
    }
}
