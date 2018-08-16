package pl.wozniaktomek.layout.widget;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.wozniaktomek.neural.Layer;
import pl.wozniaktomek.neural.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NeuralNetworkWidget {
    private final static double DEFAULT_NEURON_RADIUS = 52d;
    private final static double DEFAULT_NEURON_SIZE = 60d;
    private Double neuronRadius, neuronSize, neuronMargin;

    private NeuralNetwork neuralNetwork;
    private Integer mostNeuronsInLayer, layersAmount;

    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private Double canvasWidth;

    private HashMap<Integer, ArrayList<Point2D>> points;

    public NeuralNetworkWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        analyzeNetwork();
    }

    public Canvas getWidget() {
        return canvas;
    }

    public void drawNetwork(Double width) {
        createCanvas(width);
        createGraphicContext();
        calculatePoints();
        drawNeurons();
        drawConnections();
    }

    private void analyzeNetwork() {
        mostNeuronsInLayer = 0;
        for (Layer layer : neuralNetwork.getLayers())
            if (layer.getNeurons().size() > mostNeuronsInLayer)
                mostNeuronsInLayer = layer.getNeurons().size();

        layersAmount = neuralNetwork.getLayers().size();
    }

    private void createCanvas(Double width) {
        canvasWidth = width;
        calculateNeuronSize();
        double canvasHeight = (neuronSize * (layersAmount * 2) - (neuronSize));
        canvas = new Canvas(canvasWidth, canvasHeight);
    }

    private void createGraphicContext() {
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.rgb(113, 140, 158, 1.0));
        graphicsContext.setStroke(Color.rgb(113, 140, 158, 1.0));
        graphicsContext.setLineWidth(2);
    }

    private void calculateNeuronSize() {
        double newNeuronSize = (canvasWidth / mostNeuronsInLayer);
        if (newNeuronSize < (DEFAULT_NEURON_SIZE)) {
            neuronSize = newNeuronSize;
            neuronRadius = newNeuronSize - 8d;
        } else {
            neuronRadius = DEFAULT_NEURON_RADIUS;
            neuronSize = DEFAULT_NEURON_SIZE;
        }

        if (newNeuronSize - DEFAULT_NEURON_SIZE < 128) {
            neuronMargin = newNeuronSize;
        } else {
            neuronMargin = DEFAULT_NEURON_SIZE + 128d;
        }
    }

    private void calculatePoints() {
        points = new HashMap<>();

        double height = neuronSize / 2d;
        for (int i = 1; i < neuralNetwork.getLayers().size() + 1; i++) {
            ArrayList<Point2D> listOfPoints = new ArrayList<>();

            Layer layer = neuralNetwork.getLayers().get(i - 1);
            int neuronsAmount = layer.getNeurons().size();
            double halfNeuronsAmount = Math.floor(neuronsAmount / 2d);

            double width;
            if ((neuronsAmount & 1) == 1) {
                width = canvasWidth / 2d - (neuronMargin * halfNeuronsAmount);
            } else {
                width = ((canvasWidth / 2d) + (neuronMargin / 2d)) - (neuronMargin * halfNeuronsAmount);
            }

            int counter = 0;
            while (counter < neuronsAmount) {
                listOfPoints.add(new Point2D(width, height));
                width += neuronMargin;
                counter++;
            }

            height += neuronSize * 2;
            points.put(i, listOfPoints);
        }
    }

    private void drawNeurons() {
        for (Map.Entry<Integer, ArrayList<Point2D>> entry : points.entrySet()) {
            for (Point2D point : entry.getValue()) {
                graphicsContext.strokeOval(point.getX() - neuronRadius / 2d, point.getY() - neuronRadius / 2, neuronRadius, neuronRadius);
            }
        }

        /* Bias
        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i).get(points.get(i).size() - 1);
            graphicsContext.fillOval(point.getX() - neuronRadius / 2d, point.getY() - neuronRadius / 2, neuronRadius, neuronRadius);
        }
        */
    }

    private void drawConnections() {
        graphicsContext.setLineWidth(0.5);

        for (int i = 1; i < points.size() - 1; i++) {
            ArrayList<Point2D> layerPoints = points.get(i);
            ArrayList<Point2D> nextLayerPoints = points.get(i + 1);

            for (Point2D point : layerPoints) {
                for (int j = 0; j < nextLayerPoints.size() - 1; j++) {
                    graphicsContext.strokeLine(point.getX(), point.getY() + neuronRadius / 2, nextLayerPoints.get(j).getX(), nextLayerPoints.get(j).getY() - neuronRadius / 2);
                }
            }
        }

        ArrayList<Point2D> layerPoints = points.get(points.size() - 1);
        ArrayList<Point2D> nextLayerPoints = points.get(points.size());

        for (Point2D point : layerPoints) {
            for (int j = 0; j < nextLayerPoints.size(); j++) {
                graphicsContext.strokeLine(point.getX(), point.getY() + neuronRadius / 2, nextLayerPoints.get(j).getX(), nextLayerPoints.get(j).getY() - neuronRadius / 2);
            }
        }
    }
}
