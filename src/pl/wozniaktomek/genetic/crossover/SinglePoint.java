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
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome, Integer genIndex) {
        String firstGen = firstChrmosome.getGenome().get(genIndex);
        String secondGen = secondChromosome.getGenome().get(genIndex);

        int point = ThreadLocalRandom.current().nextInt(1, firstGen.length());

        String newFirstGen = firstGen.substring(0, point) + secondGen.substring(point, firstGen.length());
        String newSecondGen = secondGen.substring(0, point) + firstGen.substring(point, secondGen.length());

        firstChrmosome.getGenome().set(genIndex, newFirstGen);
        secondChromosome.getGenome().set(genIndex, newSecondGen);
    }
}
