package pl.wozniaktomek.layout.widget;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.NeuroGenApp;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.service.ParametersService;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.service.ValidationService;
import pl.wozniaktomek.neural.structure.Neuron;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.service.DataTransferService;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;
import java.util.List;

public class StartupWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private DataTransferService dataTransferService;

    private VBox tableContainer;
    private TableView<List<String>> table;

    private Button buttonLoadData;
    private Text dataStatus;
    private Text networkStatus;

    private ArrayList<NeuralObject> verificationData;

    public StartupWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Uruchomienie sieci");
        initialize();
    }

    public void refreshWidget() {
        if (neuralNetwork.getLearned()) {
            networkStatus.setText("Sieć przeszła przez proces uczenia.");
            buttonLoadData.setDisable(false);
        } else {
            networkStatus.setText("Sieć nie przeszła jeszcze procesu uczenia.");
            buttonLoadData.setDisable(true);
        }
    }

    /* Initialization */
    private void initialize() {
        initializeStatusContainers();
        initializeTableContainer();
        refreshWidget();
    }

    private void initializeStatusContainers() {
        HBox networkStatusHBox = layoutService.getHBox(0d, 0d, 8d);
        networkStatusHBox .setAlignment(Pos.BASELINE_LEFT);
        networkStatus = layoutService.getText("Sieć nie przeszła jeszcze procesu uczenia...", LayoutService.TextStyle.HEADING);
        networkStatusHBox .getChildren().addAll(layoutService.getText("STAN SIECI:", LayoutService.TextStyle.HEADING), networkStatus);

        HBox buttonContainer = layoutService.getHBox(0d, 0d, 0d);
        buttonContainer.getChildren().add(getLoadDataButton());
        buttonContainer.setAlignment(Pos.BASELINE_LEFT);

        dataStatus = layoutService.getText("Nie wczytano danych.", LayoutService.TextStyle.HEADING);
        HBox statusContainer = layoutService.getHBox(0d, 4d, 6d);
        statusContainer.getChildren().addAll(layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING), dataStatus);
        statusContainer.setAlignment(Pos.CENTER_LEFT);

        HBox loadDataHBox = layoutService.getHBox(0d, 0d, 16d);
        loadDataHBox.getChildren().addAll(buttonContainer, statusContainer);

        contentContainer.getChildren().addAll(networkStatusHBox, loadDataHBox);
    }

    private void initializeTableContainer() {
        tableContainer = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(tableContainer);
    }

    private Button getLoadDataButton() {
        buttonLoadData = layoutService.getButton("Wczytaj dane weryfikujące");

        buttonLoadData.setOnAction(event -> {
            dataTransferService = new DataTransferService();
            verificationData = dataTransferService.readFromFile();
            validateVerificationData();
        });

        return buttonLoadData;
    }

    private void validateVerificationData() {
        ValidationService validationService = new ValidationService(neuralNetwork.getParameters());
        if (validationService.validateObjects(verificationData, true)) {
            setButtonStyle(true);
            refreshTable();
        } else {
            setButtonStyle(false);
            tableContainer.getChildren().clear();
        }

        dataStatus.setText(dataTransferService.getTransferStatus());
    }

    private void refreshTable() {
        ParametersService parametersService = new ParametersService(neuralNetwork);
        parametersService.setCorrectAnswers(verificationData);

        table = new TableView<>();
        table.setPrefHeight(NeuroGenApp.stage.getHeight() - 368);
        table.setEditable(false);

        tableContainer.getChildren().clear();
        tableContainer.getChildren().add(table);

        fillTable();
        createTableColumns();
        initializeTableSizeListener();
    }

    private void createTableColumns() {
        List<String> columnNames = createColumnNames();

        for (int i = 0; i < columnNames.size(); i++) {
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames.get(i));

            final int finalId = i;
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalId)));

            column.setSortable(false);
            column.setReorderable(false);
            column.setMaxWidth(128d);
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

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("D[" + (i + 1) + "]");
        }

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("Y[" + (i + 1) + "]");
        }

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("E[" + (i + 1) + "]");
        }

        columnNames.add("E[SUM]");

        return columnNames;
    }

    private void fillTable() {
        for (int i = 0; i < verificationData.size(); i++) {
            List<String> row = new ArrayList<>();

            row.add(String.valueOf(i + 1));

            for (Double value : verificationData.get(i).getInputValues()) {
                row.add(String.format("%.5f", value));
            }

            for (Double value : verificationData.get(i).getCorrectAnswer()) {
                row.add(String.valueOf(value));
            }

            StartupService startupService = new StartupService(neuralNetwork);
            List<Neuron> neuronsInLastLayer = startupService.getLastLayerNeurons(verificationData.get(i), true);

            for (Neuron neuron : neuronsInLastLayer) {
                row.add(String.format("%.5f", neuron.getOutput()));
            }

            double error = 0d;
            for (Neuron neuron : neuronsInLastLayer) {
                row.add(String.format("%.5f", neuron.getOutputError()));
                error += neuron.getOutputError();
            }

            row.add(String.format("%.5f", error));

            table.getItems().add(row);
        }
    }

    private void initializeTableSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> table.setPrefHeight(NeuroGenApp.stage.getHeight() - 368);
        NeuroGenApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void setButtonStyle(Boolean state) {
        buttonLoadData.getStyleClass().remove("button-success");
        buttonLoadData.getStyleClass().remove("button-failure");

        if (state != null) {
            if (state) {
                buttonLoadData.getStyleClass().add("button-success");
            } else {
                buttonLoadData.getStyleClass().add("button-failure");
            }
        }
    }
}
