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
import pl.wozniaktomek.layout.widget.Widget;
import pl.wozniaktomek.layout.widget.neural.NeuralNetworkTopologyWidget;
import pl.wozniaktomek.layout.widget.neural.NeuralNetworkSettingsWidget;
import pl.wozniaktomek.layout.widget.neural.ReadDataWidget;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class NeuralControl implements Initializable {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox contentContainer;
    @FXML private HBox titleContainer;

    private NeuralNetwork neuralNetwork;

    private ReadDataWidget readDataWidget;
    private NeuralNetworkSettingsWidget neuralNetworkSettingsWidget;
    private NeuralNetworkTopologyWidget neuralNetworkTopologyWidget;

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
        readDataWidget = new ReadDataWidget(neuralNetwork, "Dane wejściowe");
        contentContainer.getChildren().add(readDataWidget.getWidget());
    }

    private void initializeNetworkSettingsWidget() {
        neuralNetworkSettingsWidget = new NeuralNetworkSettingsWidget(neuralNetwork, "Parametry sieci neuronowej");
        contentContainer.getChildren().add(neuralNetworkSettingsWidget.getWidget());
    }

    private void initializeNetworkTopologyWidget() {
        neuralNetworkTopologyWidget = new NeuralNetworkTopologyWidget(neuralNetwork, "Topologia sieci neuronowej");
        contentContainer.getChildren().add(neuralNetworkTopologyWidget.getWidget());
    }

    private void initializeSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            titleContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth() - 82);
            contentContainer.setPrefWidth(ThesisApp.windowControl.getContentPane().getWidth());

            if (isScrollBarVisible()) {
                neuralNetworkTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 36);
            } else {
                neuralNetworkTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 18);
            }
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
}
