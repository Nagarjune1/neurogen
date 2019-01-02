package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;
import pl.wozniaktomek.layout.charts.TestChart;

public class TestWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Text networkStatus;
    private HBox buttonsContainer;

    private VBox chartContainer;
    private TestChart testChart;

    private Button clearButton;
    private Button generateButton;

    public TestWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Testowanie sieci");
        initialize();
    }

    public void refreshWidget() {
        if (neuralNetwork.getLearned()) {
            networkStatus.setText("Sieć przeszła przez proces uczenia.");

            if (neuralNetwork.getParameters().getInputSize().equals(2)) {
                initializeChartButtons();
                initializeChart();
                initializeButtonsAction();
            } else {
                clearButtons();
                clearChart();
                Platform.runLater(() ->
                        chartContainer.getChildren().add(layoutService.getText("Aktualnie testowanie dostępne jest tylko dla danych dwuwymiarowych.", LayoutService.TextStyle.HEADING)));
            }

        } else {
            networkStatus.setText("Sieć nie przeszła jeszcze procesu uczenia.");
            clearButtons();
            clearChart();

            Platform.runLater(() ->
                    chartContainer.getChildren().add(layoutService.getText("Sieć musi przejść proces uczenia, by dostępny był panel testowania.", LayoutService.TextStyle.PARAGRAPH_THEME)));
        }
    }

    /* Initialization */
    private void initialize() {
        initializeControlsContainer();
        initializeChartContainer();
        refreshWidget();
    }

    private void initializeControlsContainer() {
        HBox controlContainer = layoutService.getHBox(0d, 0d, 12d);
        contentContainer.getChildren().add(controlContainer);

        HBox networkStatusContainer = layoutService.getHBox(4d, 0d, 8d);
        networkStatusContainer.setAlignment(Pos.BASELINE_LEFT);
        networkStatus = layoutService.getText("Sieć nie przeszła jeszcze procesu uczenia.", LayoutService.TextStyle.HEADING);
        networkStatusContainer.getChildren().addAll(layoutService.getText("STAN SIECI:", LayoutService.TextStyle.HEADING), networkStatus);

        buttonsContainer = layoutService.getHBox(0d, 0d, 8d);

        controlContainer.getChildren().addAll(networkStatusContainer, buttonsContainer);
    }

    private void initializeChartContainer() {
        chartContainer = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(chartContainer);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                testChart.setChartSize(ThesisApp.windowControl.getContentPane().getWidth() - 300, ThesisApp.windowControl.getContentPane().getHeight() - 292);

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    /* Chart */
    private void clearChart() {
        Platform.runLater(() -> chartContainer.getChildren().clear());
    }

    private void addChartToContainer() {
        Platform.runLater(() -> chartContainer.getChildren().add(testChart.getChart()));
    }

    private void initializeChart() {
        clearChart();

        testChart = new TestChart(neuralNetwork, 850, (int)(ThesisApp.windowControl.getContentPane().getHeight() - 292));
        initializeSizeListener();

        addChartToContainer();
    }

    /* Buttons */
    private void clearButtons() {
        Platform.runLater(() -> buttonsContainer.getChildren().clear());
    }

    private void initializeChartButtons() {
        clearButton = layoutService.getButton("Wyczyść");
        generateButton = layoutService.getButton("Wygeneruj losowe punkty");

        Platform.runLater(() -> buttonsContainer.getChildren().addAll(clearButton, generateButton));
    }

    private void initializeButtonsAction() {
        clearButton.setOnAction(event -> testChart.clearChart());
        generateButton.setOnAction(event -> testChart.generateRandomObjects());
    }
}