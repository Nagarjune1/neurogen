package pl.wozniaktomek.genetic.crossover;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class DoublePoint extends Crossover {
    public DoublePoint(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
    }

    @Override
    protected void crossGen(Chromosome firstChrmosome, Chromosome secondChromosome) {
        Integer[] firstGenome = firstChrmosome.getGenome();
        Integer[] secondGenome = secondChromosome.getGenome();

        int point1 = ThreadLocalRandom.current().nextInt(0,firstGenome.length);
        int point2 = ThreadLocalRandom.current().nextInt(0, firstGenome.length);

        if (point2 < point1) {
            int tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        Integer[] newFirstGenome = Stream.concat(
                Arrays.stream(Arrays.copyOfRange(firstGenome, 0, point1)),
                Arrays.stream(Stream.concat(
                        Arrays.stream(Arrays.copyOfRange(secondGenome, point1, point2)),
                        Arrays.stream(Arrays.copyOfRange(firstGenome, point2, firstGenome.length)))
                        .toArray(Integer[]::new)))
                .toArray(Integer[]::new);

        Integer[] newSecondGenome = Stream.concat(
                Arrays.stream(Arrays.copyOfRange(secondGenome, 0, point1)),
                Arrays.stream(Stream.concat(
                        Arrays.stream(Arrays.copyOfRange(firstGenome, point1, point2)),
                        Arrays.stream(Arrays.copyOfRange(secondGenome, point2, secondGenome.length)))
                        .toArray(Integer[]::new)))
                .toArray(Integer[]::new);

        firstChrmosome.setGenome(newFirstGenome);
        secondChromosome.setGenome(newSecondGenome);
    }
}
