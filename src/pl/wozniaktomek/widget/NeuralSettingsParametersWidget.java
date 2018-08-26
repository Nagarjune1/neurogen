package pl.wozniaktomek.widget;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import pl.wozniaktomek.neural.NeuralNetwork;

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
            contentContainer.getChildren().add(getActionBoldText("wczytaj dane uczące oraz dane testowe"));
        }
    }

    private void refreshBiasSetting() {
        HBox hBox = getHBoxContainer();
        hBox.getChildren().add(getActionBoldText("Bias"));
        hBox.getChildren().add(getBiasCheckbox());

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
