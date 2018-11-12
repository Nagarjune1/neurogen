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
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome, Integer genIndex) {
        String firstGen = firstChrmosome.getGenome().get(genIndex);
        String secondGen = secondChromosome.getGenome().get(genIndex);

        int point1 = ThreadLocalRandom.current().nextInt(0, firstGen.length());
        int point2 = ThreadLocalRandom.current().nextInt(0, firstGen.length());

        if (point2 < point1) {
            int tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        String newFirstGen = firstGen.substring(0, point1) + secondGen.substring(point1, point2) + firstGen.substring(point2);
        String newSecondGen = secondGen.substring(0, point1) + firstGen.substring(point1, point2) + secondGen.substring(point2);

        firstChrmosome.getGenome().set(genIndex, newFirstGen);
        secondChromosome.getGenome().set(genIndex, newSecondGen);
    }
}
