package pl.wozniaktomek.layout.widgets;

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
    private HashMap<Integer, ArrayList<Point2D>> points;
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
        points = new HashMap<>();
    }

    private void initializeChart() {
        xAxis = new NumberAxis(-10, 10, 1);
        yAxis = new NumberAxis(-10, 10, 1);
        chart = new ScatterChart<>(xAxis, yAxis);
        chart.setLegendSide(Side.RIGHT);
        chart.setPrefSize(854, 480);
    }

    private void initializeEvent() {
        chart.setOnMouseClicked(event -> {
            Point2D pointScene = new Point2D(event.getSceneX(), event.getSceneY());

            Point2D pointClass = new Point2D(
                    // TODO precision
                    xAxis.getValueForDisplay(xAxis.sceneToLocal(pointScene).getX()).doubleValue(),
                    yAxis.getValueForDisplay(yAxis.sceneToLocal(pointScene).getY()).doubleValue()
            );

            if (points.containsKey(classNumber)) {
                ArrayList<Point2D> classPoints = points.get(classNumber);
                if (!classPoints.contains(pointClass))
                    classPoints.add(pointClass);
                points.replace(classNumber, classPoints);
            } else {
                ArrayList<Point2D> classPoints = new ArrayList<>();
                classPoints.add(pointClass);
                points.put(classNumber, classPoints);
            }

            objectAmount++;
            refreshChart();
            refreshSummary();
        });
    }

    private void refreshChart() {
        chart.getData().clear();

        for (Map.Entry<Integer, ArrayList<Point2D>> entry : points.entrySet()) {
            ScatterChart.Series<Number, Number> series = new ScatterChart.Series<>();
            series.setName("klasa " + entry.getKey());
            for (Point2D point : entry.getValue()) {
                series.getData().add(new ScatterChart.Data<>(point.getX(), point.getY()));
            }

            chart.getData().add(series);
        }
    }

    private void refreshSummary() {
        String summary = "Podsumowanie";

        for (int i = 1; i < 6; i++) {
            if (points.get(i) != null)
                summary = summary.concat("\n     klasa " + i + ":  " + points.get(i).size() + " / " + objectAmount);
        }

        if (objectAmount.equals(0))
            summary = summary.concat("\n     brak obiektów...");

        textSummary.setText(summary);
    }

    public void clearChart() {
        points = new HashMap<>();
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
}
