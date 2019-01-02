package pl.wozniaktomek.layout.charts;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class Chart {
    ScatterChart<Number, Number> chart;
    HashMap<Integer, ArrayList<Point2D>> objects;
    NumberAxis xAxis;
    NumberAxis yAxis;

    void initializeChart(Integer prefWidth, Integer prefHeight) {
        xAxis = new NumberAxis(-10, 10, 1);
        yAxis = new NumberAxis(-10, 10, 1);
        chart = new ScatterChart<>(xAxis, yAxis);
        chart.setLegendSide(Side.BOTTOM);
        chart.setPrefSize(prefWidth, prefHeight);
        chart.setAnimated(false);
    }

    public void clearChart() {
        objects = new HashMap<>();
        refreshChart();
    }

    void refreshChart() {
        chart.getData().clear();

        for (Map.Entry<Integer, ArrayList<Point2D>> entry : objects.entrySet()) {
            ScatterChart.Series<Number, Number> series = new ScatterChart.Series<>();
            series.setName("klasa " + entry.getKey());
            for (Point2D point : entry.getValue()) {
                series.getData().add(new ScatterChart.Data<>(point.getX(), point.getY()));
            }

            chart.getData().add(series);
        }
    }

    void addPointToChart(Point2D pointClass, Integer classNumber) {
        if (objects.containsKey(classNumber)) {
            ArrayList<Point2D> classPoints = objects.get(classNumber);
            if (!classPoints.contains(pointClass)) {
                classPoints.add(pointClass);
            }

            objects.replace(classNumber, classPoints);
        } else {
            ArrayList<Point2D> classPoints = new ArrayList<>();
            classPoints.add(pointClass);
            objects.put(classNumber, classPoints);
        }
    }

    public ScatterChart<Number, Number> getChart() {
        return chart;
    }

    public void setChartSize(Double width, Double height) {
        chart.setPrefSize(width, height);
    }

    public void setObjects(HashMap<Integer, ArrayList<Point2D>> objects) {
        this.objects = objects;
    }

    public HashMap<Integer, ArrayList<Point2D>> getObjects() {
        return objects;
    }

    abstract void initializeEvent();

    abstract void initializeData();

    abstract public void refreshWidget();
}
