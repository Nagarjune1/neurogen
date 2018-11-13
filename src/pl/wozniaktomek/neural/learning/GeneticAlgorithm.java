package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.genetic.crossover.DoublePoint;
import pl.wozniaktomek.genetic.crossover.SinglePoint;
import pl.wozniaktomek.genetic.mutation.FlipString;
import pl.wozniaktomek.genetic.mutation.FlipBit;
import pl.wozniaktomek.genetic.selection.Roulette;
import pl.wozniaktomek.genetic.selection.Tournament;
import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.parameters.GeneticParameters;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.neural.structure.Connection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm extends Thread {
    private NeuralNetwork neuralNetwork;
    private Learning learning;
    private LearningService learningService;
    private GeneticParameters geneticParameters;

    GeneticAlgorithm(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        learning = neuralNetwork.getLearning();
        learningService = new LearningService(neuralNetwork);
        geneticParameters = new GeneticParameters(neuralNetwork.getStructure());
        initializeBasicParameters();
    }

    /* Initialization */
    void setEndingConditions(Integer iterationsAmount, Double learningTolerance) {
        geneticParameters.setIterationsAmount(iterationsAmount);
        geneticParameters.setLearningTolerance(learningTolerance);
    }

    private void initializeBasicParameters() {
        geneticParameters.setCrossoverMethod(CrossoverMethod.SINGLE);
        geneticParameters.setCrossoverProbability(0.2);

        geneticParameters.setMutationMethod(MutationMethod.FLIPBIT);
        geneticParameters.setMutationProbability(0.1);

        geneticParameters.setSelectionMethod(SelectionMethod.TOURNAMENT);

        geneticParameters.setPopulationSize(100);
        geneticParameters.setGenSize(12);

        geneticParameters.setChromosomeMinRange(-10d);
        geneticParameters.setChromosomeMaxRange(10d);
    }

    private void generatePopulation() {
        ArrayList<Chromosome> population = new ArrayList<>();

        for (int i = 0; i < geneticParameters.getPopulationSize(); i++) {
            ArrayList<String> genome = new ArrayList<>();

            for (int j = 0; j < neuralNetwork.getStructure().getConnections().size(); j++) {
                String gen = "";

                for (int k = 0; k < geneticParameters.getGenSize(); k++) {
                    gen = gen.concat(String.valueOf(ThreadLocalRandom.current().nextInt(0, 2)));
                }

                genome.add(gen);
            }

            population.add(new Chromosome(genome, geneticParameters.getGenSize(), geneticParameters.getChromosomeMinRange(), geneticParameters.getChromosomeMaxRange()));
        }

        geneticParameters.setPopulation(population);
    }

    @Override
    public void run() {
        geneticParameters.setIsLearning(true);
        geneticParameters.setIteration(0);
        geneticParameters.setLearningData(learningService.initializeLearningData());

        generatePopulation();
        learning();
    }

    /* Control */
    void stopLearning() {
        geneticParameters.setIsLearning(false);
    }

    private void learning() {
        while (geneticParameters.getIsLearning() && conditions()) {
            geneticParameters.setIteration(geneticParameters.getIteration() + 1);

            selection();
            crossover();
            mutation();

            updateInterface();
        }

        endLearning();
    }

    /* Genetic operators */
    private void crossover() {
        switch (geneticParameters.getCrossoverMethod()) {
            case SINGLE:
                SinglePoint singlePoint = new SinglePoint(geneticParameters.getPopulation(), geneticParameters.getCrossoverProbability());
                geneticParameters.setPopulation(singlePoint.getPopulation());
                break;

            case DOUBLE:
                DoublePoint doublePoint = new DoublePoint(geneticParameters.getPopulation(), geneticParameters.getCrossoverProbability());
                geneticParameters.setPopulation(doublePoint.getPopulation());
                break;
        }
    }

    private void mutation() {
        switch (geneticParameters.getMutationMethod()) {
            case FLIPBIT:
                FlipBit flipBit = new FlipBit(geneticParameters.getPopulation(), geneticParameters.getMutationProbability());
                geneticParameters.setPopulation(flipBit.getPopulation());
                break;

            case FLIPSTRING:
                FlipString flipString = new FlipString(geneticParameters.getPopulation(), geneticParameters.getMutationProbability());
                geneticParameters.setPopulation(flipString.getPopulation());
                break;
        }
    }

    private void selection() {
        switch (geneticParameters.getSelectionMethod()) {
            case ROULETTE:
                Roulette roulette = new Roulette(geneticParameters.getPopulation(), neuralNetwork);
                geneticParameters.setPopulation(roulette.getPopulation());
                break;

            case TOURNAMENT:
                Tournament tournament = new Tournament(geneticParameters.getPopulation(), 8, neuralNetwork);
                geneticParameters.setPopulation(tournament.getPopulation());
                break;
        }
    }

    /* EndingcConditions */
    private boolean conditions() {
        return iterationConditions();
    }

    private boolean iterationConditions() {
        return geneticParameters.getIteration() < geneticParameters.getIterationsAmount();
    }

    /* interface update */
    private void updateInterface() {
        learning.getLearningWidget().updateInterface(geneticParameters.getIteration(), 0d);
    }

    private void endLearning() {
        try {
            geneticParameters.getPopulation().sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }

        List<Connection> connections = neuralNetwork.getStructure().getConnections();
        Chromosome bestChromosome = geneticParameters.getPopulation().get(0);

        System.out.println("\nEND: " + bestChromosome.getFitness());

        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setWeight(bestChromosome.getWeights().get(i));
        }

        neuralNetwork.setLearned(true);
        learning.getLearningWidget().endLearning();
    }

    /* get parameters */
    public GeneticParameters getGeneticParameters() {
        return geneticParameters;
    }

    /* Operations */
    public enum SelectionMethod {
        ROULETTE, TOURNAMENT
    }

    public enum CrossoverMethod {SINGLE, DOUBLE}

    public enum MutationMethod {FLIPBIT, FLIPSTRING}
}
