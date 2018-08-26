package pl.wozniaktomek.widget;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import pl.wozniaktomek.layout.control.NeuralControl;
import pl.wozniaktomek.neural.NeuralNetwork;
public class NeuralSettingsWidget extends Widget {
    private HBox mainPane;

    private NeuralSettingsTopologyWidget settingsTopologyWidget;
    private NeuralSettingsParametersWidget settingsParametersWidget;

    private NeuralNetwork neuralNetwork;
    private NeuralControl neuralControl;

    public NeuralSettingsWidget(NeuralNetwork neuralNetwork, String widgetTitle, NeuralControl neuralControl) {
        this.neuralNetwork = neuralNetwork;
        this.neuralControl = neuralControl;
        setTitle(widgetTitle);
        initialize();
        settingsTopologyWidget.refreshWidget();
        settingsParametersWidget.refreshWidget();
    }

    public void refreshWidget() {
        settingsTopologyWidget.refreshWidget();
        settingsParametersWidget.refreshWidget();
        neuralControl.refreshTopologyWidget();
    }

    private void initialize() {
        initializeMainPane();
        initializeTopologyPane();
        initializeParametersPane();
    }

    private void initializeMainPane() {
        mainPane = new HBox();
        mainPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        mainPane.setSpacing(12);
        contentContainer.getChildren().add(mainPane);
    }

    private void initializeTopologyPane() {
        settingsTopologyWidget = new NeuralSettingsTopologyWidget(neuralNetwork, this, "Topologia sieci");
        settingsTopologyWidget.setStyle(WidgetStyle.SECONDARY);
        settingsTopologyWidget.contentContainer.setPrefWidth(960d);
        mainPane.getChildren().add(settingsTopologyWidget.getWidget());
    }

    private void initializeParametersPane() {
        settingsParametersWidget = new NeuralSettingsParametersWidget(neuralNetwork, this, "Parametry sieci");
        settingsParametersWidget.setStyle(WidgetStyle.SECONDARY);
        settingsParametersWidget.contentContainer.setPrefWidth(960d);
        mainPane.getChildren().add(settingsParametersWidget.getWidget());
    }
}
