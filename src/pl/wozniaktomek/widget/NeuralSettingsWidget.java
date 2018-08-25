package pl.wozniaktomek.widget;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.util.ArrayList;

public class NeuralSettingsWidget extends Widget {
    private HBox mainPane;

    private DefaultWidget topologyWidget;
    private DefaultWidget parametersWidget;

    private NeuralNetwork neuralNetwork;

    public NeuralSettingsWidget(NeuralNetwork neuralNetwork, String widgetTitle) {
        this.neuralNetwork = neuralNetwork;
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
    }

    private void refreshTopology() {
        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            topologyWidget.contentContainer.getChildren().clear();
            refreshInputLayer();
            refreshHiddenLayers();
            refreshOutputLayer();
        }
    }

    private void refreshInputLayer() {
        HBox hBox = getInputHBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(getLayerName("Warstwa wejściowa"));

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 2);
        spinner.setValueFactory(valueFactory);
        spinner.setDisable(true);
        hBox.getChildren().add(spinner);

        topologyWidget.contentContainer.getChildren().add(hBox);
    }

    private void refreshHiddenLayers() {
        ArrayList<HBox> hBoxes = new ArrayList<>();

        for (HBox hBox : hBoxes) {
            topologyWidget.contentContainer.getChildren().add(hBox);
        }
    }

    private void refreshOutputLayer() {
        HBox hBox = getInputHBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(getLayerName("Warstwa wyjściowa"));

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 2);
        spinner.setValueFactory(valueFactory);
        spinner.setDisable(true);
        hBox.getChildren().add(spinner);

        topologyWidget.contentContainer.getChildren().add(hBox);
    }

    private void refreshSettings() {

    }

    private HBox getInputHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 12, 2, 12));
        hBox.setSpacing(12);
        return hBox;
    }

    private Text getLayerName(String name) {
        Text text = new Text(name);
        text.getStyleClass().add("action-status");
        return text;
    }
}
