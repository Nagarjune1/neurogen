package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class LearningWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Button buttonStartLearning;
    private Button buttonStopLearning;

    private Text textTime;
    private Text textIterations;
    private Text textError;
    private Text textoObjectsOutOfTolerance;

    private Timer timer;
    private Long startTime;

    private DecimalFormat errorDecimalFormat = new DecimalFormat("#.####");
    private DecimalFormat weightDecimalFormat = new DecimalFormat("#.##");
    private DecimalFormat timeDecimalFormat = new DecimalFormat("#.#");

    private VBox weightPane;

    public LearningWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createPrimaryWidget("Panel kontrolny");
        initialize();
    }

    private void initialize() {
        initializeControlsContainer();
        initializeStatisticsContainer();
        initializeWeightsContainer();
        initializeButtonActions();
        disableControls();

        neuralNetwork.getLearning().setLearningWidget(this);
    }

    private void initializeStatisticsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("STATYSTYKI UCZENIA", LayoutService.TextStyle.HEADING));

        HBox mainHBox = layoutService.getHBox(0d, 0d, 12d);
        vBox.getChildren().add(mainHBox);

        VBox timeVbox = layoutService.getVBox(8d, 16d, 4d);
        timeVbox.getStyleClass().add("stats-pane");
        timeVbox.setMinWidth(156d);
        textTime = layoutService.getText("nic tu nie ma...", LayoutService.TextStyle.STATUS_WHITE);
        timeVbox.getChildren().addAll(layoutService.getText("Czas uczenia", LayoutService.TextStyle.PARAGRAPH_WHITE), textTime);
        mainHBox.getChildren().add(timeVbox);

        VBox iterationVbox = layoutService.getVBox(8d, 16d, 4d);
        iterationVbox.getStyleClass().add("stats-pane");
        iterationVbox.setMinWidth(156d);
        textIterations = layoutService.getText("nic tu nie ma...", LayoutService.TextStyle.STATUS_WHITE);
        iterationVbox.getChildren().addAll(layoutService.getText("Iteracje", LayoutService.TextStyle.PARAGRAPH_WHITE), textIterations);
        mainHBox.getChildren().add(iterationVbox);

        VBox errorVbox = layoutService.getVBox(8d, 16d, 4d);
        errorVbox.getStyleClass().add("stats-pane");
        errorVbox.setMinWidth(156d);
        textError = layoutService.getText("nic tu nie ma...", LayoutService.TextStyle.STATUS_WHITE);
        errorVbox.getChildren().addAll(layoutService.getText("Błąd całkowity", LayoutService.TextStyle.PARAGRAPH_WHITE), textError);
        mainHBox.getChildren().add(errorVbox);

        VBox objectsVbox = layoutService.getVBox(8d, 16d, 4d);
        objectsVbox.getStyleClass().add("stats-pane");
        objectsVbox.setMinWidth(156d);
        textoObjectsOutOfTolerance = layoutService.getText("nic tu nie ma...", LayoutService.TextStyle.STATUS_WHITE);
        objectsVbox.getChildren().addAll(layoutService.getText("Dane poza tolerancją", LayoutService.TextStyle.PARAGRAPH_WHITE), textoObjectsOutOfTolerance);
        mainHBox.getChildren().add(objectsVbox);

        contentContainer.getChildren().add(vBox);
    }

    private void initializeControlsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 16d);
        vBox.getChildren().add(layoutService.getText("KONTROLA UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox hbox = layoutService.getHBox(0d, 8d, 12d);
        hbox.getChildren().add(layoutService.getText("STEROWANIE", LayoutService.TextStyle.PARAGRAPH));
        buttonStartLearning = layoutService.getButton("Uruchom uczenie");
        buttonStopLearning = layoutService.getButton("Przewij uczenie");
        buttonStopLearning.setDisable(true);
        hbox.getChildren().addAll(buttonStartLearning, buttonStopLearning);

        hbox.getChildren().add(new Separator(Orientation.VERTICAL));

        hbox.getChildren().addAll(getInterfaceUpdateCheckbox(), getWeightUpdateCheckbox());

        vBox.getChildren().add(hbox);
    }

    private void initializeWeightsContainer() {
        weightPane = layoutService.getVBox(4d, 4d, 4d);
        contentContainer.getChildren().add(weightPane);
    }

    private void initializeButtonActions() {
        buttonStartLearning.setOnAction(event -> startLearning());
        buttonStopLearning.setOnAction(event -> stopLearning());
    }

    private void startLearning() {
        neuralNetwork.startLearning();
        switchButtons(buttonStartLearning);
        startTimer();
    }

    private void stopLearning() {
        neuralNetwork.stopLearning();
        switchButtons(buttonStopLearning);
        stopTimer();
    }

    private void switchButtons(Button button) {
        if (button.equals(buttonStartLearning)) {
            buttonStartLearning.setDisable(true);
            buttonStopLearning.setDisable(false);
        } else {
            buttonStartLearning.setDisable(false);
            buttonStopLearning.setDisable(true);
        }
    }

    /* Interface control */
    public void disableControls() {
        buttonStartLearning.setDisable(true);
        buttonStopLearning.setDisable(true);
    }

    public void enableControls() {
        buttonStartLearning.setDisable(false);
    }

    public void endLearning() {
        switchButtons(buttonStopLearning);
        stopTimer();
    }

    /* Timer */
    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                textTime.setText(timeDecimalFormat.format((System.currentTimeMillis() - startTime) / 1000d) + " s");
            }
        }, 0, 50);
    }

    private void stopTimer() {
        timer.cancel();
    }

    /* Interface update */
    public void updateIteration(Integer iteration) {
        Platform.runLater(() -> this.textIterations.setText(String.valueOf(iteration)));
    }

    public void updateError(Double error) {
        Platform.runLater(() -> this.textError.setText(errorDecimalFormat.format(error)));
    }

    public void updateObjectsOutOfTolerance(String objects) {
        Platform.runLater(() -> this.textoObjectsOutOfTolerance.setText(objects));
    }

    public void updateWeightsPane() {
        Platform.runLater(this::createWeightsPane);
    }

    /* Weights pane updating */
    private void clearWeightsPane() {
        weightPane.getChildren().clear();
    }

    private void createWeightsPane() {
        clearWeightsPane();

        FlowPane flowPane = getWeightsFlowPane();
        for (int i = 0; i < neuralNetwork.getStructure().getConnections().size(); i++) {
            addWeight(i, flowPane);
        }

        weightPane.getChildren().add(flowPane);
        neuralNetwork.getLearning().setIsInterfaceUpdating(false);
    }

    private void addWeight(Integer weightNumer, FlowPane flowPane) {
        HBox hBox = layoutService.getHBox(0d, 0d, 8d);
        hBox.getStyleClass().add("stats-pane-white");
        hBox.getChildren().add(layoutService.getText("[WAGA " + (weightNumer + 1) + "]", LayoutService.TextStyle.STATUS));
        hBox.getChildren().add(layoutService.getText(weightDecimalFormat.format(neuralNetwork.getStructure().getConnections().get(weightNumer).getWeight()), LayoutService.TextStyle.PARAGRAPH_THEME));

        flowPane.getChildren().add(hBox);
    }

    private FlowPane getWeightsFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(12d);
        flowPane.setVgap(12d);
        return flowPane;
    }

    /* Controls initialization */
    private CheckBox getInterfaceUpdateCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Aktualizacja statystyk", null);
        checkBox.setSelected(neuralNetwork.getLearning().getInterfaceUpdating());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> neuralNetwork.getLearning().setInterfaceUpdating(newValue));
        return checkBox;
    }

    private CheckBox getWeightUpdateCheckbox() {
        CheckBox checkBox = layoutService.getCheckBox("Aktualizacja wartości wag", null);
        checkBox.setSelected(neuralNetwork.getLearning().getWeightsUpdating());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            neuralNetwork.getLearning().setWeightsUpdating(newValue);

            if (!newValue) {
                clearWeightsPane();
            }
        });
        return checkBox;
    }
}
