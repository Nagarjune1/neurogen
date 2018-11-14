package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class SinglePoint extends Crossover {
    public SinglePoint(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        crossPopulation();
    }

    @Override
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome) {
        Integer[] firstGenome = firstChrmosome.getGenome();
        Integer[] secondGenome = secondChromosome.getGenome();

        int point = ThreadLocalRandom.current().nextInt(1, firstGenome.length);


        Integer[] newFirstGenome = Stream.concat(
                Arrays.stream(Arrays.copyOfRange(firstGenome, 0, point)),
                Arrays.stream(Arrays.copyOfRange(secondGenome, point, firstGenome.length)))
                .toArray((Integer[]::new));

        Integer[] newSecondGenome = Stream.concat(
                Arrays.stream(Arrays.copyOfRange(secondGenome, 0, point)),
                Arrays.stream(Arrays.copyOfRange(firstGenome, point, secondGenome.length)))
                .toArray((Integer[]::new));

        firstChrmosome.setGenome(newFirstGenome);
        secondChromosome.setGenome(newSecondGenome);
    }
}
