package pl.wozniaktomek.neural.learning.parameters;

import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.learning.GeneticAlgorithm;
import pl.wozniaktomek.neural.structure.Structure;

import java.util.ArrayList;

public class GeneticParameters extends LearningParameters {
    private ArrayList<Chromosome> population;

    private GeneticAlgorithm.CrossoverMethod crossoverMethod;
    private GeneticAlgorithm.MutationMethod mutationMethod;
    private GeneticAlgorithm.SelectionMethod selectionMethod;

    private Double crossoverProbability;
    private Double mutationProbability;

    private Integer populationSize;
    private Integer genSize;

    public GeneticParameters(Structure structure) {
        setStructure(structure);
    }

    /* Getters */
    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

    public GeneticAlgorithm.CrossoverMethod getCrossoverMethod() {
        return crossoverMethod;
    }

    public GeneticAlgorithm.MutationMethod getMutationMethod() {
        return mutationMethod;
    }

    public GeneticAlgorithm.SelectionMethod getSelectionMethod() {
        return selectionMethod;
    }

    public Double getCrossoverProbability() {
        return crossoverProbability;
    }

    public Double getMutationProbability() {
        return mutationProbability;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public Integer getGenSize() {
        return genSize;
    }

    /* Setters*/
    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }

    public void setCrossoverMethod(GeneticAlgorithm.CrossoverMethod crossoverMethod) {
        this.crossoverMethod = crossoverMethod;
    }

    public void setMutationMethod(GeneticAlgorithm.MutationMethod mutationMethod) {
        this.mutationMethod = mutationMethod;
    }

    public void setSelectionMethod(GeneticAlgorithm.SelectionMethod selectionMethod) {
        this.selectionMethod = selectionMethod;
    }

    public void setCrossoverProbability(Double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setMutationProbability(Double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public void setGenSize(Integer genSize) {
        this.genSize = genSize;
    }
}
