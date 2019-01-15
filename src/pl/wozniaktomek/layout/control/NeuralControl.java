package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.NeuroGenApp;
import pl.wozniaktomek.layout.widget.*;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;

public class NeuralControl implements Initializable {
    @FXML private HBox titleContainer;
    @FXML private VBox contentContainer;

    @FXML private AnchorPane settingsTabPane;
    @FXML private AnchorPane learningTabPane;
    @FXML private AnchorPane startupTabPane;
    @FXML private AnchorPane testTabPane;

    @FXML private VBox settingsTab;
    @FXML private VBox learningTab;
    @FXML private VBox startupTab;
    @FXML private VBox testTab;

    private NeuralNetwork neuralNetwork;

    private SettingsWidget settingsWidget;
    private TopologyVisualizationWidget topologyVisualizationWidget;
    private StartupWidget startupWidget;
    private TestWidget testWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNeuralNetwork();
        initializeReadDataWidget();
        initializeSettingsWidget();
        initializeNetworkTopologyWidget();
        initializeLearningWidget();
        initializeStartupWidget();
        initializeTestWidget();
        initializeSizeListener();

        minimizeSettingsWidgets();
        neuralNetwork.setNeuralControl(this);
    }

    /* Refreshing */
    public void refreshSettings() {
        settingsWidget.refreshWidget();
    }

    public void refreshTopology() {
        topologyVisualizationWidget.drawNetwork(NeuroGenApp.windowControl.getContentPane().getWidth() - 54);
    }

    /* Initializting */
    private void initializeNeuralNetwork() {
        neuralNetwork = new NeuralNetwork();
    }

    private void initializeReadDataWidget() {
        LoadDataWidget loadDataWidget = new LoadDataWidget(this, neuralNetwork);
        settingsTab.getChildren().add(loadDataWidget.getWidget());
    }

    private void initializeSettingsWidget() {
        settingsWidget = new SettingsWidget(neuralNetwork, this);
        settingsTab.getChildren().add(settingsWidget.getWidget());
    }

    private void initializeNetworkTopologyWidget() {
        topologyVisualizationWidget = new TopologyVisualizationWidget(neuralNetwork);
        settingsTab.getChildren().add(topologyVisualizationWidget.getWidget());
    }

    private void initializeLearningWidget() {
        LearningWidget learningWidget = new LearningWidget(neuralNetwork);
        learningTab.getChildren().add(learningWidget.getWidget());
    }

    private void initializeStartupWidget() {
        startupWidget = new StartupWidget(neuralNetwork);
        startupTab.getChildren().add(startupWidget.getWidget());
    }

    private void initializeTestWidget() {
        testWidget = new TestWidget(neuralNetwork);
        testTab.getChildren().add(testWidget.getWidget());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth() - 82);
            contentContainer.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth());

            settingsTab.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth() - 36);
            settingsTabPane.setPrefSize(NeuroGenApp.windowControl.getContentPane().getWidth(), NeuroGenApp.windowControl.getContentPane().getHeight());

            learningTab.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth() - 36);
            learningTabPane.setPrefSize(NeuroGenApp.windowControl.getContentPane().getWidth(), NeuroGenApp.windowControl.getContentPane().getHeight());

            startupTab.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth() - 36);
            startupTabPane.setPrefSize(NeuroGenApp.windowControl.getContentPane().getWidth(), NeuroGenApp.windowControl.getContentPane().getHeight());

            testTab.setPrefWidth(NeuroGenApp.windowControl.getContentPane().getWidth() - 36);
            testTabPane.setPrefSize(NeuroGenApp.windowControl.getContentPane().getWidth(), NeuroGenApp.windowControl.getContentPane().getHeight());

            refreshTopology();
        };

        NeuroGenApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        NeuroGenApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    /* Widgets expanding */
    public void minimizeSettingsWidgets() {
        settingsWidget.minimizeWidget();
        topologyVisualizationWidget.minimizeWidget();
    }

    public void maximizeSettingsWidgets() {
        settingsWidget.maximizeWidget();
        topologyVisualizationWidget.maximizeWidget();
    }

    /* Getters */
    public StartupWidget getStartupWidget() {
        return startupWidget;
    }

    public TestWidget getTestWidget() {
        return testWidget;
    }
}
