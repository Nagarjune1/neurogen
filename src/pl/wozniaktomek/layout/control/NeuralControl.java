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
import pl.wozniaktomek.widget.NeuralTopologyWidget;
import pl.wozniaktomek.widget.NeuralSettingsWidget;
import pl.wozniaktomek.widget.NeuralDataWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class NeuralControl implements Initializable {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox contentContainer;
    @FXML private HBox titleContainer;

    private NeuralNetwork neuralNetwork;

    private NeuralDataWidget neuralDataWidget;
    private NeuralSettingsWidget neuralSettingsWidget;
    private NeuralTopologyWidget neuralTopologyWidget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNeuralNetwork();
        initializeReadDataWidget();
        initializeNetworkSettingsWidget();
        initializeNetworkTopologyWidget();
        initializeSizeListener();
        initializeButtonActions();
    }

    private void initializeNeuralNetwork() {
        neuralNetwork = new NeuralNetwork();
    }

    private void initializeReadDataWidget() {
        neuralDataWidget = new NeuralDataWidget(this, neuralNetwork, "Dane wejściowe");
        contentContainer.getChildren().add(neuralDataWidget.getWidget());
    }

    private void initializeNetworkSettingsWidget() {
        neuralSettingsWidget = new NeuralSettingsWidget(neuralNetwork, "Parametry sieci neuronowej");
        contentContainer.getChildren().add(neuralSettingsWidget.getWidget());
    }

    private void initializeNetworkTopologyWidget() {
        neuralTopologyWidget = new NeuralTopologyWidget(neuralNetwork, "Topologia sieci neuronowej");
        contentContainer.getChildren().add(neuralTopologyWidget.getWidget());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            contentContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());
            refreshTopologyWidget();
        };

        ThesisApp.windowControl.getContentPane().widthProperty().addListener(stageSizeListener);
        ThesisApp.windowControl.getContentPane().heightProperty().addListener(stageSizeListener);
    }

    private void initializeButtonActions() {
    }

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

    public void refreshSettingsWidget() {
        neuralSettingsWidget.refreshWidget();
    }

    public void refreshTopologyWidget() {
        if (isScrollBarVisible()) {
            neuralTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 36);
        } else {
            neuralTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 18);
        }
    }
}
