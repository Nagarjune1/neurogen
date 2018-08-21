package pl.wozniaktomek.service;

import javafx.geometry.Point2D;
import javafx.stage.FileChooser;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.neural.NeuralObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DataTransferService {
    private final static String SEPARATOR = ";";
    private Integer classCounter;

    public void saveToFile(HashMap<Integer, ArrayList<Point2D>> objects) {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(ThesisApp.stage);
        if (file != null) {
            try {
                Files.write(Paths.get(file.getPath()), parseToString(organizeObjects(objects)).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<NeuralObject> readFromFile() {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(ThesisApp.stage);

        if (file != null) {
            return parseFromFile(file);
        } else {
            return null;
        }
    }

    /* Parsing to file */
    private HashMap<Integer, ArrayList<Point2D>> organizeObjects(HashMap<Integer, ArrayList<Point2D>> objects) {
        for (int i = 1; i < 6; i++) {
            if (!objects.containsKey(i)) {
                for (int j = i + 1; j < 6; j++) {
                    if (objects.containsKey(j)) {
                        objects.put(i, objects.get(j));
                        objects.remove(j);
                        break;
                    }
                }
            }
        }

        classCounter = 0;
        for (int i = 1; i < 6; i++) {
            if (objects.containsKey(i)) {
                classCounter++;
            }
        }

        return objects;
    }

    private String parseToString(HashMap<Integer, ArrayList<Point2D>> objects) {
        String content = "";
        content = content.concat(getFirstLine(objects));

        for (Map.Entry<Integer, ArrayList<Point2D>> mapEntry : objects.entrySet()) {
            ArrayList<Point2D> classObjects = mapEntry.getValue();
            for (Point2D point : classObjects) {
                content = content.concat(point.getX() + SEPARATOR + point.getY() + SEPARATOR + getStringClassNumber(mapEntry.getKey()) + "\n");
            }
        }

        return content;
    }

    private String getFirstLine(HashMap<Integer, ArrayList<Point2D>> objects) {
        return "NeuroGen" + SEPARATOR + "2" + SEPARATOR + objects.size() + "\n";
    }

    private String getStringClassNumber(Integer classNumber) {
        String content = "";

        for (int i = 1; i <= classCounter; i++) {
            if (classNumber.equals(i)) {
                content = content.concat("1" + SEPARATOR);
            } else {
                content = content.concat("0" + SEPARATOR);
            }
        }

        return content;
    }

    /* Parsing from file */
    private ArrayList<NeuralObject> parseFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getObjectsFromLines(lines);
    }

    private ArrayList<NeuralObject> getObjectsFromLines(List<String> lines) {
        ArrayList<NeuralObject> objects = new ArrayList<>();
        Integer inputAmount = getInputAmount(lines.get(0));

        for (int i = 1; i < lines.size(); i++) {
            String[] splitLine = lines.get(i).split(SEPARATOR);
            ArrayList<Double> inputValues = getInputValues(splitLine, inputAmount);
            Integer classNumber = getClassNumber(splitLine, inputAmount);

            if (inputValues != null && classNumber != null) {
                objects.add(new NeuralObject(inputValues, classNumber));
            } else {
                return null;
            }
        }

        if (objects.size() > 0) {
            return objects;
        } else {
            return null;
        }
    }

    private Integer getInputAmount(String line) {
        String[] splitLine = line.split(SEPARATOR);
        return Integer.valueOf(splitLine[1]);
    }

    private ArrayList<Double> getInputValues(String splitLine[], Integer inputAmount) {
        ArrayList<Double> inputValues = new ArrayList<>();

        for (int j = 0; j < inputAmount; j++) {
            inputValues.add(Double.valueOf(splitLine[j]));
        }

        if (inputValues.size() > 0) {
            return inputValues;
        } else {
            return null;
        }
    }

    private Integer getClassNumber(String splitLine[], Integer inputAmount) {
        for (int i = inputAmount; i < splitLine.length; i++) {
            if (splitLine[i].equals(String.valueOf(1))) {
                return i - inputAmount + 1;
            }
        }

        return null;
    }

    /* Parsing list to map */
    public HashMap<Integer, ArrayList<Point2D>> parseListToMap(ArrayList<NeuralObject> objects) {
        if (objects != null) {
            HashMap<Integer, ArrayList<Point2D>> objectsInMap = new HashMap<>();

            if (objects.size() > 0) {
                if (objects.get(0).getInputValues().size() <= 2) {
                    for (NeuralObject object : objects) {
                        Integer classNumber = object.getClassNumber();

                        if (objectsInMap.containsKey(classNumber)) {
                            ArrayList<Point2D> points = objectsInMap.get(classNumber);
                            points.add(getPoint(object));
                            objectsInMap.replace(classNumber, points);
                        } else {
                            ArrayList<Point2D> points = new ArrayList<>();
                            points.add(getPoint(object));
                            objectsInMap.put(classNumber, points);
                        }
                    }

                    return objectsInMap;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Point2D getPoint(NeuralObject neuralObject) {
        return new Point2D(neuralObject.getInputValues().get(0), neuralObject.getInputValues().get(1));
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pliki .csv (*.csv)", "*.csv"));
        return fileChooser;
    }
}
