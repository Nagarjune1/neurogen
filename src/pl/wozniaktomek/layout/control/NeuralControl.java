package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.layout.widget.NeuralNetworkWidget;
import pl.wozniaktomek.layout.widget.neural.ReadDataWidget;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.data.DataService;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

public class NeuralControl implements Initializable {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox contentContainer;
    @FXML private HBox titleContainer;
    @FXML private HBox progressWidgetContainer;
    @FXML private VBox networkWidgetContainer;
    private NeuralNetworkWidget neuralNetworkWidget;

    private NeuralNetwork neuralNetwork;
    private HashMap<Integer, ArrayList<Point2D>> objectsLearning;
    private HashMap<Integer, ArrayList<Point2D>> objectsTesting;

    private enum ObjectsType {LEARNING, TESTING}

    private ReadDataWidget readDataWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNeuralNetwork();
        // initializeNetworkWidget();
        initializeReadDataWidget();
        initializeSizeListener();
        initializeButtonActions();
    }

    private void initializeNeuralNetwork() {
        neuralNetwork = new NeuralNetwork();
    }

    /*
    private void initializeNetworkWidget() {
        neuralNetwork = new NeuralNetwork();
        neuralNetworkWidget = new NeuralNetworkWidget(neuralNetwork);
    }
    */

    private void initializeReadDataWidget() {
        readDataWidget = new ReadDataWidget(neuralNetwork, "Dane wejściowe");
        contentContainer.getChildren().add(readDataWidget.getWidget());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            progressWidgetContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());
            // networkWidgetContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());

            /*
            if (isScrollBarVisible()) {
                refreshNetworkWidget(ThesisApp.windowControl.getContentPane().getWidth() - 18);
            } else {
                refreshNetworkWidget(ThesisApp.windowControl.getContentPane().getWidth());
            }
            */
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void initializeButtonActions() {
    }

    private void refreshNetworkWidget(Double width) {
        networkWidgetContainer.getChildren().clear();
        neuralNetworkWidget.drawNetwork(width - 42);
        networkWidgetContainer.getChildren().add(neuralNetworkWidget.getWidget());
    }

    /*
    private void setStatus(ObjectsType objectsType) {
        if (objectsType.equals(ObjectsType.LEARNING)) {
            status(objectsLearning, textLoadDataLearningStatus);
        } else {
            status(objectsTesting, textLoadDataTestingStatus);
        }
    }

    private void status(HashMap<Integer, ArrayList<Point2D>> objects, Text textStatus) {
        if (objects != null) {
            textStatus.setText("DANE POPRAWNE");
            textStatus.getStyleClass().add("action-status-success");
            textStatus.getStyleClass().remove("action-status-failure");
        } else {
            textStatus.setText("DANE NIEPOPRAWNE");
            textStatus.getStyleClass().add("action-status-failure");
            textStatus.getStyleClass().remove("action-status-success");
        }
    }
    */

    private boolean isScrollBarVisible() {
        Set<Node> nodes = scrollPane.lookupAll(".scroll-bar");

        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation().equals(Orientation.VERTICAL) && scrollBar.isVisible()) {
                    return true;
                }
            }
        }

        return false;
    }
}
