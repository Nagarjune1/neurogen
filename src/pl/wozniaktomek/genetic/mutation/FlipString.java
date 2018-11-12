package pl.wozniaktomek.genetic.mutation;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FlipString extends Mutation {
    public FlipString(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        ArrayList<String> newGenome = new ArrayList<>();

        for (String gen : chromosome.getGenome()) {
            char[] genCharArray = gen.toCharArray();
            newGenome.add(new String(mutateSingleBit(genCharArray)));
        }

        chromosome.setGenome(newGenome);
    }

    private char[] mutateSingleBit(char[] genCharArray) {
        int point = ThreadLocalRandom.current().nextInt(0, genCharArray.length);

        if (genCharArray[point] == '1') {
            genCharArray[point] = '0';
        } else {
            genCharArray[point] = '1';
        }

        return genCharArray;
    }

    /* Alternative method for testing | counting probability mutation for every bit of gen
    private char[] mutateAllBits(char[] genCharArray) {
        for (int i = 0; i < genCharArray.length; i++) {
            if (ThreadLocalRandom.current().nextDouble(0d, 1d) <= probability) {
                if (genCharArray[i] == '0') {
                    genCharArray[i] = '1';
                } else {
                    genCharArray[i] = '0';
                }
            }
        }
        return genCharArray;
    }
    */
}
