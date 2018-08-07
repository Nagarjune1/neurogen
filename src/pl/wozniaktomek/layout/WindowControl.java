package pl.wozniaktomek.layout;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import pl.wozniaktomek.layout.widgets.DataEditorWidget;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowControl implements Initializable {
    @FXML private ScrollPane paneHome;
    @FXML private ScrollPane paneDataEditor;
    @FXML private Pane dataEditorContainer;

    @FXML private Button buttonNavHome;
    @FXML private Button buttonNavDataEditor;
    @FXML private Button buttonNavExit;

    private enum ActivePanel {HOME, DATA_EDITOR}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initializeDataEditorWidget();
        initializeButtonActions();
    }

    /*
    private void initializeDataEditorWidget() {
        DataEditorWidget dataEditorWidget = new DataEditorWidget();
        dataEditorContainer.getChildren().add(dataEditorWidget.getChart());
    }
    */

    private void initializeButtonActions() {
        buttonNavHome.setOnAction(event -> switchPanels(ActivePanel.HOME));
        buttonNavDataEditor.setOnAction(event -> switchPanels(ActivePanel.DATA_EDITOR));
        buttonNavExit.setOnAction(event -> closeProgram());
    }

    private void switchPanels(ActivePanel activePanel) {
        switch (activePanel) {
            case HOME:
                paneHome.setVisible(true);
                paneDataEditor.setVisible(false);
                break;

            case DATA_EDITOR:
                paneHome.setVisible(false);
                paneDataEditor.setVisible(true);
                break;
        }
    }

    private void closeProgram() {
        Platform.exit();
        System.exit(0);
    }
}
