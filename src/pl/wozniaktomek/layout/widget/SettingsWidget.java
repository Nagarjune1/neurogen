package pl.wozniaktomek.layout.widget;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.learning.Learning;
import pl.wozniaktomek.service.LayoutService;

public class SettingsWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    private HBox topologyContainer;
    private VBox learningContainer;

    public SettingsWidget(NeuralNetwork neuralNetwork, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        createPrimaryWidget("Parametry sieci");
        initializeContainers();
    }

    private void initializeContainers() {
        VBox localContentContainer = layoutService.getVBox(0d, 0d, 12d);
        contentContainer.getChildren().add(localContentContainer);

        topologyContainer = layoutService.getHBox(0d, 8d, 8d);
        topologyContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        VBox mainTopologyContainer = layoutService.getVBox(8d, 8d, 8d);
        mainTopologyContainer.getChildren().addAll(layoutService.getText("TOPOLOGIA", LayoutService.TextStyle.HEADING), topologyContainer);

        learningContainer = layoutService.getVBox(4d, 8d, 8d);
        learningContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        learningContainer.setMinWidth(256d);
        VBox mainLearningContainer = layoutService.getVBox(8d, 8d, 8d);
        mainLearningContainer.getChildren().addAll(layoutService.getText("METODA UCZENIA", LayoutService.TextStyle.HEADING), learningContainer);

        localContentContainer.getChildren().addAll(mainTopologyContainer, mainLearningContainer);
    }

    public void refreshWidget() {
        refreshTopologySettings();
        refreshLearningSettings();
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

    private void refreshLearningSettings() {
        learningContainer.getChildren().clear();

        if (neuralNetwork.getStructure().getLayers().size() > 0) {
            learningContainer.getChildren().add(getLearningMethodChoiceBox());
        } else {
            learningContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }
    }

    private void refreshInputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wejściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshOutputLayer(VBox vBox) {
        HBox hBox = layoutService.getHBox(2d, 0d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wyjściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        vBox.getChildren().add(hBox);
    }

    private void refreshHiddenLayers(VBox vBox) {
        for (int i = 1; i < neuralNetwork.getStructure().getLayers().size() - 1; i++) {
            HBox hBox = layoutService.getHBox(2d, 0d, 12d);
            hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa ukryta " + i, LayoutService.TextStyle.STATUS)));

            Spinner spinner;
            if (neuralNetwork.getStructure().isBias()) {
                spinner = getSpinner(false, neuralNetwork.getStructure().getLayers().get(i).getLayerSize() - 1, i + 1);
            } else {
                spinner = getSpinner(false, neuralNetwork.getStructure().getLayers().get(i).getLayerSize(), i + 1);
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

    private Spinner getSpinner(Boolean isDisable, Integer value, Integer number) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, value);
        spinner.setValueFactory(valueFactory);
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

    private ChoiceBox getLearningMethodChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Algorytm genetyczny", "Algorytm wstecznej propagacji");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Algorytm genetyczny")) {
                neuralNetwork.createLearning(Learning.LearningMethod.GENETIC);
            } else {
                neuralNetwork.createLearning(Learning.LearningMethod.BACKPROPAGATION);
            }
        });

        return choiceBox;
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
