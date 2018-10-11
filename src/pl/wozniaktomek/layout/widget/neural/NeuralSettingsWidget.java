package pl.wozniaktomek.layout.widget.neural;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

public class NeuralSettingsWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    private VBox topologyContainer;
    private VBox parametersContainer;

    public NeuralSettingsWidget(NeuralNetwork neuralNetwork, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        createPrimaryWidget("Parametry sieci");
        initializeContainers();
    }

    private void initializeContainers() {
        HBox localContentContainer = layoutService.getHBox(0d, 0d, 0d);
        contentContainer.getChildren().add(localContentContainer);

        topologyContainer = layoutService.getVBox(4d, 8d, 8d);
        topologyContainer.setMinWidth(356d);

        parametersContainer = layoutService.getVBox(4d, 8d, 8d);
        localContentContainer.getChildren().addAll(topologyContainer, parametersContainer);
    }

    public void refreshWidget() {
        refreshTopology();
        refreshParameters();
        neuralNetwork.getNeuralStructure().createConnections();
        neuralNetwork.showNetwork();
    }

    private void refreshTopology() {
        topologyContainer.getChildren().clear();
        topologyContainer.getChildren().add(layoutService.getText("TOPOLOGIA", LayoutService.TextStyle.HEADING));

        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            refreshInputLayer();
            refreshHiddenLayers();
            refreshOutputLayer();
        } else {
            topologyContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }

        neuralControl.refreshTopology();
    }

    private void refreshParameters() {
        parametersContainer.getChildren().clear();
        parametersContainer.getChildren().add(layoutService.getText("PARAMETRY", LayoutService.TextStyle.HEADING));
    }

    private void refreshInputLayer() {
        HBox hBox = layoutService.getHBox(2d, 12d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wejściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        topologyContainer.getChildren().add(hBox);
    }

    private void refreshHiddenLayers() {
        for (int i = 1; i < neuralNetwork.getNeuralStructure().getLayers().size() - 1; i++) {
            HBox hBox = layoutService.getHBox(2d, 12d, 12d);
            hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa ukryta " + i, LayoutService.TextStyle.STATUS)));

            Spinner spinner;
            if (neuralNetwork.getNeuralStructure().isBias()) {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize() - 1, i + 1);
            } else {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize(), i + 1);
            }

            hBox.getChildren().add(spinner);
            hBox.getChildren().add(getDeleteLayerButton(i + 1));

            topologyContainer.getChildren().add(hBox);
        }

        topologyContainer.getChildren().add(getNewLayerButton());
    }

    private void refreshOutputLayer() {
        HBox hBox = layoutService.getHBox(2d, 12d, 12d);
        hBox.getChildren().add(layoutService.getTextFlow(6d, 0d, 112d, layoutService.getText("Warstwa wyjściowa", LayoutService.TextStyle.STATUS)));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        topologyContainer.getChildren().add(hBox);
    }


    private Spinner getSpinner(Boolean isDisable, Integer value, Integer number) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 64, value);
        spinner.setValueFactory(valueFactory);
        spinner.setDisable(isDisable);
        spinner.setPrefWidth(64d);

        spinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (neuralNetwork.getNeuralStructure().isBias()) {
                neuralNetwork.getNeuralStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue + 1);
            } else {
                neuralNetwork.getNeuralStructure().getLayers().get(getLayerNumber(number)).setNumberOfNeurons(newValue);
            }


            refreshWidget();
        }));

        return spinner;
    }

    private Button getDeleteLayerButton(Integer layerNumber) {
        Button button = new Button();
        button.setText("Usuń warstwę");

        button.setOnAction(event -> {
            neuralNetwork.getNeuralStructure().deleteLayer(getLayerNumber(layerNumber));
            refreshWidget();
        });

        return button;
    }

    private Button getNewLayerButton() {
        Button button = new Button();
        button.setText("Dodaj nową warstwę ukrytą");

        button.setOnAction(event -> {
            if (neuralNetwork.getNeuralStructure().isBias()) {
                neuralNetwork.getNeuralStructure().addLayer(2);
            } else {
                neuralNetwork.getNeuralStructure().addLayer(1);
            }

            refreshWidget();
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
