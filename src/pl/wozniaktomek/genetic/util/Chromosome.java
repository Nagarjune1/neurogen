package pl.wozniaktomek.genetic.util;

import java.util.ArrayList;

public class Chromosome implements Cloneable {
    private ArrayList<String> genome; // chromosome binary genome
    private ArrayList<Double> weights; // chromosome genome values
    private Integer genSize; // size of binary string for single weight

    private Double fitness; // total network error, sum of errors for each object
    private Double distribution;
    private Double percent;

    private Double minRange = -2d;
    private Double maxRange = 2d;
    private Double maxValue;

    public Chromosome(ArrayList<String> genome, Integer genSize) {
        this.genome = genome;
        this.genSize = genSize;
        countGenMaxValue();
        decodeGenome();
    }

    /* Genome operations */
    public void decodeGenome() {
        weights = new ArrayList<>();

        for (String gen : genome) {
            weights.add(minRange + ((maxRange - minRange) * countGenValue(gen) / maxValue));
        }
    }

    /* Genome value operations */
    private Double countGenValue(String gen) {
        double value = 0d;
        double counter = 0d;

        for (int i = 0; i < gen.length(); i++) {
            if (gen.charAt(i) == 1) {
                value += Math.pow(2, counter);
            }
            counter++;
        }

        return value;
    }

    /* Genome value setter */
    public void setGenValueRanges(Double minRange, Double maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
        countGenMaxValue();
    }

    private void countGenMaxValue() {
        maxValue = 0d;
        for (int i = 0; i < genSize; i++) {
            maxValue += (int) (Math.pow(2, i));
        }
    }

    /* Getters */
    public ArrayList<Double> getWeights() {
        return weights;
    }

    public ArrayList<String> getGenome() {
        return genome;
    }

    public Integer getGenSize() {
        return genSize;
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

    public Double getMinRange() {
        return minRange;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    /* Setters */
    public void setGenome(ArrayList<String> genome) {
        this.genome = genome;
        decodeGenome();
    }

    public void setGenSize(Integer genSize) {
        this.genSize = genSize;
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
