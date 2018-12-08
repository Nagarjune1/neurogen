package pl.wozniaktomek.layout.widget;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.structure.Connection;

import java.util.ArrayList;
import java.util.List;

public class LearningVisualizationWidget extends TopologyWidget {
    LearningVisualizationWidget(NeuralNetwork neuralNetwork) {
        super(neuralNetwork);
        setMinimizationVisibility(false);
        setTitle("Wizualizacja uczenia");
    }

    @Override
    public void drawNetwork(Double width) {
        super.drawNetwork(width);
        neuralNetwork.getLearning().setIsNowVisualizationUpdating(false);
    }

    /* Different way to draw connections */
    @Override
    protected void drawConnections() {
        graphicsContext.setLineWidth(2d);

        ArrayList<Point2D> allPoints = new ArrayList<>();
        for (int i = 1; i < points.size() + 1; i++) {
            ArrayList<Point2D> layerPoints = points.get(i);
            allPoints.addAll(layerPoints);
        }

        List<Connection> connections = neuralNetwork.getStructure().getConnections();
        for (Connection connection : connections) {
            Point2D startPoint = allPoints.get(connection.getNeuronInput().getNumber() - 1);
            Point2D endPoint = allPoints.get(connection.getNeuronOutput().getNumber() - 1);

            double startPointX = startPoint.getX();
            double startPointY = startPoint.getY() + neuronRadius / 2d;
            double endPointX = endPoint.getX();
            double endPointY = endPoint.getY() - neuronRadius / 2d;

            double midPointX = (startPointX + endPointX) / 2d;
            double midPointY = (startPointY + endPointY) / 2d;

            double angle = Math.tanh(Math.abs(startPointX - endPointX) / Math.abs(startPointY - endPointY));
            double magnification = connection.getWeight();

            double[] xPoints = {
                    startPointX,
                    midPointX + Math.sin(angle + 90) * magnification,
                    endPointX,
                    midPointX + Math.sin(angle - 90) * magnification
            };

            double[] yPoints = {
                    startPointY,
                    midPointY + Math.cos(angle + 90) * magnification,
                    endPointY,
                    midPointY + Math.cos(angle - 90) * magnification,
            };

            if (connection.getWeight() >= 0) {
                graphicsContext.setFill(Color.rgb(31, 64, 90, 0.75));
            } else {
                graphicsContext.setFill(Color.rgb(0, 195, 237, 0.5));
            }

            graphicsContext.fillPolygon(xPoints, yPoints, 4);

        }
    }

}
