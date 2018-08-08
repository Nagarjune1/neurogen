package pl.wozniaktomek.layout.control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pl.wozniaktomek.ThesisApp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowControl implements Initializable {
    @FXML private AnchorPane navPane;
    @FXML private AnchorPane contentPane;
    private AnchorPane homePane;
    private AnchorPane editorPane;
    private AnchorPane geneticPane;
    private AnchorPane neuralPane;

    @FXML private Button buttonNavHome;
    @FXML private Button buttonNavDataEditor;
    @FXML private Button buttonNavGenetic;
    @FXML private Button buttonNavNeural;
    @FXML private Button buttonNavExit;

    private enum ActivePane {HOME, EDITOR, GENETIC, NEURAL}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initialization() {
        initializePanels();
        initializeSizeListener();
        initializeButtonActions();
    }

    private void initializePanels() {
        try {
            homePane = FXMLLoader.load(getClass().getResource("../view/home.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            editorPane = FXMLLoader.load(getClass().getResource("../view/editor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            geneticPane = FXMLLoader.load(getClass().getResource("../view/genetic.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            neuralPane = FXMLLoader.load(getClass().getResource("../view/neural.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        contentPane.getChildren().addAll(homePane, editorPane, geneticPane, neuralPane);
        switchPanels(ActivePane.HOME);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            contentPane.setPrefSize(ThesisApp.stage.getWidth() - navPane.getWidth() - 16, ThesisApp.stage.getHeight() - 38);
            homePane.setPrefSize(contentPane.getPrefWidth(), contentPane.getPrefHeight());
            editorPane.setPrefSize(contentPane.getPrefWidth(), contentPane.getPrefHeight());
            geneticPane.setPrefSize(contentPane.getPrefWidth(), contentPane.getPrefHeight());
            neuralPane.setPrefSize(contentPane.getPrefWidth(), contentPane.getPrefHeight());
        };

        ThesisApp.stage.widthProperty().addListener(stageSizeListener);
        ThesisApp.stage.heightProperty().addListener(stageSizeListener);
    }

    private void initializeButtonActions() {
        buttonNavHome.setOnAction(event -> switchPanels(ActivePane.HOME));
        buttonNavDataEditor.setOnAction(event -> switchPanels(ActivePane.EDITOR));
        buttonNavGenetic.setOnAction(event -> switchPanels(ActivePane.GENETIC));
        buttonNavNeural.setOnAction(event -> switchPanels(ActivePane.NEURAL));
        buttonNavExit.setOnAction(event -> closeProgram());

    }
    private void switchPanels(ActivePane activePanel) {
        switch (activePanel) {
            case HOME:
                homePane.setVisible(true);
                editorPane.setVisible(false);
                geneticPane.setVisible(false);
                neuralPane.setVisible(false);
                break;

            case EDITOR:
                homePane.setVisible(false);
                editorPane.setVisible(true);
                geneticPane.setVisible(false);
                neuralPane.setVisible(false);
                break;

            case GENETIC:
                homePane.setVisible(false);
                editorPane.setVisible(false);
                geneticPane.setVisible(true);
                neuralPane.setVisible(false);
                break;

            case NEURAL:
                homePane.setVisible(false);
                editorPane.setVisible(false);
                geneticPane.setVisible(false);
                neuralPane.setVisible(true);
                break;
        }
    }

    AnchorPane getContentPane() {
        return contentPane;
    }

    private void closeProgram() {
        Platform.exit();
        System.exit(0);
    }
}
