package pl.wozniaktomek.layout.charts;

import javafx.geometry.Point2D;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class EditorChart extends Chart {
    private Text textSummary;

    private Integer classNumber;
    private Integer objectAmount;

    public EditorChart() {
        initializeData();
        initializeChart(854, 480);
        initializeEvent();
    }

    @Override
    public void refreshWidget() {
        refreshChart();
        refreshSummary();
    }

    @Override
    void initializeData() {
        classNumber = 1;
        objectAmount = 0;
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
                addPointToChart(pointClass, classNumber);
            }

            refreshChart();
            refreshSummary();
        });
    }

    private void refreshSummary() {
        objectAmount = objects.values().stream().mapToInt(ArrayList::size).sum();

        String summary = "";

        for (int i = 1; i <= 8; i++) {
            if (objects.get(i) != null)
                summary = summary.concat("\nklasa " + i + ":  " + objects.get(i).size() + " / " + objectAmount);
        }

        if (objectAmount.equals(0))
            summary = summary.concat("\nbrak obiektów...");

        textSummary.setText(summary);
    }

    @Override
    public void clearChart() {
        super.clearChart();
        objectAmount = 0;
        refreshSummary();
    }


    public void setTextSummary(Text textSummary) {
        this.textSummary = textSummary;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }
}
