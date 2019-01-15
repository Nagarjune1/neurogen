package pl.wozniaktomek.genetic.util;

import java.util.ArrayList;

public class Chromosome implements Cloneable {
    private Integer[] genome;
    private ArrayList<Double> weights; // chromosome genome values
    private Integer genSize; // size of binary string for single weight

    private Double fitness; // total network error, sum of errors for each object
    private Double distribution;
    private Double percent;

    private Double minRange;
    private Double maxRange;
    private Double maxValue;

    public Chromosome(Integer[] genome, Integer genSize, Double minRange, Double maxRange) {
        this.genome = genome;
        this.genSize = genSize;
        setGenValueRanges(minRange, maxRange);
        decodeGenome();
    }

    /* Genome operations */
    public void decodeGenome() {
        weights = new ArrayList<>();
        ArrayList<Integer[]> genList = getGenList();

        for (Integer[] gen : genList) {
            weights.add(minRange + ((maxRange - minRange) * countGenValue(gen) / maxValue));
        }
    }

    private ArrayList<Integer[]> getGenList() {
        ArrayList<Integer[]> genList = new ArrayList<>();
        int gens = genome.length / genSize;
        int counter = 0;

        while (counter < (gens * genSize)) {
            Integer[] gen = new Integer[genSize];

            for (int i = counter; i < counter + genSize; i++) {
                gen[i % genSize] = genome[i];
            }

            genList.add(gen);
            counter += genSize;
        }

        return genList;
    }

    /* Genome value operations */
    private Double countGenValue(Integer[] gen) {
        double value = 0d;
        double counter = 0d;

        for (int i = genSize - 1; i >= 0; i--) {
            if (gen[i] == 1) {
                value += Math.pow(2, counter);
            }
            counter++;
        }

        return value;
    }

    /* Genome value setter */
    private void setGenValueRanges(Double minRange, Double maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        countGenMaxValue();
    }

    private void countGenMaxValue() {
        maxValue = 0d;
        for (int i = 0; i < genSize; i++) {
            maxValue += (Math.pow(2, i));
        }
    }

    /* Getters */
    public Integer[] getGenome() {
        return genome;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public Double getFitness() {
        return fitness;
    }

    public Double getDistribution() {
        return distribution;
    }

    public Double getPercent() {
        return percent;
    }

    /* Setters */
    public void setGenome(Integer[] genome) {
        this.genome = genome;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public void setDistribution(Double distribution) {
        this.distribution = distribution;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    @Override
    public Chromosome clone() {
        Chromosome clone;

        try {
            clone = (Chromosome)super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

        return clone;
    }
}
