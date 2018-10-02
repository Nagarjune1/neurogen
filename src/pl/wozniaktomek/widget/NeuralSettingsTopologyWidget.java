package pl.wozniaktomek.widget;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

class NeuralSettingsTopologyWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralSettingsWidget neuralSettingsWidget;

    NeuralSettingsTopologyWidget(NeuralNetwork neuralNetwork, NeuralSettingsWidget neuralSettingsWidget, String title) {
        this.neuralNetwork = neuralNetwork;
        this.neuralSettingsWidget = neuralSettingsWidget;
        setTitle(title);
    }

    void refreshWidget() {
        contentContainer.getChildren().clear();

        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            refreshInputLayer();
            refreshHiddenLayers();
            refreshOutputLayer();
        } else {
            contentContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }
    }

    private void refreshInputLayer() {
        HBox hBox = layoutService.getHBox(2d, 12d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 96d, layoutService.getText("Warstwa wejściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        if (neuralNetwork.getNeuralStructure().getLayers().size() <= 2) {
            hBox.getChildren().add(getNewLayerButton());
        }

        contentContainer.getChildren().add(hBox);
    }

    private void refreshHiddenLayers() {
        for (int i = 1; i < neuralNetwork.getNeuralStructure().getLayers().size() - 1; i++) {
            HBox hBox = layoutService.getHBox(2d, 12d, 12d);
            hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 96d, layoutService.getText("Warstwa ukryta " + i, LayoutService.TextStyle.STATUS)));

            Spinner spinner;
            if (neuralNetwork.getNeuralStructure().isBias()) {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize() - 1, i + 1);
            } else {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize(), i + 1);
            }

            hBox.getChildren().add(spinner);

            if (i == neuralNetwork.getNeuralStructure().getLayers().size() - 2) {
                hBox.getChildren().add(getNewLayerButton());
            }

            hBox.getChildren().add(getDeleteLayerButton(i + 1));

            contentContainer.getChildren().add(hBox);
        }
    }

    private void refreshOutputLayer() {
        HBox hBox = layoutService.getHBox(2d, 12d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 96d, layoutService.getText("Warstwa wyjściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        contentContainer.getChildren().add(hBox);
    }


    private Spinner getSpinner(Boolean isDisable, Integer value, Integer number) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, value);
        spinner.setValueFactory(valueFactory);
        spinner.setDisable(isDisable);
        spinner.setPrefWidth(72d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (neuralNetwork.getNeuralStructure().isBias()) {
                neuralNetwork.getNeuralStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue + 1);
            } else {
                neuralNetwork.getNeuralStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue);
            }

            neuralSettingsWidget.refreshWidget();
        }));

        return spinner;
    }

    private Button getDeleteLayerButton(Integer layerNumber) {
        Button button = new Button();
        button.setText("-");
        button.setPrefSize(32d, 32d);

        button.setOnAction(event -> {
            neuralNetwork.getNeuralStructure().deleteLayer(getLayerNumber(layerNumber));
            neuralSettingsWidget.refreshWidget();
        });

        return button;
    }

    private Button getNewLayerButton() {
        Button button = new Button();
        button.setText("+");
        button.setPrefSize(32d, 32d);

        button.setOnAction(event -> {
            if (neuralNetwork.getNeuralStructure().isBias()) {
                neuralNetwork.getNeuralStructure().addLayer(2);
            } else {
                neuralNetwork.getNeuralStructure().addLayer(1);
            }

            neuralSettingsWidget.refreshWidget();
        });

        return button;
    }

    private Integer getLayerNumber(Integer number) {
        if (number.equals(0)) {
            return 0;
        } else if (number.equals(1)) {
            return neuralNetwork.getNeuralStructure().getLayers().size() - 1;
        } else {
            return number - 1;
        }
    }
}
