package pl.wozniaktomek.genetic.util;

import java.util.ArrayList;

public class Chromosome implements Cloneable {
    private ArrayList<String> genome; // chromosome binary genome
    private ArrayList<Double> weights; // chromosome genome values
    private Integer genSize; // size of binary string for single weight

    private Double fitness; // total network error, sum of errors for each object
    private Double distribution;
    private Double percent;

    private Double minRange;
    private Double maxRange;
    private Double maxValue;

    public Chromosome(ArrayList<String> genome, Integer genSize, Double minRange, Double maxRange) {
        this.genome = genome;
        this.genSize = genSize;
        setGenValueRanges(minRange, maxRange);
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

        for (int i = genSize - 1; i >= 0; i--) {
            if (gen.charAt(i) == '1') {
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

    /* Full genome operations */
    public String getfullGenome() {
        StringBuilder fullGenome = new StringBuilder();

        for (String string : genome) {
            fullGenome.append(string);
        }

        return fullGenome.toString();
    }

    public void setFullGenome(String fullGenome) {
        genome = new ArrayList<>();

        for (int i = 0; i < weights.size(); i ++) {
            genome.add(fullGenome.substring(genSize * i, genSize * (i + 1)));
        }

        decodeGenome();
    }

    /* Getters */
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
