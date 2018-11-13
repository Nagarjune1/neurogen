package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DoublePoint extends Crossover {
    public DoublePoint(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
    }

    @Override
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome) {
        String firstGenome = firstChrmosome.getfullGenome();
        String secondGenome = secondChromosome.getfullGenome();

        int point1 = ThreadLocalRandom.current().nextInt(0,firstGenome.length());
        int point2 = ThreadLocalRandom.current().nextInt(0, firstGenome.length());

        if (point2 < point1) {
            int tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        String newFirstGenome = firstGenome.substring(0, point1) + secondGenome.substring(point1, point2) + firstGenome.substring(point2);
        String newSecondGenome = secondGenome.substring(0, point1) + firstGenome.substring(point1, point2) + secondGenome.substring(point2);

        firstChrmosome.setFullGenome(newFirstGenome);
        secondChromosome.setFullGenome(newSecondGenome);
    }
}
