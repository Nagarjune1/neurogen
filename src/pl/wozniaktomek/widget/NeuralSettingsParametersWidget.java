package pl.wozniaktomek.widget;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

class NeuralSettingsParametersWidget extends Widget {
    private NeuralNetwork neuralNetwork;
    private NeuralSettingsWidget neuralSettingsWidget;

    NeuralSettingsParametersWidget(NeuralNetwork neuralNetwork, NeuralSettingsWidget neuralSettingsWidget, String title) {
        this.neuralNetwork = neuralNetwork;
        this.neuralSettingsWidget = neuralSettingsWidget;
        setTitle(title);
    }

    void refreshWidget() {
        contentContainer.getChildren().clear();

        if (neuralNetwork.getNeuralStructure().getLayers().size() > 0) {
            refreshBiasSetting();
        } else {
            contentContainer.getChildren().add(layoutService.getText("wczytaj dane uczące oraz dane testowe", LayoutService.TextStyle.STATUS));
        }
    }

    private void refreshBiasSetting() {
        HBox hBox = layoutService.getHBox(2d, 12d, 12d);
        hBox.getChildren().addAll(layoutService.getText("Bias", LayoutService.TextStyle.STATUS), getBiasCheckbox());
        contentContainer.getChildren().add(hBox);
    }

    private CheckBox getBiasCheckbox() {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(neuralNetwork.getNeuralStructure().isBias());

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                neuralNetwork.addBias();
            } else {
                neuralNetwork.deleteBias();
            }

            neuralSettingsWidget.refreshWidget();
        });

        return checkBox;
    }
}
