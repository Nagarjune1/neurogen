package pl.wozniaktomek.service;

import javafx.geometry.Point2D;
import javafx.scene.chart.ScatterChart;
import javafx.stage.FileChooser;
import pl.wozniaktomek.ThesisApp;
import pl.wozniaktomek.service.util.DataGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataService {
    private final static String SEPARATOR = ";";
    private Integer classCounter;

    public HashMap<Integer, ArrayList<Point2D>> generateObjects(ScatterChart chart) {
        DataGenerator dataGenerator = new DataGenerator(chart);
        return dataGenerator.generateObjects(new Random().nextInt(3) + 2);
    }

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

    public HashMap<Integer, ArrayList<Point2D>> readFromFile() {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(ThesisApp.stage);

        if (file != null)
            return parseFromFile(file);
        else return null;
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
        for (int i = 1; i < 6; i++)
            if (objects.containsKey(i))
                classCounter++;

        return objects;
    }

    private String parseToString(HashMap<Integer, ArrayList<Point2D>> objects) {
        String content = "";

        for (Map.Entry<Integer, ArrayList<Point2D>> mapEntry : objects.entrySet()) {
            ArrayList<Point2D> classObjects = mapEntry.getValue();
            for (Point2D point : classObjects) {
                content = content.concat(point.getX() + SEPARATOR + point.getY() + SEPARATOR + getStringClassNumber(mapEntry.getKey()) + "\n");
            }
        }

        return content;
    }

    private String getStringClassNumber(Integer classNumber) {
        String content = "";

        for (int i = 1; i <= classCounter; i++) {
            if (classNumber.equals(i)) content = content.concat("1" + SEPARATOR);
            else content = content.concat("0" + SEPARATOR);
        }

        return content;
    }

    /* Parsing from file */
    private HashMap<Integer, ArrayList<Point2D>> parseFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try {
            lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getObjectsFromLines(lines);
    }

    private HashMap<Integer, ArrayList<Point2D>> getObjectsFromLines(List<String> lines) {
        HashMap<Integer, ArrayList<Point2D>> objects = new HashMap<>();

        for (String line : lines) {
            String[] splitLine = line.split(SEPARATOR);
            Point2D point = new Point2D(Double.valueOf(splitLine[0]), Double.valueOf(splitLine[1]));
            Integer classNumber = getClassNumber(splitLine);

            if (objects.containsKey(classNumber)) {
                ArrayList<Point2D> classPoints = objects.get(classNumber);
                classPoints.add(point);
                objects.replace(classNumber, classPoints);
            } else {
                ArrayList<Point2D> classPoints = new ArrayList<>();
                classPoints.add(point);
                objects.put(classNumber, classPoints);
            }
        }

        return objects;
    }

    private Integer getClassNumber(String[] splitLine) {
        for (int i = 1; i < splitLine.length; i++)
            if (splitLine[i].equals(String.valueOf(1)))
                return i - 1;

        return null;
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pliki .csv (*.csv)", "*.csv"));
        return fileChooser;
    }
}
