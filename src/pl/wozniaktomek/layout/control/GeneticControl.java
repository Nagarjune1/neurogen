package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.widget.ProgressWidget;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneticControl implements Initializable {
    @FXML private HBox titleContainer;
    @FXML private HBox progressContainer;

    private Integer progressStep = 0;
    private ProgressWidget progressWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSizeListener();
        initializeProgressWidget();
        initializeButtonActions();
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void initializeProgressWidget() {
        // progressWidget = new ProgressWidget(progressContainer, 4);
    }

    private void initializeButtonActions() {

    }
}
