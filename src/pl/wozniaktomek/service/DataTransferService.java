package pl.wozniaktomek.service;

import javafx.geometry.Point2D;
import javafx.stage.FileChooser;
import pl.wozniaktomek.NeuroGenApp;
import pl.wozniaktomek.neural.util.NeuralObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataTransferService {
    private final static String SEPARATOR = ";";
    private Integer classCounter;
    private String transferStatus;

    /*
     * Parsing to file
     */
    public void saveToFile(HashMap<Integer, ArrayList<Point2D>> objects) {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(NeuroGenApp.stage);
        if (file != null) {
            try {
                Files.write(Paths.get(file.getPath()), parseToString(organizeObjects(objects)).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Sorting objects by class */
    private HashMap<Integer, ArrayList<Point2D>> organizeObjects(HashMap<Integer, ArrayList<Point2D>> objects) {
        for (int i = 1; i <= 8; i++) {
            if (!objects.containsKey(i)) {
                for (int j = i + 1; j <= 8; j++) {
                    if (objects.containsKey(j)) {
                        objects.put(i, objects.get(j));
                        objects.remove(j);
                        break;
                    }
                }
            }
        }

        classCounter = 0;
        for (int i = 1; i <= 8; i++) {
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

    /*
     * Parsing from file
     */
    public ArrayList<NeuralObject> readFromFile() {
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(NeuroGenApp.stage);

        if (file != null) {
            return parseFromFile(file);
        } else {
            transferStatus = "Nie wybrano pliku.";
            return null;
        }
    }

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
        Integer classAmount = getClassAmount(lines.get(0));

        if (classAmount > 0) {
            for (int i = 1; i < lines.size(); i++) {
                if (!lines.get(i).equals("")) {
                    String[] splitLine = lines.get(i).split(SEPARATOR);
                    ArrayList<Double> inputValues = getInputValues(splitLine, inputAmount, i + 1);
                    Integer classNumber = getClassNumber(splitLine, inputAmount, classAmount, i + 1);

                    if (inputValues != null && classNumber != null) {
                        objects.add(new NeuralObject(inputValues, classNumber));
                    } else {
                        return null;
                    }
                }
            }
        } else if (classAmount.equals(0)) {
            for (int i = 1; i < lines.size(); i++) {
                if (!lines.get(i).equals("")) {
                    String[] splitLine = lines.get(i).split(SEPARATOR);
                    ArrayList<Double> inputValues = getInputValues(splitLine, inputAmount, i + 1);

                    if (inputValues != null) {
                        objects.add(new NeuralObject(inputValues, null));
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }

        if (objects.size() > 0) {
            transferStatus = "Zbiór danych jest poprawny.";
            return objects;
        } else {
            transferStatus = "Zbiór danych jest pusty.";
            return null;
        }
    }

    private Integer getInputAmount(String line) {
        String[] splitLine = line.split(SEPARATOR);
        return Integer.valueOf(splitLine[1]);
    }

    private Integer getClassAmount(String line) {
        String[] splitLine = line.split(SEPARATOR);
        return Integer.valueOf(splitLine[2]);
    }

    private ArrayList<Double> getInputValues(String splitLine[], Integer inputAmount, Integer line) {
        ArrayList<Double> inputValues = new ArrayList<>();

        for (int j = 0; j < inputAmount; j++) {
            if (Objects.equals(splitLine[j], "")) {
                transferStatus = "Dane w linii " + line + " są niepoprawne - pusta linia";
                return null;
            } else {
                inputValues.add(Double.valueOf(splitLine[j]));
            }
        }

        if (inputValues.size() == inputAmount) {
            return inputValues;
        } else {
            transferStatus = "Dane w linii " + line + " są niepoprawne - błędny rozmiar danych wejściowych";
            return null;
        }
    }

    private Integer getClassNumber(String splitLine[], Integer inputAmount, Integer classAmount, Integer line) {
        if (splitLine.length != (inputAmount + classAmount)) {
            transferStatus = "Dane w linii " + line + " są niepoprawne - błędna liczba klas.";
            return null;
        }

        Integer classNumber = null;
        for (int i = inputAmount; i < splitLine.length; i++) {
            if (splitLine[i].equals(String.valueOf(1))) {
                if (classNumber == null) {
                    classNumber = i - inputAmount + 1;
                } else {
                    transferStatus = "Dane w linii " + line + " są niepoprawne - przypisano więcej niż jedną klasę.";
                    return null;
                }
            }
        }

        if (classNumber == null) {
            transferStatus = "Dane w linii " + line + " są niepoprawne - brak przypisanej klasy.";
        }

        return classNumber;
    }

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

    /* File chooser */
    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pliki .csv (*.csv)", "*.csv"));
        return fileChooser;
    }

    /* Getter */
    public String getTransferStatus() {
        return transferStatus;
    }
}
