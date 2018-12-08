package pl.wozniaktomek.layout.widget;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pl.wozniaktomek.ThesisApp;
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
            networkStatus.setText("Sieć nauczona");
            buttonLoadData.setDisable(false);
        } else {
            networkStatus.setText("Sieć nie przeszła jeszcze procesu uczenia...");
            buttonLoadData.setDisable(true);
        }
    }

    private void initialize() {
        initializeStatusContainers();
        initializeTableContainer();
        refreshWidget();
    }

    private void initializeStatusContainers() {
        VBox mainVBox = layoutService.getVBox(0d, 0d, 12d);
        contentContainer.getChildren().add(mainVBox);

        HBox buttonHBox = layoutService.getHBox(0d, 0d, 8d);
        buttonHBox.setAlignment(Pos.BASELINE_LEFT);
        dataStatus = layoutService.getText("Nie wczytano danych...", LayoutService.TextStyle.HEADING);
        buttonHBox.getChildren().addAll(getLoadDataButton(), layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING), dataStatus);
        mainVBox.getChildren().add(buttonHBox);

        HBox statusHBox = layoutService.getHBox(0d, 0d, 8d);
        statusHBox.setAlignment(Pos.BASELINE_LEFT);
        networkStatus = layoutService.getText("Sieć nie przeszła jeszcze procesu uczenia...", LayoutService.TextStyle.HEADING);
        statusHBox.getChildren().addAll(layoutService.getText("STAN SIECI:", LayoutService.TextStyle.HEADING), networkStatus);
        mainVBox.getChildren().add(statusHBox);
    }

    private void initializeTableContainer() {
        tableContainer = layoutService.getVBox(0d, 0d, 0d);
        contentContainer.getChildren().add(tableContainer);
    }

    private Button getLoadDataButton() {
        buttonLoadData = layoutService.getButton("Wczytaj dane weryfikujące");

        buttonLoadData.setOnAction(event -> {
            verificationData = new DataTransferService().readFromFile();
            validateVerificationData();
        });

        return buttonLoadData;
    }

    private void validateVerificationData() {
        ValidationService validationService = new ValidationService(neuralNetwork.getParameters());
        if (validationService.validateObjects(verificationData, true)) {
            dataStatus.setText("Dane poprawne");
            setButtonStyle(true);
            refreshTable();
        } else {
            dataStatus.setText("Dane nieprawidłowe");
            setButtonStyle(false);
            tableContainer.getChildren().clear();
        }
    }

    private void refreshTable() {
        ParametersService parametersService = new ParametersService(neuralNetwork);
        parametersService.setCorrectAnswers(verificationData);

        table = new TableView<>();
        table.setPrefHeight(ThesisApp.stage.getHeight() - 365);
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

            if (i == columnNames.size() - 1) {
                createCellFactory(column);
            }

            table.getColumns().add(column);
        }
    }

    private void createCellFactory(TableColumn<List<String>, String> tableColumn) {
        tableColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty ? "" : getItem());
                setGraphic(null);

                TableRow<List<String>> row = getTableRow();

                if (!isEmpty()) {
                    if (Double.parseDouble(item.replace(",", ".")) <= neuralNetwork.getLearning().getLearningTolerance()) {
                        row.setStyle("-fx-background-color: rgba(16, 66, 16, 0.9)");
                    } else {
                        row.setStyle("-fx-background-color: rgba(145, 8, 12, 0.9)");
                    }
                }
            }
        });
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

        columnNames.add("E");

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
            List<Neuron> neuronsInLastLayer = startupService.getLastLayerNeurons(verificationData.get(i));

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
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> table.setPrefHeight(ThesisApp.stage.getHeight() - 365);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
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
