package pl.wozniaktomek.widget;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;

public class NeuralSettingsWidget extends Widget {
    private HBox mainPane;

    private DefaultWidget topologyWidget;
    private DefaultWidget parametersWidget;

    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    public NeuralSettingsWidget(NeuralNetwork neuralNetwork, String widgetTitle, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        setTitle(widgetTitle);
        initialize();
    }

    private void initialize() {
        initializeMainPane();
        initializeTopologyPane();
        initializeParametersPane();
        refreshTopology();
        refreshSettings();
    }

    private void initializeMainPane() {
        mainPane = new HBox();
        mainPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        mainPane.setSpacing(12);
        contentContainer.getChildren().add(mainPane);
    }

    private void initializeTopologyPane() {
        topologyWidget = new DefaultWidget("Topologia sieci");
        topologyWidget.setStyle(WidgetStyle.SECONDARY);
        mainPane.getChildren().add(topologyWidget.getWidget());
    }

    private void initializeParametersPane() {
        parametersWidget = new DefaultWidget("Parametry sieci");
        parametersWidget.setStyle(WidgetStyle.SECONDARY);
        mainPane.getChildren().add(parametersWidget.getWidget());
    }

    public void refreshWidget() {
        refreshTopology();
        refreshSettings();
        neuralControl.refreshTopologyWidget();
    }

    private void refreshTopology() {
        topologyWidget.contentContainer.getChildren().clear();

        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            refreshInputLayer();
            refreshHiddenLayers();
            refreshOutputLayer();
        } else {
            topologyWidget.contentContainer.getChildren().add(getActionBoldText("wczytaj dane uczące oraz dane testowe"));
        }
    }

    private void refreshSettings() {
        parametersWidget.contentContainer.getChildren().clear();

        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            refreshBiasSetting();
        } else {
            parametersWidget.contentContainer.getChildren().add(getActionBoldText("wczytaj dane uczące oraz dane testowe"));
        }
    }

    private void refreshInputLayer() {
        HBox hBox = getInputHBox();
        hBox.getChildren().add(getActionText("Warstwa wejściowa"));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getInputSize(), 0);
        hBox.getChildren().add(spinner);

        topologyWidget.contentContainer.getChildren().add(hBox);
    }

    private void refreshHiddenLayers() {
        for (int i = 1; i < neuralNetwork.getNeuralStructure().getLayers().size() - 1; i++) {
            HBox hBox = getInputHBox();
            hBox.getChildren().add(getActionText("Warstwa ukryta  (" + i + ")"));

            Spinner spinner;
            if (neuralNetwork.getNeuralStructure().isBias()) {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize() - 1, i + 1);
            } else {
                spinner = getSpinner(false, neuralNetwork.getNeuralStructure().getLayers().get(i).getLayerSize(), i + 1);
            }

            hBox.getChildren().add(spinner);
            hBox.getChildren().add(getDeleteLayerButton(i + 1));
            topologyWidget.contentContainer.getChildren().add(hBox);
        }

        topologyWidget.contentContainer.getChildren().add(getNewLayerButton());
    }

    private void refreshOutputLayer() {
        HBox hBox = getInputHBox();
        hBox.getChildren().add(getActionText("Warstwa wyjściowa"));

        Spinner spinner = getSpinner(true, neuralNetwork.getNeuralParameters().getOutputSize(), 1);
        hBox.getChildren().add(spinner);

        topologyWidget.contentContainer.getChildren().add(hBox);
    }

    private void refreshBiasSetting() {
        HBox hBox = getInputHBox();
        hBox.getChildren().add(getActionBoldText("Bias"));
        hBox.getChildren().add(getBiasCheckbox());

        parametersWidget.contentContainer.getChildren().add(hBox);
    }

    private HBox getInputHBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(2, 12, 2, 12));
        hBox.setSpacing(12);
        return hBox;
    }

    private Text getActionText(String name) {
        Text text = new Text(name);
        text.getStyleClass().add("action-status");
        return text;
    }

    private Text getActionBoldText(String name) {
        Text text = new Text(name);
        text.getStyleClass().add("action-bold-status");
        return text;
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
        button.setText("-");
        button.setPrefSize(32d, 32d);

        button.setOnAction(event -> {
            neuralNetwork.getNeuralStructure().deleteLayer(getLayerNumber(layerNumber));
            refreshWidget();
        });

        return button;
    }

    private Button getNewLayerButton() {
        Button button = new Button();
        button.setText("Dodaj warstwę");

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

    private CheckBox getBiasCheckbox() {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(neuralNetwork.getNeuralStructure().isBias());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                neuralNetwork.addBias();
            } else {
                neuralNetwork.deleteBias();
            }

            refreshWidget();
        });

        return checkBox;
    }
}
