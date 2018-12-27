package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;
import pl.wozniaktomek.util.TestChart;

public class TestWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Text networkStatus;

    private VBox chartContainer;
    private TestChart testChart;

    public TestWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Testowanie sieci");
        initialize();
    }

    public void refreshWidget() {
        if (neuralNetwork.getLearned()) {
            networkStatus.setText("Sieć przeszła przez proces uczenia.");

            if (neuralNetwork.getParameters().getInputSize().equals(2)) {
                initializeChart();
            } else {
                Platform.runLater(() ->
                        chartContainer.getChildren().add(layoutService.getText("Testowanie dostępne tylko dla danych dwuwymiarowych.", LayoutService.TextStyle.PARAGRAPH_THEME)));
            }

        } else {
            networkStatus.setText("Sieć nie przeszła jeszcze procesu uczenia.");
            clearChart();

            Platform.runLater(() ->
                    chartContainer.getChildren().add(layoutService.getText("Sieć musi przejść proces uczenia, by dostępny był panel testowania.", LayoutService.TextStyle.PARAGRAPH_THEME)));
        }
    }

    /* Initialization */
    private void initialize() {
        initializeStatusContainer();
        initializeChartContainer();
        refreshWidget();
    }

    private void initializeStatusContainer() {
        HBox networkStatusHBox = layoutService.getHBox(0d, 0d, 8d);
        networkStatusHBox .setAlignment(Pos.BASELINE_LEFT);

        networkStatus = layoutService.getText("Sieć nie przeszła jeszcze procesu uczenia.", LayoutService.TextStyle.HEADING);

        networkStatusHBox .getChildren().addAll(layoutService.getText("STAN SIECI:", LayoutService.TextStyle.HEADING), networkStatus);
        contentContainer.getChildren().add(networkStatusHBox);
    }

    private void initializeChartContainer() {
        chartContainer = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(chartContainer);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                testChart.setChartSize(ThesisApp.windowControl.getContentPane().getWidth() - 300, ThesisApp.windowControl.getContentPane().getHeight() - 282);

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

        testChart = new TestChart(neuralNetwork, 850, (int)(ThesisApp.windowControl.getContentPane().getHeight() - 282));
        initializeSizeListener();

        addChartToContainer();
    }
}