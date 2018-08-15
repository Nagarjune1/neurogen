package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.layout.widget.NeuralNetworkWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class NeuralControl implements Initializable {
    @FXML private ScrollPane scrollPane;
    @FXML private HBox titleContainer;
    @FXML private VBox widgetContainer;

    private NeuralNetwork neuralNetwork;
    private NeuralNetworkWidget neuralNetworkWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeWidget();
        initializeSizeListener();
    }

    private void initializeWidget() {
        neuralNetwork = new NeuralNetwork();
        neuralNetwork.addLayer(3);
        neuralNetwork.addLayer(8);
        neuralNetwork.addLayer(5);
        neuralNetwork.addLayer(3);
        neuralNetwork.createConnections();
        neuralNetworkWidget = new NeuralNetworkWidget(neuralNetwork);
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            widgetContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());

            if (isScrollBarVisible())
                refreshWidget(ThesisApp.windowControl.getContentPane().getWidth() - 18);
            else refreshWidget(ThesisApp.windowControl.getContentPane().getWidth());
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private boolean isScrollBarVisible() {
        Set<Node> nodes = scrollPane.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation().equals(Orientation.VERTICAL))
                    return true;
            }
        }

        return false;
    }

    private void refreshWidget(Double width) {
        widgetContainer.getChildren().clear();
        neuralNetworkWidget.drawNetwork(width - 42);
        widgetContainer.getChildren().add(neuralNetworkWidget.getWidget());
    }
}
