package pl.wozniaktomek.layout.control;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.widget.*;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.net.URL;
import java.util.ResourceBundle;

public class NeuralControl implements Initializable {
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
        neuralSettingsWidget = new NeuralSettingsWidget(neuralNetwork, "Ustawienia sieci neuronowej", this);
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

    public void refreshSettingsWidget() {
        neuralSettingsWidget.refreshWidget();
    }

    public void refreshTopologyWidget() {
        neuralTopologyWidget.drawNetwork(ThesisApp.windowControl.getContentPane().getWidth() - 36);
    }

    /*
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
    */
}
