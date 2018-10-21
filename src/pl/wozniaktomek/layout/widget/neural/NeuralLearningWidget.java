package pl.wozniaktomek.layout.widget.neural;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

public class NeuralLearningWidget extends Widget {
    private NeuralNetwork neuralNetwork;

    private Button buttonStartLearning;
    private Button buttonStopLearning;

    public NeuralLearningWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createPrimaryWidget("Panel kontrolny");
        initialize();
    }

    private void initialize() {
        initializeStatisticsContainer();
        initializeControlsContainer();
        initializeButtonActions();
    }

    private void initializeStatisticsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("STATYSTYKI UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);
    }

    private void initializeControlsContainer() {
        VBox vBox = layoutService.getVBox(0d, 0d, 12d);
        vBox.getChildren().add(layoutService.getText("KONTROLA UCZENIA", LayoutService.TextStyle.HEADING));
        contentContainer.getChildren().add(vBox);

        HBox hBox = layoutService.getHBox(0d, 8d, 12d);
        hBox.getChildren().add(layoutService.getText("STEROWANIE", LayoutService.TextStyle.STATUS));

        buttonStartLearning = layoutService.getButton("Uruchom uczenie", null, null, null);
        buttonStopLearning = layoutService.getButton("Przewij uczenie", null, null, null);
        hBox.getChildren().addAll(buttonStartLearning, buttonStopLearning);

        vBox.getChildren().add(hBox);
    }

    private void initializeButtonActions() {
        buttonStartLearning.setOnAction(event -> neuralNetwork.startLearning());
    }
}
