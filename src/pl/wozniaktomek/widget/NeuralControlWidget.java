package pl.wozniaktomek.widget;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NeuralControlWidget extends Widget {
    public NeuralControlWidget(String widgetTitle) {
        setTitle(widgetTitle);
        initialize();
    }

    private void initialize() {
        initializeStatusPanels();
    }

    private void initializeStatusPanels() {
        HBox hBox = layoutService.getHBox(6d, 6d, 12d);

        VBox vBox = layoutService.getInfoPane("STATUS DANYCH WEJŚCIOWYCH");
        hBox.getChildren().add(vBox);

        vBox = layoutService.getInfoPane("STATUS SIECI");
        hBox.getChildren().add(vBox);

        vBox = layoutService.getInfoPane("STATUS UCZENIA");
        hBox.getChildren().add(vBox);

        contentContainer.getChildren().add(hBox);
    }
}
