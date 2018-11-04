package pl.wozniaktomek.layout.widget;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

public class LearningWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Button buttonStartLearning;
    private Button buttonStopLearning;

    private Text iterations;
    private Text error;

    public LearningWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createPrimaryWidget("Panel kontrolny");
        initialize();
    }

    private void initialize() {
        initializeStatisticsContainer();
        initializeControlsContainer();
        initializeButtonActions();
        disableControls();

        neuralNetwork.getLearning().setLearningWidget(this);
    }

    private void initializeStatisticsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("STATYSTYKI UCZENIA", LayoutService.TextStyle.HEADING));

        HBox mainHBox = layoutService.getHBox(0d, 0d, 4d);
        vBox.getChildren().add(mainHBox);

        HBox hBox = layoutService.getHBox(0d, 8d, 4d);
        iterations = layoutService.getText("nie uruchomiono sieci...", LayoutService.TextStyle.STATUS);
        hBox.getChildren().addAll(layoutService.getText("Iteracje:", LayoutService.TextStyle.STATUS), iterations);
        mainHBox.getChildren().add(hBox);

        mainHBox.getChildren().add(new Separator(Orientation.VERTICAL));

        hBox = layoutService.getHBox(0d, 8d, 4d);
        error = layoutService.getText("nie uruchomiono sieci...", LayoutService.TextStyle.STATUS);
        hBox.getChildren().addAll(layoutService.getText("Błąd całkowity:", LayoutService.TextStyle.STATUS), error);
        mainHBox.getChildren().add(hBox);

        contentContainer.getChildren().add(vBox);
    }

    private void initializeControlsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 16d);
        vBox.getChildren().add(layoutService.getText("KONTROLA UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox hBox = layoutService.getHBox(0d, 8d, 12d);
        hBox.getChildren().add(layoutService.getText("STEROWANIE", LayoutService.TextStyle.STATUS));
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
    }

    private void stopLearning() {
        neuralNetwork.stopLearning();
        switchButtons(buttonStopLearning);
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

    /* Interface update */
    public void updateInterface(Integer iterations, Double error) {
        Platform.runLater(() -> this.iterations.setText(String.valueOf(iterations)));
        Platform.runLater(() -> this.error.setText(String.valueOf(error)));
    }

    public void endLearning() {
        switchButtons(buttonStopLearning);
    }
}
