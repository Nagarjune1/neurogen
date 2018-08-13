package pl.wozniaktomek.layout.widget;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataEditorWidget {
    private Text textSummary;

    private ScatterChart<Number, Number> chart;
    private HashMap<Integer, ArrayList<Point2D>> objects;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Integer classNumber;
    private Integer objectAmount;

    public DataEditorWidget() {
        initializeData();
        initializeChart();
        initializeEvent();
    }

    private void initializeData() {
        classNumber = 1;
        objectAmount = 0;
        objects = new HashMap<>();
    }

    private void initializeChart() {
        xAxis = new NumberAxis(-10, 10, 1);
        yAxis = new NumberAxis(-10, 10, 1);
        chart = new ScatterChart<>(xAxis, yAxis);
        chart.setLegendSide(Side.RIGHT);
        chart.setPrefSize(854, 480);
        chart.setAnimated(false);
    }

    private void initializeEvent() {
        chart.setOnMouseClicked(event -> {
            Point2D pointScene = new Point2D(event.getSceneX(), event.getSceneY());

            Point2D pointClass = new Point2D(
                    Math.round(xAxis.getValueForDisplay(xAxis.sceneToLocal(pointScene).getX()).doubleValue() * 1000.0) / 1000.0,
                    Math.round(yAxis.getValueForDisplay(yAxis.sceneToLocal(pointScene).getY()).doubleValue() * 1000.0) / 1000.0
            );

            if (objects.containsKey(classNumber)) {
                ArrayList<Point2D> classPoints = objects.get(classNumber);
                if (!classPoints.contains(pointClass))
                    classPoints.add(pointClass);
                objects.replace(classNumber, classPoints);
            } else {
                ArrayList<Point2D> classPoints = new ArrayList<>();
                classPoints.add(pointClass);
                objects.put(classNumber, classPoints);
            }

            refreshChart();
            refreshSummary();
        });
    }

    private void refreshChart() {
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

    private void refreshSummary() {
        objectAmount = objects.values().stream().mapToInt(ArrayList::size).sum();

        String summary = "Podsumowanie";

        for (int i = 1; i < 6; i++) {
            if (objects.get(i) != null)
                summary = summary.concat("\n     klasa " + i + ":  " + objects.get(i).size() + " / " + objectAmount);
        }

        if (objectAmount.equals(0))
            summary = summary.concat("\n     brak obiektów...");

        textSummary.setText(summary);
    }

    public void refresh() {
        refreshChart();
        refreshSummary();
    }

    public void clearChart() {
        objects = new HashMap<>();
        objectAmount = 0;
        refreshChart();
        refreshSummary();
    }

    public ScatterChart<Number, Number> getChart() {
        return chart;
    }

    public void setChartSize(Double width, Double height) {
        chart.setPrefSize(width, height);
    }

    public void setTextSummary(Text textSummary) {
        this.textSummary = textSummary;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public void setObjects(HashMap<Integer, ArrayList<Point2D>> objects) {
        this.objects = objects;
    }

    public HashMap<Integer, ArrayList<Point2D>> getObjects() {
        return objects;
    }
}
