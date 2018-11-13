package pl.wozniaktomek.layout.widget;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.GeneticAlgorithm;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.neural.service.LearningService;
import pl.wozniaktomek.service.LayoutService;

public class SettingsWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    private HBox learningParametersContainer;
    private VBox learningMethodContainer;
    private HBox learningMethodSettingsContainer;
    private HBox topologyContainer;

    public SettingsWidget(NeuralNetwork neuralNetwork, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        createPrimaryWidget("Parametry sieci");
        initializeContainers();
    }

    private void initializeContainers() {
        VBox localContentContainer = layoutService.getVBox(0d, 0d, 12d);
        contentContainer.getChildren().add(localContentContainer);

        VBox mainLearningContainer = layoutService.getVBox(4d, 8d, 16d);
        VBox mainTopologyContainer = layoutService.getVBox(4d, 8d, 8d);
        localContentContainer.getChildren().addAll(mainLearningContainer, mainTopologyContainer);

        learningParametersContainer = layoutService.getHBox(4d, 8d, 18d);
        learningParametersContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        mainLearningContainer.getChildren().addAll(layoutService.getText("PARAMETRY UCZENIA", LayoutService.TextStyle.HEADING), learningParametersContainer);

        learningMethodContainer = layoutService.getVBox(4d, 8d, 18d);
        learningMethodContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        learningMethodContainer.setMinWidth(256d);
        mainLearningContainer.getChildren().addAll(layoutService.getText("METODA UCZENIA", LayoutService.TextStyle.HEADING), learningMethodContainer);

        topologyContainer = layoutService.getHBox(0d, 8d, 8d);
        topologyContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        mainTopologyContainer.getChildren().addAll(layoutService.getText("TOPOLOGIA", LayoutService.TextStyle.HEADING), topologyContainer);
    }

    public void refreshWidget() {
        refreshLearningSettings();
        refreshTopologySettings();
    }

    /**
     * Containers
     **/
    private void refreshLearningSettings() {
        learningParametersContainer.getChildren().clear();
        learningMethodContainer.getChildren().clear();

        if (neuralNetwork.getStructure().getLayers().size() > 0) {
            refreshParametersContainer();
            refreshMethodContainer(null);
            refreshMethodContainer(true);

        } else {
            learningParametersContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
            learningMethodContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }
    }

    private void refreshParametersContainer() {
        HBox hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getChildren().addAll(layoutService.getText("Maksymalna liczba iteracji", LayoutService.TextStyle.STATUS), getLearningIterationsSpinner());
        learningParametersContainer.getChildren().add(hBox);

        hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getChildren().addAll(layoutService.getText("Tolerancja uczenia", LayoutService.TextStyle.STATUS), getLearningToleranceSpinner());
        learningParametersContainer.getChildren().add(hBox);
    }

    private void refreshMethodContainer(Boolean isGenetic) {
        if (isGenetic == null) {
            learningMethodSettingsContainer = layoutService.getHBox(0d, 0d, 8d);
            learningMethodContainer.getChildren().addAll(getLearningMethodChoiceBox(), learningMethodSettingsContainer);
        } else {
            learningMethodSettingsContainer.getChildren().clear();
            if (isGenetic) {
                HBox geneticSettingsContainer = layoutService.getHBox(5d, 0d, 48d);
                geneticSettingsContainer.getChildren().addAll(getGeneticMethodsContainer(), getGeneticParametersContainer());
                learningMethodSettingsContainer.getChildren().add(geneticSettingsContainer);
            } else {
                HBox backpropagationSettingsContainer = layoutService.getHBox(5d, 0d, 8d);
                backpropagationSettingsContainer.getChildren().addAll(layoutService.getText("Współczynnik uczenia", LayoutService.TextStyle.STATUS), getLearningFactorSpinner());
                learningMethodSettingsContainer.getChildren().add(backpropagationSettingsContainer);
            }
        }
    }

    private VBox getGeneticMethodsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 8d);
        vBox.getChildren().add(layoutService.getText("OPERATORY GENETYCZNE", LayoutService.TextStyle.STATUS));

        HBox crossoverHbox = layoutService.getHBox(0d, 0d, 8d);
        crossoverHbox.getChildren().addAll(layoutService.getText("Krzyżowanie", LayoutService.TextStyle.STATUS), getGeneticCrossoverMethodChoiceBox());
        crossoverHbox.getChildren().addAll(layoutService.getText("prawdopodobieństwo: ", LayoutService.TextStyle.STATUS), getGeneticCrossoverProbabilitySpinner());

        HBox mutationHbox = layoutService.getHBox(0d, 0d, 8d);
        mutationHbox.getChildren().addAll(layoutService.getText("Mutacja", LayoutService.TextStyle.STATUS), getGeneticMutationMethodChoiceBox());
        mutationHbox.getChildren().addAll(layoutService.getText("prawdopodobieństwo: ", LayoutService.TextStyle.STATUS), getGeneticMutationProbabilitySpinner());

        HBox selectionHbox = layoutService.getHBox(0d, 0d, 8d);
        selectionHbox.getChildren().addAll(layoutService.getText("Selekcja", LayoutService.TextStyle.STATUS), getGeneticSelectionMethodChoiceBox());

        vBox.getChildren().addAll(crossoverHbox, mutationHbox, selectionHbox);
        return vBox;
    }

    private VBox getGeneticParametersContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 8d);
        vBox.getChildren().add(layoutService.getText("PARAMETRY ALGORYTMU", LayoutService.TextStyle.STATUS));

        HBox populationSizeHbox = layoutService.getHBox(0d, 0d, 8d);
        populationSizeHbox.getChildren().addAll(layoutService.getText("Rozmiar populacji", LayoutService.TextStyle.STATUS), getGeneticPopulationSizeSpinner());

        HBox genSizeHbox = layoutService.getHBox(0d, 0d, 8d);
        genSizeHbox.getChildren().addAll(layoutService.getText("Rozmiar pojedynczego genu", LayoutService.TextStyle.STATUS), getGeneticGenSizeSpinner());

        HBox chromosomeRangeHbox = layoutService.getHBox(0d, 0d, 8d);
        chromosomeRangeHbox.getChildren().addAll(layoutService.getText("Przedział wartości wag", LayoutService.TextStyle.STATUS), getChromosomeMinRangeSpinner(), getChromosomeMaxRangeSpinner());

        vBox.getChildren().addAll(populationSizeHbox, genSizeHbox, chromosomeRangeHbox);
        return vBox;
    }

    private void refreshTopologySettings() {
        topologyContainer.getChildren().clear();

        if (neuralNetwork.getStructure().getLayers().size() > 0) {
            VBox vBox = layoutService.getVBox(0d, 12d, 12d);
            topologyContainer.getChildren().add(vBox);
            refreshInputLayer(vBox);
            refreshOutputLayer(vBox);

            vBox = layoutService.getVBox(0d, 12d, 12d);
            vBox.setMinWidth(356d);
            topologyContainer.getChildren().add(vBox);
            refreshHiddenLayers(vBox);

            vBox = layoutService.getVBox(6d, 12d, 12d);
            topologyContainer.getChildren().add(vBox);
            refreshTopologySettings(vBox);
        } else {
            topologyContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }

        neuralNetwork.getStructure().createConnections();
        neuralControl.refreshTopology();
    }

    private void refreshInputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wejściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getLayerSpinner(true, neuralNetwork.getParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshOutputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wyjściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getLayerSpinner(true, neuralNetwork.getParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshHiddenLayers(VBox vBox) {
        for (int i = 1; i < neuralNetwork.getStructure().getLayers().size() - 1; i++) {
            HBox hBox = layoutService.getHBox(2d, 0d, 12d);
            hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa ukryta " + i, LayoutService.TextStyle.STATUS)));

            Spinner spinner;
            if (neuralNetwork.getStructure().isBias()) {
                spinner = getLayerSpinner(false, neuralNetwork.getStructure().getLayers().get(i).getLayerSize() - 1, i + 1);
            } else {
                spinner = getLayerSpinner(false, neuralNetwork.getStructure().getLayers().get(i).getLayerSize(), i + 1);
            }

            hBox.getChildren().add(spinner);
            hBox.getChildren().add(getDeleteLayerButton(i + 1));

            vBox.getChildren().add(hBox);
        }

        vBox.getChildren().add(getNewLayerButton());
    }

    private void refreshTopologySettings(VBox vBox) {
        vBox.getChildren().add(layoutService.getText("POZOSTAŁE PARAMETRY TOPOLOGII", LayoutService.TextStyle.STATUS));
        vBox.getChildren().add(getBiasCheckbox());
    }

    /**
     * Controls
     **/
    private Spinner getLearningIterationsSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, neuralNetwork.getLearning().getIterationsAmount(), 1));
        spinner.setEditable(true);
        spinner.setPrefWidth(92d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setIterationsAmount(newValue)));
        return spinner;
    }

    private Spinner getLearningToleranceSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, neuralNetwork.getLearning().getLearningTolerance(), 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setLearningTolerance(newValue)));
        return spinner;
    }

    private ChoiceBox getLearningMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Algorytm genetyczny", "Algorytm wstecznej propagacji");
        choiceBox.setMinWidth(212d);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Algorytm genetyczny")) {
                neuralNetwork.createLearning(Learning.LearningMethod.GENETIC);
                refreshMethodContainer(true);
            } else {
                neuralNetwork.createLearning(Learning.LearningMethod.BACKPROPAGATION);
                refreshMethodContainer(false);
            }
        });

        if (neuralNetwork.getLearning().getLearningMethod().equals(Learning.LearningMethod.GENETIC)) {
            choiceBox.getSelectionModel().select("Algorytm genetyczny");
            refreshMethodContainer(true);
        } else {
            choiceBox.getSelectionModel().select("Algorytm wstecznej propagacji");
            refreshMethodContainer(false);
        }

        return choiceBox;
    }

    private ChoiceBox getGeneticCrossoverMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Krzyżowanie jednopunktowe", "Krzyżowanie dwupunktowe");
        choiceBox.setMinWidth(212d);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Krzyżowanie jednopunktowe")) {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverMethod(GeneticAlgorithm.CrossoverMethod.SINGLE);
            } else {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverMethod(GeneticAlgorithm.CrossoverMethod.DOUBLE);
            }
        });

        if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getCrossoverMethod().equals(GeneticAlgorithm.CrossoverMethod.SINGLE)) {
            choiceBox.getSelectionModel().select("Krzyżowanie jednopunktowe");
        } else {
            choiceBox.getSelectionModel().select("Krzyżowanie dwupunktowe");
        }

        return choiceBox;
    }

    private Spinner getGeneticCrossoverProbabilitySpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getCrossoverProbability(), 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverProbability(newValue)));
        return spinner;
    }

    private ChoiceBox getGeneticMutationMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Mutacja pojedynczego bitu", "Mutacja całego ciągu bitów");
        choiceBox.setMinWidth(212d);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Mutacja pojedynczego bitu")) {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setMutationMethod(GeneticAlgorithm.MutationMethod.FLIPBIT);
            } else {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setMutationMethod(GeneticAlgorithm.MutationMethod.FLIPSTRING);
            }
        });

        if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getMutationMethod().equals(GeneticAlgorithm.MutationMethod.FLIPBIT)) {
            choiceBox.getSelectionModel().select("Mutacja pojedynczego bitu");
        } else {
            choiceBox.getSelectionModel().select("Mutacja całego ciągu bitów");
        }

        return choiceBox;
    }

    private Spinner getGeneticMutationProbabilitySpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getMutationProbability(), 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setMutationProbability(newValue)));
        return spinner;
    }

    private ChoiceBox getGeneticSelectionMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Metoda turniejowa", "Metoda koła ruletki");
        choiceBox.setMinWidth(212d);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Metoda turniejowa")) {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setSelectionMethod(GeneticAlgorithm.SelectionMethod.TOURNAMENT);
            } else {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setSelectionMethod(GeneticAlgorithm.SelectionMethod.ROULETTE);
            }
        });

        if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getSelectionMethod().equals(GeneticAlgorithm.SelectionMethod.TOURNAMENT)) {
            choiceBox.getSelectionModel().select("Metoda turniejowa");
        } else {
            choiceBox.getSelectionModel().select("Metoda koła ruletki");
        }

        return choiceBox;
    }

    private Spinner getGeneticPopulationSizeSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100, 1));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setPopulationSize(newValue)));
        return spinner;
    }

    private Spinner getGeneticGenSizeSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 32, 12, 1));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setGenSize(newValue)));
        return spinner;
    }

    private Spinner getChromosomeMinRangeSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-20d, 20d, -10d, 0.1));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setChromosomeMinRange(newValue)));
        return spinner;
    }

    private Spinner getChromosomeMaxRangeSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-20d, 20d, -10d, 0.1));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setChromosomeMaxRange(newValue)));
        return spinner;
    }

    private Spinner getLearningFactorSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, 0.1, 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getBackpropagation().getBackpropagationParameters().setLearningFactor(newValue)));
        return spinner;
    }

    private Spinner getLayerSpinner(Boolean isDisable, Integer value, Integer number) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, value));
        spinner.setDisable(isDisable);
        spinner.setPrefWidth(64d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (neuralNetwork.getStructure().isBias()) {
                neuralNetwork.getStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue + 1);
            } else {
                neuralNetwork.getStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue);
            }

            refreshTopologySettings();
        }));

        return spinner;
    }

    private Button getDeleteLayerButton(Integer layerNumber) {
        Button button = new Button();
        button.setText("Usuń warstwę");

        button.setOnAction(event -> {
            neuralNetwork.getStructure().deleteLayer(getLayerNumber(layerNumber));
            refreshTopologySettings();
        });

        return button;
    }

    private Button getNewLayerButton() {
        Button button = new Button();
        button.setText("Dodaj nową warstwę ukrytą");

        button.setOnAction(event -> {
            if (neuralNetwork.getStructure().isBias()) {
                neuralNetwork.getStructure().addLayer(2);
            } else {
                neuralNetwork.getStructure().addLayer(1);
            }

            refreshTopologySettings();
        });

        return button;
    }

    private CheckBox getBiasCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Bias", null);
        checkBox.setSelected(neuralNetwork.getStructure().isBias());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            checkBox.setSelected(newValue);

            if (newValue) {
                neuralNetwork.getStructure().addBias();
                new LearningService(neuralNetwork).initializeBiasOutput(neuralNetwork.getStructure().getLayers());
            } else {
                neuralNetwork.getStructure().deleteBias();
            }

            refreshTopologySettings();
        });

        return checkBox;
    }

    private Integer getLayerNumber(Integer number) {
        if (number.equals(0)) {
            return 0;
        } else if (number.equals(1)) {
            return neuralNetwork.getStructure().getLayers().size() - 1;
        } else {
            return number - 1;
        }
    }
}
