package pl.wozniaktomek.neural.learning;

import pl.wozniaktomek.genetic.crossover.CrossDouble;
import pl.wozniaktomek.genetic.crossover.CrossEvenly;
import pl.wozniaktomek.genetic.crossover.CrossSingle;
import pl.wozniaktomek.genetic.mutation.FlipString;
import pl.wozniaktomek.genetic.mutation.FlipBit;
import pl.wozniaktomek.genetic.selection.Roulette;
import pl.wozniaktomek.genetic.selection.Tournament;
import pl.wozniaktomek.genetic.util.Chromosome;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.parameters.GeneticParameters;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.neural.service.StartupService;
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
        geneticParameters.setCrossoverProbability(0.5);

        geneticParameters.setMutationMethod(MutationMethod.FLIPBIT);
        geneticParameters.setMutationProbability(0.01);

        geneticParameters.setSelectionMethod(SelectionMethod.TOURNAMENT);
        geneticParameters.setTournamentSize(8);

        geneticParameters.setPopulationSize(64);
        geneticParameters.setGenSize(8);

        geneticParameters.setChromosomeMinRange(-10d);
        geneticParameters.setChromosomeMaxRange(10d);
    }

    private void generatePopulation() {
        ArrayList<Chromosome> population = new ArrayList<>();
        int genomeSize = neuralNetwork.getStructure().getConnections().size() * geneticParameters.getGenSize();

        for (int i = 0; i < geneticParameters.getPopulationSize(); i++) {
            Integer[] genome = new Integer[genomeSize];

            for (int j = 0; j < genomeSize; j++) {
                genome[j] = ThreadLocalRandom.current().nextInt(0, 2);
            }

            population.add(new Chromosome(genome, geneticParameters.getGenSize(), geneticParameters.getChromosomeMinRange(), geneticParameters.getChromosomeMaxRange()));
        }

        geneticParameters.setPopulation(population);
    }

    @Override
    public void run() {
        geneticParameters.setIsLearning(true);
        geneticParameters.setIteration(0);
        geneticParameters.setTotalError(1d);
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

            countError();

            if (learning.getInterfaceUpdating()) {
                updateIteration();
                updateError();
                updateObjectsOutOfTolerance();
            } else {
                countError();
            }

            if (learning.getWeightsUpdating()) {
                learning.setIsInterfaceUpdating(true);
                updateWeights();

                while (learning.getIsInterfaceUpdating()) try {
                    Thread.sleep(25);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }

        endLearning();
    }

    /* Genetic operators */
    private void crossover() {
        switch (geneticParameters.getCrossoverMethod()) {
            case SINGLE:
                CrossSingle crossSingle = new CrossSingle(geneticParameters.getPopulation(), geneticParameters.getCrossoverProbability());
                geneticParameters.setPopulation(crossSingle.getPopulation());
                break;

            case DOUBLE:
                CrossDouble crossDouble = new CrossDouble(geneticParameters.getPopulation(), geneticParameters.getCrossoverProbability());
                geneticParameters.setPopulation(crossDouble.getPopulation());
                break;

            case EVENLY:
                CrossEvenly crossEvenly = new CrossEvenly(geneticParameters.getPopulation(), geneticParameters.getCrossoverProbability());
                geneticParameters.setPopulation(crossEvenly.getPopulation());
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

    private void countError() {
        geneticParameters.setTotalError(new StartupService(neuralNetwork).getTotalError(neuralNetwork.getParameters().getLearningData()));
    }

    private void countObjectsOutOfTolerance() {
        geneticParameters.setObjectsOutOfTolerace(new StartupService(neuralNetwork).getObjectsOutOfTolerance(neuralNetwork.getParameters().getLearningData()));
    }

    /* EndingcConditions */
    private boolean conditions() {
        return iterationCondition() && toleranceCondition();
    }

    private boolean iterationCondition() {
        return geneticParameters.getIteration() < geneticParameters.getIterationsAmount();
    }

    private boolean toleranceCondition() {
        return geneticParameters.getTotalError() > learning.getLearningTolerance();
    }

    /* interface update */
    private void updateIteration() {
        learning.getLearningWidget().updateIteration(geneticParameters.getIteration());
    }

    private void updateError() {
        countError();
        learning.getLearningWidget().updateError(geneticParameters.getTotalError());
    }

    private void updateObjectsOutOfTolerance() {
        countObjectsOutOfTolerance();
        learning.getLearningWidget().updateObjectsOutOfTolerance(geneticParameters.getObjectsOutOfTolerace().toString() + " / " + neuralNetwork.getParameters().getLearningData().size());
    }

    private void updateWeights() {
        learning.getLearningWidget().updateWeightsPane();
    }

    private void endLearning() {
        try {
            geneticParameters.getPopulation().sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }

        List<Connection> connections = neuralNetwork.getStructure().getConnections();
        Chromosome bestChromosome = geneticParameters.getPopulation().get(0);

        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setWeight(bestChromosome.getWeights().get(i));
        }

        updateIteration();
        updateError();
        updateObjectsOutOfTolerance();

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

    public enum CrossoverMethod {SINGLE, DOUBLE, EVENLY}

    public enum MutationMethod {FLIPBIT, FLIPSTRING}
}
