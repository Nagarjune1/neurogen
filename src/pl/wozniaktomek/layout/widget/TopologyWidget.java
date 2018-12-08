package pl.wozniaktomek.layout.widget;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pl.wozniaktomek.neural.structure.Connection;
import pl.wozniaktomek.neural.structure.Layer;
import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologyWidget extends Widget {
    private final static double DEFAULT_NEURON_RADIUS = 58d;
    private final static double DEFAULT_NEURON_SIZE = 78d;
    private Double neuronSize, neuronMargin;
    Double neuronRadius;

    NeuralNetwork neuralNetwork;
    private Integer mostNeuronsInLayer, layersAmount;

    GraphicsContext graphicsContext;
    private Canvas canvas;
    private Double canvasWidth;

    HashMap<Integer, ArrayList<Point2D>> points;

    public TopologyWidget(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        createWidget("Wizualizacja topologii sieci");
    }

    public void drawNetwork(Double width) {
        analyzeNetwork();
        createCanvas(width);
        createGraphicContext();

        if (neuralNetwork.getStructure().getLayers().size() > 1) {
            calculatePoints();
            drawNeurons();
            drawConnections();
        } else {
            contentContainer.getChildren().add(layoutService.getText("Wymagane jest wczytanie danych uczących!", LayoutService.TextStyle.HEADING));
        }
    }

    private void analyzeNetwork() {
        mostNeuronsInLayer = 0;
        for (Layer layer : neuralNetwork.getStructure().getLayers())
            if (layer.getNeurons().size() > mostNeuronsInLayer)
                mostNeuronsInLayer = layer.getNeurons().size();

        layersAmount = neuralNetwork.getStructure().getLayers().size();
    }

    private void createCanvas(Double width) {
        contentContainer.getChildren().clear();
        canvasWidth = width - 48;
        calculateNeuronSize();
        double canvasHeight = (neuronSize * (layersAmount * 2) - (neuronSize));
        canvas = new Canvas(canvasWidth, canvasHeight);
        contentContainer.getChildren().add(canvas);
    }

    private void createGraphicContext() {
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.rgb(31, 64, 90, 0.75));
        graphicsContext.setLineWidth(2);

        BoxBlur blur = new BoxBlur();
        blur.setWidth(1);
        blur.setHeight(1);
        blur.setIterations(1);
        graphicsContext.setEffect(blur);
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

        double height = DEFAULT_NEURON_SIZE / 2d;
        for (int i = 1; i < neuralNetwork.getStructure().getLayers().size() + 1; i++) {
            ArrayList<Point2D> listOfPoints = new ArrayList<>();

            Layer layer = neuralNetwork.getStructure().getLayers().get(i - 1);
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

            height += DEFAULT_NEURON_SIZE * 2;
            points.put(i, listOfPoints);
        }
    }

    private void drawNeurons() {
        drawInputNeurons();
        drawHiddenLayers();

        if (neuralNetwork.getStructure().isBias())
            drawBias();
    }

    private void drawInputNeurons() {
        graphicsContext.setFill(Color.rgb(31, 64, 90, 1.0));

        for (Point2D point : points.get(1)) {
            graphicsContext.fillOval(point.getX() - neuronRadius / 2d, point.getY() - neuronRadius / 2, neuronRadius, neuronRadius);
        }
    }

    private void drawHiddenLayers() {
        for (int i = 2; i < points.size() + 1; i++) {
            for (Point2D point : points.get(i)) {
                graphicsContext.strokeOval(point.getX() - neuronRadius / 2d, point.getY() - neuronRadius / 2, neuronRadius, neuronRadius);
            }
        }
    }

    private void drawBias() {
        graphicsContext.setFill(Color.rgb(0, 195, 237, 1.0));

        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i).get(points.get(i).size() - 1);
            graphicsContext.fillOval(point.getX() - neuronRadius / 2d, point.getY() - neuronRadius / 2, neuronRadius, neuronRadius);
        }
    }

    protected void drawConnections() {
        graphicsContext.setLineWidth(0.5);

        ArrayList<Point2D> allPoints = new ArrayList<>();
        for (int i = 1; i < points.size() + 1; i++) {
            ArrayList<Point2D> layerPoints = points.get(i);
            allPoints.addAll(layerPoints);
        }

        List<Connection> connections = neuralNetwork.getStructure().getConnections();
        for (Connection connection : connections) {
            Point2D startPoint = allPoints.get(connection.getNeuronInput().getNumber() - 1);
            Point2D endPoint = allPoints.get(connection.getNeuronOutput().getNumber() - 1);
            graphicsContext.strokeLine(startPoint.getX(), startPoint.getY() + neuronRadius / 2, endPoint.getX(), endPoint.getY() - neuronRadius / 2);
        }
    }

    /* just for debug */
    private void drawNeuronNumbers() {
        graphicsContext.setFont(new Font("Arial", 16));
        graphicsContext.setFill(Color.rgb(0, 0, 0, 1.0));

        int counter = 0;
        for (Map.Entry<Integer, ArrayList<Point2D>> entry : points.entrySet()) {
            for (Point2D point : entry.getValue()) {
                graphicsContext.fillText(String.valueOf(++counter), point.getX() - neuronRadius / 8d, point.getY() - neuronRadius / 8d);
            }
        }
    }

}
