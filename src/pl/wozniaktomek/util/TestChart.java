package pl.wozniaktomek.util;

import javafx.geometry.Point2D;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.service.StartupService;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestChart extends Chart {
    private NeuralNetwork neuralNetwork;

    public TestChart(NeuralNetwork neuralNetwork, Integer prefWidth, Integer prefHeight) {
        this.neuralNetwork = neuralNetwork;
        initializeData();
        initializeChart(prefWidth, prefHeight);
        initializeEvent();
    }

    @Override
    public void refreshWidget() {
        refreshChart();
    }

    @Override
    void initializeData() {
        objects = new HashMap<>();
    }

    @Override
    void initializeEvent() {
        chart.setOnMouseClicked(event -> {
            Point2D pointScene = new Point2D(event.getSceneX(), event.getSceneY());

            Point2D pointClass = new Point2D(
                    Math.round(xAxis.getValueForDisplay(xAxis.sceneToLocal(pointScene).getX()).doubleValue() * 1000.0) / 1000.0,
                    Math.round(yAxis.getValueForDisplay(yAxis.sceneToLocal(pointScene).getY()).doubleValue() * 1000.0) / 1000.0
            );

            if (pointClass.getX() >= xAxis.getLowerBound() && pointClass.getX() <= xAxis.getUpperBound() && pointClass.getY() >= yAxis.getLowerBound() && pointClass.getY() <= yAxis.getUpperBound()) {
                Integer classNumber = countClassNumber(pointClass.getX(), pointClass.getY());
                addPointToChart(pointClass, classNumber);
            }

            refreshChart();
        });
    }

    private Integer countClassNumber(Double posX, Double posY) {
        NeuralObject neuralObject = getNeuralObject(posX, posY);
        return new StartupService(neuralNetwork).getObjectClass(neuralObject);
    }

    private NeuralObject getNeuralObject(Double posX, Double posY) {
        ArrayList<Double> inputValues = new ArrayList<>();
        inputValues.add(posX);
        inputValues.add(posY);

        return new NeuralObject(inputValues, null);
    }
}
