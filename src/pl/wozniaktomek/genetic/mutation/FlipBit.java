package pl.wozniaktomek.genetic.mutation;

import pl.wozniaktomek.genetic.util.Chromosome;

import java.util.ArrayList;

public class FlipBit extends Mutation {
    public FlipBit(ArrayList<Chromosome> population, Double probability) {
        this.population = population;
        this.probability = probability;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        ArrayList<String> newGenome = new ArrayList<>();

        for (String gen : chromosome.getGenome()) {
            char[] genCharArray = gen.toCharArray();

            for (int i = 0; i < genCharArray.length; i++) {
                if (genCharArray[i] == '0') {
                    genCharArray[i] = '1';
                } else {
                    genCharArray[i] = '0';
                }
            }

            newGenome.add(new String(genCharArray));
        }

        chromosome.setGenome(newGenome);
    }
}
