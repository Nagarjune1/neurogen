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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowControl implements Initializable {
    @FXML private AnchorPane navPane;
    @FXML private AnchorPane contentPane;

    private AnchorPane homePane;
    private AnchorPane editorPane;
    private AnchorPane geneticPane;
    private AnchorPane neuralPane;

    @FXML private Button buttonNavHome;
    @FXML private Button buttonNavEditor;
    @FXML private Button buttonNavGenetic;
    @FXML private Button buttonNavNeural;
    @FXML private Button buttonNavExit;

    private ArrayList<AnchorPane> panels;
    private ArrayList<Button> buttons;

    private enum ActivePane {HOME, EDITOR, GENETIC, NEURAL}
    private ActivePane activePane = ActivePane.EDITOR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void initialization() {
        initializePanels();
        initializeCollections();
        initializeSizeListener();
        initializeButtonActions();
        switchPanels(ActivePane.HOME);
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
    }

    private void initializeCollections() {
        panels = new ArrayList<>();
        panels.add(homePane);
        panels.add(editorPane);
        panels.add(geneticPane);
        panels.add(neuralPane);

        buttons = new ArrayList<>();
        buttons.add(buttonNavHome);
        buttons.add(buttonNavEditor);
        buttons.add(buttonNavGenetic);
        buttons.add(buttonNavNeural);
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
        buttonNavEditor.setOnAction(event -> switchPanels(ActivePane.EDITOR));
        buttonNavGenetic.setOnAction(event -> switchPanels(ActivePane.GENETIC));
        buttonNavNeural.setOnAction(event -> switchPanels(ActivePane.NEURAL));
        buttonNavExit.setOnAction(event -> closeProgram());

    }
    private void switchPanels(ActivePane activePanel) {
        switch (activePanel) {
            case HOME:
                if (!activePane.equals(ActivePane.HOME)) {
                    setActivePane(homePane);
                    setActiveButton(buttonNavHome);
                    activePane = ActivePane.HOME;
                }

                break;

            case EDITOR:
                if (!activePane.equals(ActivePane.EDITOR)) {
                    setActivePane(editorPane);
                    setActiveButton(buttonNavEditor);
                    activePane = ActivePane.EDITOR;
                }

                break;

            case GENETIC:
                if (!activePane.equals(ActivePane.GENETIC)) {
                    setActivePane(geneticPane);
                    setActiveButton(buttonNavGenetic);
                    activePane = ActivePane.GENETIC;
                }

                break;

            case NEURAL:
                if (!activePane.equals(ActivePane.NEURAL)) {
                    setActivePane(neuralPane);
                    setActiveButton(buttonNavNeural);
                    activePane = ActivePane.NEURAL;
                }

                break;
        }
    }

    private void setActivePane(AnchorPane activePane) {
        for (AnchorPane anchorPane : panels) {
            if (anchorPane.equals(activePane)) {
                anchorPane.setVisible(true);
            } else {
                anchorPane.setVisible(false);
            }
        }
    }

    private void setActiveButton(Button activeButton) {
        for (Button button : buttons) {
            if (button.equals(activeButton)) {
                button.getStyleClass().add("nav-button-active");
            } else {
                button.getStyleClass().remove("nav-button-active");
            }
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
