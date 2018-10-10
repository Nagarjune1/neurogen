package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import pl.wozniaktomek.ThesisApp;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneticControl implements Initializable {
    @FXML private HBox titleContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSizeListener();
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }
}
