package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.NeuroGenApp;
import pl.wozniaktomek.layout.charts.TestChart;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.service.DataTransferService;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Text networkStatus;

    private HBox buttonsContainer;
    private VBox resultsContainer;

    private TestChart testChart;

    private ArrayList<NeuralObject> testingData;
    private TableView<List<String>> table;

    private Button clearButton;
    private Button generateButton;
    private Button loadButton;

    public TestWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Testowanie sieci");
        initialize();
    }

    /* Refreshing */
    public void refreshWidget() {
        if (neuralNetwork.getLearned()) {
            networkStatus.setText("Sieć przeszła przez proces uczenia.");

            if (neuralNetwork.getParameters().getInputSize().equals(2)) {
                refreshTwoDimensionalData();
            } else {
                refreshMultiDimensionalData();
            }

        } else {
            networkStatus.setText("Sieć nie przeszła jeszcze procesu uczenia.");
            clearContainers();

            Platform.runLater(() ->
                    resultsContainer.getChildren().add(layoutService.getText("Sieć musi przejść proces uczenia, by dostępny był panel testowania.", LayoutService.TextStyle.PARAGRAPH_THEME)));
        }
    }

    private void refreshTwoDimensionalData() {
        clearContainers();

        initializeButtons(true);
        initializeChart();
        initializeButtonsAction(true);
    }

    private void refreshMultiDimensionalData() {
        clearContainers();
        initializeButtons(false);
        initializeButtonsAction(false);
    }

    private void clearContainers() {
        clearButtonsContainer();
        clearResultsContainer();
    }

    private void clearButtonsContainer() {
        Platform.runLater(() -> buttonsContainer.getChildren().clear());
    }

    private void clearResultsContainer() {
        Platform.runLater(() -> resultsContainer.getChildren().clear());
    }

    /* Initialization */
    private void initialize() {
        initializeControlsContainer();
        initializeResultsContainer();
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

    private void initializeResultsContainer() {
        resultsContainer = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(resultsContainer);
    }

    private void initializeChartSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                testChart.setChartSize(NeuroGenApp.windowControl.getContentPane().getWidth() - 300, NeuroGenApp.windowControl.getContentPane().getHeight() - 292);

        NeuroGenApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        NeuroGenApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    /* Chart */
    private void addChartToContainer() {
        Platform.runLater(() -> resultsContainer.getChildren().add(testChart.getChart()));
    }

    private void initializeChart() {
        clearResultsContainer();

        testChart = new TestChart(neuralNetwork, 850, (int) (NeuroGenApp.windowControl.getContentPane().getHeight() - 292));
        initializeChartSizeListener();

        addChartToContainer();
    }

    /* Table */
    private void initializeTable() {
        clearResultsContainer();

        table = new TableView<>();
        table.setPrefHeight(NeuroGenApp.stage.getHeight() - 332);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        fillTable();
        createTableColumns();
        initializeTableSizeListener();
        addTableToContainer();
    }

    private void fillTable() {
        StartupService startupService = new StartupService(neuralNetwork);

        for (int i = 0; i < testingData.size(); i++) {
            List<String> row = new ArrayList<>();

            row.add(String.valueOf(i + 1));

            for (Double value : testingData.get(i).getInputValues()) {
                row.add(String.format("%.5f", value));
            }

            row.add(String.valueOf(startupService.getObjectClass(testingData.get(i))));
            table.getItems().add(row);
        }
    }

    private void createTableColumns() {
        List<String> columnNames = createColumnNames();

        for (int i = 0; i < columnNames.size(); i++) {
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames.get(i));

            final int finalId = i;
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalId)));
            column.setSortable(false);
            column.setReorderable(false);
            column.setMinWidth(32d);

            table.getColumns().add(column);
        }
    }

    private List<String> createColumnNames() {
        List<String> columnNames = new ArrayList<>();

        columnNames.add("ID");

        for (int i = 0; i < neuralNetwork.getParameters().getInputSize(); i++) {
            columnNames.add("X[" + (i + 1) + "]");
        }

        columnNames.add("Klasa");

        return columnNames;
    }

    private void initializeTableSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> table.setPrefHeight(NeuroGenApp.stage.getHeight() - 332);
        NeuroGenApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void addTableToContainer() {
        Platform.runLater(() -> resultsContainer.getChildren().add(table));
    }

    /* Buttons */
    private void initializeButtons(Boolean isTwoDimensional) {
        clearButton = layoutService.getButton("Wyczyść");
        generateButton = layoutService.getButton("Wygeneruj losowe obiekty");

        if (isTwoDimensional) {
            Platform.runLater(() -> buttonsContainer.getChildren().addAll(clearButton, generateButton));
        } else {
            loadButton = layoutService.getButton("Wczytaj zbiór danych");
            Platform.runLater(() -> buttonsContainer.getChildren().addAll(clearButton, generateButton, new Separator(Orientation.VERTICAL), loadButton));
        }
    }

    private void initializeButtonsAction(Boolean isTwoDimensional) {
        if (isTwoDimensional) {
            clearButton.setOnAction(event -> testChart.clearChart());
            generateButton.setOnAction(event -> testChart.generateRandomObjects());
        } else {
            clearButton.setOnAction(event -> clearResultsContainer());
            generateButton.setOnAction(event -> {
                generateTestingData();
                initializeTable();
            });

            loadButton.setOnAction(event -> {
                DataTransferService dataTransferService = new DataTransferService();
                testingData = dataTransferService.readFromFile();

                if (validateLoadedDataSize()) {
                    initializeTable();
                } else {
                    clearResultsContainer();
                }
            });
        }
    }

    /* Data operations */
    private boolean validateLoadedDataSize() {
        for (NeuralObject neuralObject : testingData) {
            if (neuralObject.getInputValues().size() != neuralNetwork.getParameters().getInputSize()) {
                return false;
            }
        }

        return true;
    }

    private void generateTestingData() {
        testingData = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            ArrayList<Double> inputValues = new ArrayList<>();

            for (int j = 0; j < neuralNetwork.getParameters().getInputSize(); j++) {
                inputValues.add(ThreadLocalRandom.current().nextDouble(-10d, 10d));
            }

            testingData.add(new NeuralObject(inputValues, null));
        }
    }
}