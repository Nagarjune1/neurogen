package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.scene.control.Button;
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

    private Text textIterations;
    private Text textError;
    private Text textoObjectsOutOfTolerance;
    private Text textTime;

    private Timer timer;
    private Long startTime;

    private DecimalFormat errorDecimalFormat = new DecimalFormat("#.####");

    public LearningWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createPrimaryWidget("Panel kontrolny");
        initialize();
    }

    private void initialize() {
        initializeControlsContainer();
        initializeStatisticsContainer();
        initializeButtonActions();
        disableControls();

        neuralNetwork.getLearning().setLearningWidget(this);
    }

    private void initializeStatisticsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("STATYSTYKI UCZENIA", LayoutService.TextStyle.HEADING));

        HBox mainHBox = layoutService.getHBox(0d, 0d, 12d);
        vBox.getChildren().add(mainHBox);

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

        VBox timeVbox = layoutService.getVBox(8d, 16d, 4d);
        timeVbox.getStyleClass().add("stats-pane");
        timeVbox.setMinWidth(156d);
        textTime = layoutService.getText("nic tu nie ma...", LayoutService.TextStyle.STATUS_WHITE);
        timeVbox.getChildren().addAll(layoutService.getText("Czas uczenia", LayoutService.TextStyle.PARAGRAPH_WHITE), textTime);
        mainHBox.getChildren().add(timeVbox);

        contentContainer.getChildren().add(vBox);
    }

    private void initializeControlsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 16d);
        vBox.getChildren().add(layoutService.getText("KONTROLA UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox hBox = layoutService.getHBox(0d, 8d, 12d);
        hBox.getChildren().add(layoutService.getText("STEROWANIE", LayoutService.TextStyle.PARAGRAPH));
        buttonStartLearning = layoutService.getButton("Uruchom uczenie");
        buttonStopLearning = layoutService.getButton("Przewij uczenie");
        buttonStopLearning.setDisable(true);
        hBox.getChildren().addAll(buttonStartLearning, buttonStopLearning);
        vBox.getChildren().add(hBox);
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

    /* Timer */
    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                textTime.setText(String.valueOf((System.currentTimeMillis() - startTime) / 1000d) + " s");
            }}, 0, 50);
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

    public void endLearning() {
        switchButtons(buttonStopLearning);
        stopTimer();
    }
}
