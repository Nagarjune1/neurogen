package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.layout.widget.neural.NeuralDataWidget;
import pl.wozniaktomek.layout.widget.neural.NeuralSettingsWidget;
import pl.wozniaktomek.layout.widget.neural.NeuralTopologyWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;

public class NeuralControl implements Initializable {
    @FXML private HBox titleContainer;
    @FXML private VBox contentContainer;

    @FXML private AnchorPane settingsTabPane;
    @FXML private AnchorPane learningTabPane;
    @FXML private AnchorPane startupTabPane;

    @FXML private VBox settingsTab;
    @FXML private VBox learningTab;
    @FXML private VBox startupTab;

    private NeuralNetwork neuralNetwork;

    private NeuralDataWidget neuralDataWidget;
    private NeuralSettingsWidget neuralSettingsWidget;
    private NeuralTopologyWidget neuralTopologyWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNeuralNetwork();
        initializeReadDataWidget();
        initializeSettingsWidget();
        initializeNetworkTopologyWidget();
        initializeSizeListener();
    }

    public void refreshSettings() {
        neuralSettingsWidget.refreshWidget();
    }

    public void refreshTopology() {
        neuralTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 48);
    }

    private void initializeNeuralNetwork() {
        neuralNetwork = new NeuralNetwork();
    }

    private void initializeReadDataWidget() {
        neuralDataWidget = new NeuralDataWidget(this, neuralNetwork);
        settingsTab.getChildren().add(neuralDataWidget.getWidget());
    }

    private void initializeSettingsWidget() {
        neuralSettingsWidget = new NeuralSettingsWidget(neuralNetwork, this);
        settingsTab.getChildren().add(neuralSettingsWidget.getWidget());
    }

    private void initializeNetworkTopologyWidget() {
        neuralTopologyWidget = new NeuralTopologyWidget(neuralNetwork);
        settingsTab.getChildren().add(neuralTopologyWidget.getWidget());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            contentContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());

            settingsTab.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 36);
            settingsTabPane.setPrefSize(ThesisApp.windowControl.getContentPane().getWidth(), ThesisApp.windowControl.getContentPane().getHeight());

            learningTab.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 36);
            learningTabPane.setPrefSize(ThesisApp.windowControl.getContentPane().getWidth(), ThesisApp.windowControl.getContentPane().getHeight());

            startupTab.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 36);
            startupTabPane.setPrefSize(ThesisApp.windowControl.getContentPane().getWidth(), ThesisApp.windowControl.getContentPane().getHeight());

            refreshTopology();
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }
}
