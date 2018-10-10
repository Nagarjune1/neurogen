package pl.wozniaktomek.layout.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeControl implements Initializable {
    @FXML private VBox contentContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAboutPane();
    }

    private void initializeAboutPane() {

    }
}
