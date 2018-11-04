package pl.wozniaktomek.layout.widget;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.Backpropagation;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.service.LayoutService;

public class SettingsWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    private HBox learningParametersContainer;
    private HBox learningMethodContainer;
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

        learningMethodContainer = layoutService.getHBox(4d, 8d, 18d);
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
                HBox geneticSettingsContainer = layoutService.getHBox(5d, 0d, 8d);
                geneticSettingsContainer.getChildren().add(layoutService.getText("GENETIC", LayoutService.TextStyle.STATUS));
                learningMethodSettingsContainer.getChildren().add(geneticSettingsContainer);
            } else {
                HBox backpropagationSettingsContainer = layoutService.getHBox(5d, 0d, 8d);
                backpropagationSettingsContainer.getChildren().addAll(layoutService.getText("Współczynnik uczenia", LayoutService.TextStyle.STATUS), getLearningFactorSpinner());
                learningMethodSettingsContainer.getChildren().add(backpropagationSettingsContainer);
            }
        }
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

    private Spinner getLearningIterationsSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100000, neuralNetwork.getLearning().getIterationsAmount(), 1));
        spinner.setEditable(true);
        spinner.setPrefWidth(92d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setEndingIterations(newValue)));
        return spinner;
    }

    private Spinner getLearningToleranceSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, neuralNetwork.getLearning().getLearningTolerance(), 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> neuralNetwork.getLearning().setEndingLearningTolerance(newValue)));
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

        if (neuralNetwork.getLearning() instanceof Backpropagation) {
            choiceBox.getSelectionModel().select("Algorytm wstecznej propagacji");
        } else {
            choiceBox.getSelectionModel().select("Algorytm genetyczny");
        }

        return choiceBox;
    }

    private Spinner getLearningFactorSpinner() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1.0, 0.1, 0.01));
        spinner.setEditable(true);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> ((Backpropagation) neuralNetwork.getLearning()).setLearningParameters(newValue)));
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

            refreshWidget();
        }));

        return spinner;
    }

    private Button getDeleteLayerButton(Integer layerNumber) {
        Button button = new Button();
        button.setText("Usuń warstwę");

        button.setOnAction(event -> {
            neuralNetwork.getStructure().deleteLayer(getLayerNumber(layerNumber));
            refreshWidget();
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

            refreshWidget();
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

            refreshWidget();
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
