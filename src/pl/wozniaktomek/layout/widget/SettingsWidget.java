package pl.wozniaktomek.layout.widget;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.GeneticAlgorithm;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.service.LayoutService;

public class SettingsWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    private VBox mainLearningContainer;
    private VBox mainTopologyContainer;

    private HBox learningParametersContainer;
    private VBox learningMethodContainer;
    private HBox learningMethodSettingsContainer;
    private HBox topologyContainer;

    private Spinner<Double> minRangeSpinner;
    private Spinner<Double> maxRangeSpinner;
    private Spinner<Integer> tournamentSizeSpinner;

    private VBox binaryStringInfoVBox;
    private Text binaryStringInfo;
    private Text binaryStringWarning;

    public SettingsWidget(NeuralNetwork neuralNetwork, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        createWidget("Parametry sieci");
        initialize();
    }

    /* Initialization */
    private void initialize() {
        mainLearningContainer = layoutService.getVBox(4d, 8d, 16d);
        mainTopologyContainer = layoutService.getVBox(4d, 8d, 8d);

        learningParametersContainer = layoutService.getHBox(4d, 8d, 18d);
        mainLearningContainer.getChildren().addAll(layoutService.getText("PARAMETRY UCZENIA", LayoutService.TextStyle.HEADING), learningParametersContainer);

        learningMethodContainer = layoutService.getVBox(4d, 8d, 18d);
        learningMethodContainer.setMinWidth(256d);
        mainLearningContainer.getChildren().addAll(layoutService.getText("METODA UCZENIA", LayoutService.TextStyle.HEADING), learningMethodContainer);

        topologyContainer = layoutService.getHBox(0d, 8d, 8d);
        mainTopologyContainer.getChildren().addAll(layoutService.getText("TOPOLOGIA", LayoutService.TextStyle.HEADING), topologyContainer);

        contentContainer.getChildren().add(layoutService.getText("Wymagane jest wczytanie danych uczących!", LayoutService.TextStyle.HEADING));
    }

    public void refreshWidget() {
        refreshLearningSettings();
        refreshTopologySettings();
        refreshBinaryStringInfo();
    }

    /*
     * Containers
     */
    private void refreshLearningSettings() {
        learningParametersContainer.getChildren().clear();
        learningMethodContainer.getChildren().clear();
        contentContainer.getChildren().clear();

        if (neuralNetwork.getStructure().getLayers().size() > 0) {
            contentContainer.getChildren().addAll(mainLearningContainer, mainTopologyContainer);
            refreshParametersContainer();
            refreshMethodContainer(null);
            refreshMethodContainer(true);
        } else {
            contentContainer.getChildren().add(layoutService.getText("Wymagane jest wczytanie danych uczących!", LayoutService.TextStyle.HEADING));
        }
    }

    private void refreshParametersContainer() {
        HBox hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getChildren().addAll(layoutService.getText("Maksymalna liczba iteracji", LayoutService.TextStyle.PARAGRAPH), getLearningIterationsSpinner());
        learningParametersContainer.getChildren().add(hBox);

        hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getChildren().addAll(layoutService.getText("Tolerancja uczenia", LayoutService.TextStyle.PARAGRAPH), getLearningToleranceSpinner());
        learningParametersContainer.getChildren().add(hBox);

        /*
        hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getChildren().addAll(layoutService.getText("Tolerancja całkowita", LayoutService.TextStyle.PARAGRAPH), getToleranceToggleButton());
        learningParametersContainer.getChildren().add(hBox);
        */
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
                refreshBinaryStringInfo();
            } else {
                HBox backpropagationSettingsContainer = layoutService.getHBox(5d, 8d, 12d);
                backpropagationSettingsContainer.getChildren().addAll(layoutService.getText("Współczynnik uczenia", LayoutService.TextStyle.PARAGRAPH), getLearningFactorSpinner());
                backpropagationSettingsContainer.getChildren().addAll(new Separator(Orientation.VERTICAL), getRecordsMixingCheckbox());
                learningMethodSettingsContainer.getChildren().add(backpropagationSettingsContainer);
            }
        }
    }

    private VBox getGeneticMethodsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 8d);
        vBox.getChildren().add(layoutService.getText("OPERATORY GENETYCZNE", LayoutService.TextStyle.PARAGRAPH));

        HBox crossoverHbox = layoutService.getHBox(0d, 0d, 8d);
        crossoverHbox.getChildren().addAll(layoutService.getText("Krzyżowanie", LayoutService.TextStyle.PARAGRAPH), getGeneticCrossoverMethodChoiceBox());
        crossoverHbox.getChildren().addAll(layoutService.getText("prawdopodobieństwo: ", LayoutService.TextStyle.PARAGRAPH), getGeneticCrossoverProbabilitySpinner());

        HBox mutationHbox = layoutService.getHBox(0d, 0d, 8d);
        mutationHbox.getChildren().addAll(layoutService.getText("Mutacja", LayoutService.TextStyle.PARAGRAPH), getGeneticMutationMethodChoiceBox());
        mutationHbox.getChildren().addAll(layoutService.getText("prawdopodobieństwo: ", LayoutService.TextStyle.PARAGRAPH), getGeneticMutationProbabilitySpinner());

        createTournamentSizeSpinner();
        HBox selectionHbox = layoutService.getHBox(0d, 0d, 8d);
        selectionHbox.getChildren().addAll(layoutService.getText("Selekcja", LayoutService.TextStyle.PARAGRAPH), getGeneticSelectionMethodChoiceBox(), tournamentSizeSpinner);

        vBox.getChildren().addAll(crossoverHbox, mutationHbox, selectionHbox);
        return vBox;
    }

    private VBox getGeneticParametersContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 8d);
        vBox.getChildren().add(layoutService.getText("PARAMETRY ALGORYTMU", LayoutService.TextStyle.PARAGRAPH));

        HBox populationSizeHbox = layoutService.getHBox(0d, 0d, 8d);
        populationSizeHbox.getChildren().addAll(layoutService.getText("Rozmiar populacji", LayoutService.TextStyle.PARAGRAPH), getGeneticPopulationSizeSpinner());

        HBox genSizeHbox = layoutService.getHBox(0d, 0d, 8d);
        genSizeHbox.getChildren().addAll(layoutService.getText("Rozmiar pojedynczego genu", LayoutService.TextStyle.PARAGRAPH), getGeneticGenSizeSpinner());

        binaryStringInfoVBox = layoutService.getVBox(0d, 0d, 0d);
        binaryStringInfo = layoutService.getText("", LayoutService.TextStyle.PARAGRAPH_THEME);
        binaryStringWarning = layoutService.getText("", LayoutService.TextStyle.PARAGRAPH_THEME);
        binaryStringInfoVBox.getChildren().addAll(binaryStringInfo, binaryStringWarning);

        HBox chromosomeRangeHbox = layoutService.getHBox(0d, 0d, 8d);
        chromosomeRangeHbox.getChildren().addAll(layoutService.getText("Przedział wartości wag", LayoutService.TextStyle.PARAGRAPH), getChromosomeMinRangeSpinner(), getChromosomeMaxRangeSpinner());

        vBox.getChildren().addAll(populationSizeHbox, genSizeHbox, binaryStringInfoVBox, chromosomeRangeHbox);
        return vBox;
    }

    private void refreshBinaryStringInfo() {
        if (neuralNetwork.getLearning().getLearningMethod().equals(Learning.LearningMethod.GENETIC)) {
            int binaryStringSize = neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getGenSize() * neuralNetwork.getStructure().getConnections().size();
            binaryStringInfo.setText("Rozmiar osobnika: " + binaryStringSize + "b");

            if (binaryStringSize > 512) {
                binaryStringWarning.setText("Uwaga! Algorytm genetyczny może nie działać prawidłowo!");
                if (!binaryStringInfoVBox.getChildren().contains(binaryStringWarning)) {
                    binaryStringInfoVBox.getChildren().add(binaryStringWarning);
                }
            } else {
                binaryStringInfoVBox.getChildren().remove(binaryStringWarning);
            }
        }
    }

    private void refreshRangeSpinners() {
        minRangeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-52d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMaxRange() - 0.1,
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMinRange(), 0.1));

        maxRangeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMinRange() + 0.1,
                52d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMaxRange(), 0.1));
    }

    private void refreshTopologySettings() {
        topologyContainer.getChildren().clear();

        if (neuralNetwork.getStructure().getLayers().size() > 0) {
            VBox vBox = layoutService.getVBox(0d, 12d, 12d);
            topologyContainer.getChildren().add(vBox);
            refreshInputLayer(vBox);
            refreshOutputLayer(vBox);

            vBox = layoutService.getVBox(0d, 12d, 12d);
            topologyContainer.getChildren().add(vBox);
            refreshHiddenLayers(vBox);

            vBox = layoutService.getVBox(6d, 12d, 12d);
            topologyContainer.getChildren().add(vBox);
            refreshTopologySettings(vBox);
        }

        neuralNetwork.getStructure().createConnections();
        neuralControl.refreshTopology();
        refreshBinaryStringInfo();
    }

    private void refreshInputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wejściowa", LayoutService.TextStyle.PARAGRAPH)));

        Spinner spinner = getLayerSpinner(true, neuralNetwork.getParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshOutputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wyjściowa", LayoutService.TextStyle.PARAGRAPH)));

        Spinner spinner = getLayerSpinner(true, neuralNetwork.getParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshHiddenLayers(VBox vBox) {
        for (int i = 1; i < neuralNetwork.getStructure().getLayers().size() - 1; i++) {
            HBox hBox = layoutService.getHBox(2d, 0d, 12d);
            hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa ukryta " + i, LayoutService.TextStyle.PARAGRAPH)));

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
        vBox.getChildren().add(layoutService.getText("POZOSTAŁE PARAMETRY TOPOLOGII", LayoutService.TextStyle.PARAGRAPH));
        vBox.getChildren().add(getBiasCheckbox());
    }

    /*
     * Controls
     */
    private Spinner getLearningIterationsSpinner() {
        Spinner<Integer> spinner = layoutService.getIntegerSpinner(92d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000, neuralNetwork.getLearning().getIterationsAmount(), 1));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setIterationsAmount(newValue)));
        return spinner;
    }

    private Spinner getLearningToleranceSpinner() {
        Spinner<Double> spinner = layoutService.getDoubleSpinner(78d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, neuralNetwork.getLearning().getLearningTolerance(), 0.01));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setLearningTolerance(newValue)));
        return spinner;
    }

    private ToggleButton getToleranceToggleButton() {
        ToggleButton toggleButton = new ToggleButton("Wyłączona");
        toggleButton.setPrefWidth(84d);
        toggleButton.setSelected(neuralNetwork.getLearning().getIsTotalTolerance());

        toggleButton.setOnAction(event -> {
            if (toggleButton.isSelected()) {
                neuralNetwork.getLearning().setIsTotalTolerance(true);
                toggleButton.setText("Włączona");
            } else {
                neuralNetwork.getLearning().setIsTotalTolerance(false);
                toggleButton.setText("Wyłączona");
            }
        });
        return toggleButton;
    }

    private ChoiceBox getLearningMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Algorytm genetyczny", "Algorytm wstecznej propagacji");

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
        choiceBox.getItems().addAll("Krzyżowanie jednopunktowe", "Krzyżowanie dwupunktowe", "Krzyżowanie równomierne");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Krzyżowanie jednopunktowe":
                    neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverMethod(GeneticAlgorithm.CrossoverMethod.SINGLE);
                    break;
                case "Krzyżowanie dwupunktowe":
                    neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverMethod(GeneticAlgorithm.CrossoverMethod.DOUBLE);
                    break;
                default:
                    neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverMethod(GeneticAlgorithm.CrossoverMethod.EVENLY);
                    break;
            }
        });

        if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getCrossoverMethod().equals(GeneticAlgorithm.CrossoverMethod.SINGLE)) {
            choiceBox.getSelectionModel().select("Krzyżowanie jednopunktowe");
        } else if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getCrossoverMethod().equals(GeneticAlgorithm.CrossoverMethod.DOUBLE)) {
            choiceBox.getSelectionModel().select("Krzyżowanie dwupunktowe");
        } else {
            choiceBox.getSelectionModel().select("Krzyżowanie równomierne");
        }

        return choiceBox;
    }

    private Spinner getGeneticCrossoverProbabilitySpinner() {
        Spinner<Double> spinner = layoutService.getDoubleSpinner(72d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0d, 1d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getCrossoverProbability(), 0.01));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setCrossoverProbability(newValue)));
        return spinner;
    }

    private ChoiceBox getGeneticMutationMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Mutacja pojedynczego bitu", "Mutacja całego ciągu bitów");

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
        Spinner<Double> spinner = layoutService.getDoubleSpinner(72d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0d, 1d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getMutationProbability(), 0.01));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setMutationProbability(newValue)));
        return spinner;
    }

    private ChoiceBox getGeneticSelectionMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Metoda turniejowa", "Metoda koła ruletki");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Metoda turniejowa")) {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setSelectionMethod(GeneticAlgorithm.SelectionMethod.TOURNAMENT);
                tournamentSizeSpinner.setVisible(true);
            } else {
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setSelectionMethod(GeneticAlgorithm.SelectionMethod.ROULETTE);
                tournamentSizeSpinner.setVisible(false);
            }
        });

        if (neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getSelectionMethod().equals(GeneticAlgorithm.SelectionMethod.TOURNAMENT)) {
            choiceBox.getSelectionModel().select("Metoda turniejowa");
        } else {
            choiceBox.getSelectionModel().select("Metoda koła ruletki");
        }

        return choiceBox;
    }

    private void createTournamentSizeSpinner() {
        tournamentSizeSpinner = layoutService.getIntegerSpinner(72d, true, false);
        tournamentSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 32, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getTournamentSize(), 1));

        tournamentSizeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setTournamentSize(newValue)));
    }

    private Spinner getGeneticPopulationSizeSpinner() {
        Spinner<Integer> spinner = layoutService.getIntegerSpinner(72d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getPopulationSize(), 1));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setPopulationSize(newValue)));
        return spinner;
    }

    private Spinner getGeneticGenSizeSpinner() {
        Spinner<Integer> spinner = layoutService.getIntegerSpinner(72d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 32, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getGenSize(), 1));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setGenSize(newValue);
            refreshBinaryStringInfo();
        }));
        return spinner;
    }

    private Spinner getChromosomeMinRangeSpinner() {
        minRangeSpinner = layoutService.getDoubleSpinner(72d, true, false);
        minRangeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-52d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMaxRange() - 0.1,
                neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMinRange(), 0.1));

        minRangeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setChromosomeMinRange(newValue);
            refreshRangeSpinners();
        }));
        return minRangeSpinner;
    }

    private Spinner getChromosomeMaxRangeSpinner() {
        maxRangeSpinner = layoutService.getDoubleSpinner(72d, true, false);
        maxRangeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMinRange() + 0.1,
                52d, neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().getChromosomeMaxRange(), 0.1));

        maxRangeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            neuralNetwork.getLearning().getGeneticAlgorithm().getGeneticParameters().setChromosomeMaxRange(newValue);
            refreshRangeSpinners();
        }));
        return maxRangeSpinner;
    }

    private Spinner getLearningFactorSpinner() {
        Spinner<Double> spinner = layoutService.getDoubleSpinner(72d, true, false);
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, neuralNetwork.getLearning().getBackpropagation().getBackpropagationParameters().getLearningFactor(), 0.01));

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().getBackpropagation().getBackpropagationParameters().setLearningFactor(newValue)));
        return spinner;
    }

    private CheckBox getRecordsMixingCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Mieszanie danych wejściowych", null);
        checkBox.setSelected(neuralNetwork.getLearning().getBackpropagation().getBackpropagationParameters().getRecordsMixing());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> neuralNetwork.getLearning().getBackpropagation().getBackpropagationParameters().setRecordsMixing(newValue));
        return checkBox;
    }

    private Spinner getLayerSpinner(Boolean isDisable, Integer value, Integer number) {
        Spinner<Integer> spinner = layoutService.getIntegerSpinner(64d, false, isDisable);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, value));

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
        button.setText("Dodaj warstwę ukrytą");

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
