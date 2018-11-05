package pl.wozniaktomek.layout.widget;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
        createPrimaryWidget("Uruchomienie sieci");
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
        dataStatus = layoutService.getText("Nie wczytano danych...", LayoutService.TextStyle.STATUS);
        buttonHBox.getChildren().addAll(getLoadDataButton(), layoutService.getText("STATUS:", LayoutService.TextStyle.HEADING), dataStatus);
        mainVBox.getChildren().add(buttonHBox);

        HBox statusHBox = layoutService.getHBox(0d, 0d, 8d);
        networkStatus = layoutService.getText("Sieć nie przeszła jeszcze procesu uczenia...", LayoutService.TextStyle.STATUS);
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
        if (validationService.validateObjects(verificationData)) {
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
        table.setEditable(false);

        tableContainer.getChildren().clear();
        tableContainer.getChildren().add(table);

        createTableColumns();
        fillTable();
    }

    private void createTableColumns() {
        List<String> columnNames = createColumnNames();

        for (int i = 0; i <columnNames.size(); i++) {
            TableColumn<List<String>, String> column = new TableColumn<>(columnNames.get(i));

            final int finalId = i;
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalId)));

            column.setSortable(false);
            column.setReorderable(false);

            column.setMaxWidth(128d);
            column.setMinWidth(64d);

            table.getColumns().add(column);
        }
    }

    private List<String> createColumnNames() {
        List<String> columnNames = new ArrayList<>();

        columnNames.add("id");

        for (int i = 0; i < neuralNetwork.getParameters().getInputSize(); i++) {
            columnNames.add("x[" + (i + 1) + "]");
        }

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("d[" + (i + 1) + "]");
        }

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("y[" + (i + 1) + "]");
        }

        for (int i = 0; i < neuralNetwork.getParameters().getOutputSize(); i++) {
            columnNames.add("error[" + (i + 1) + "]");
        }

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
                row.add(String.format("%.10f", neuron.getOutput()));
            }

            for (Neuron neuron : neuronsInLastLayer) {
                row.add(String.format("%.10f", neuron.getOutputError()));
            }

            table.getItems().add(row);
        }
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
