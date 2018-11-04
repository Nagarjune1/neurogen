package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.layout.widget.LoadDataWidget;
import pl.wozniaktomek.layout.widget.LearningWidget;
import pl.wozniaktomek.layout.widget.SettingsWidget;
import pl.wozniaktomek.layout.widget.TopologyWidget;
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

    private LoadDataWidget loadDataWidget;
    private SettingsWidget settingsWidget;
    private TopologyWidget neuralTopologyWidget;
    private LearningWidget learningWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNeuralNetwork();
        initializeReadDataWidget();
        initializeSettingsWidget();
        initializeNetworkTopologyWidget();
        initializeLearningWidget();
        initializeSizeListener();
    }

    public void refreshSettings() {
        settingsWidget.refreshWidget();
    }

    public void refreshTopology() {
        neuralTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 54);
    }

    private void initializeNeuralNetwork() {
        neuralNetwork = new NeuralNetwork();
    }

    private void initializeReadDataWidget() {
        loadDataWidget = new LoadDataWidget(this, neuralNetwork);
        settingsTab.getChildren().add(loadDataWidget.getWidget());
    }

    private void initializeSettingsWidget() {
        settingsWidget = new SettingsWidget(neuralNetwork, this);
        settingsTab.getChildren().add(settingsWidget.getWidget());
    }

    private void initializeNetworkTopologyWidget() {
        neuralTopologyWidget = new TopologyWidget(neuralNetwork);
        settingsTab.getChildren().add(neuralTopologyWidget.getWidget());
    }

    private void initializeLearningWidget() {
        learningWidget = new LearningWidget(neuralNetwork);
        learningTab.getChildren().add(learningWidget.getWidget());
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
