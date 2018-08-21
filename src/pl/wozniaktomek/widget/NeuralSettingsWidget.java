package pl.wozniaktomek.widget;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.neural.NeuralNetwork;

public class NeuralSettingsWidget extends Widget {
    private HBox mainPane;
    private NeuralNetwork neuralNetwork;

    public NeuralSettingsWidget(NeuralNetwork neuralNetwork, String widgetTitle) {
        this.neuralNetwork = neuralNetwork;
        setTitle(widgetTitle);
        initialize();
    }

    private void initialize() {
        initializeMainPane();
        initializeTopologyPane();
        initializeParametersPane();
    }

    private void initializeMainPane() {
        mainPane = new HBox();
        mainPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        mainPane.setSpacing(12);
        contentContainer.getChildren().add(mainPane);
    }

    private void initializeTopologyPane() {
        VBox vBox = getPane();
        Text text = getTitle("Topologia", vBox);
    }

    private void initializeParametersPane() {
        VBox vBox = getPane();
        Text text = getTitle("Parametry", vBox);
    }

    private VBox getPane() {
        VBox vBox = new VBox();
        vBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(12));
        vBox.setSpacing(12);
        vBox.getStyleClass().add("widget-pane-primary");
        mainPane.getChildren().add(vBox);
        return vBox;
    }

    private Text getTitle(String textTitle, VBox vBox) {
        Text text = new Text(textTitle);
        text.getStyleClass().add("section-title");
        vBox.getChildren().add(text);
        return text;
    }
}
