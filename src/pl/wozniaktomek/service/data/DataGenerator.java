package pl.wozniaktomek.service.data;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    private ArrayList<DataGeneratorSector> usedSectors;

    public HashMap<Integer, ArrayList<Point2D>> generateObjects(Integer classes) {
        switch (classes) {
            case 2:
                return generateTwoClasses();
            case 3:
                return generateThreeClasses();
            case 4:
                return generateFourClasses();

            default:
                return new HashMap<>();
        }
    }

    private HashMap<Integer, ArrayList<Point2D>> generateTwoClasses() {
        HashMap<Integer, ArrayList<Point2D>> points = new HashMap<>();
        if (ThreadLocalRandom.current().nextInt() > 50) {
            if (ThreadLocalRandom.current().nextInt() > 50) {
                generateSector(DataGeneratorSector.NW, points, 1, 60);
                generateSector(DataGeneratorSector.SE, points, 2, 60);
            } else {
                generateSector(DataGeneratorSector.NE, points, 1, 60);
                generateSector(DataGeneratorSector.SW, points, 2, 60);
            }
        } else {
            if (ThreadLocalRandom.current().nextInt() > 50) {
                generateSector(DataGeneratorSector.N, points, 1, 60);
                generateSector(DataGeneratorSector.S, points, 2, 60);
            } else {
                generateSector(DataGeneratorSector.W, points, 1, 60);
                generateSector(DataGeneratorSector.E, points, 2, 60);
            }
        }

        return points;
    }

    private HashMap<Integer, ArrayList<Point2D>> generateThreeClasses() {
        HashMap<Integer, ArrayList<Point2D>> points = new HashMap<>();
        usedSectors = new ArrayList<>();

        DataGeneratorSector sector = getSector(ThreadLocalRandom.current().nextInt(1, 5));
        usedSectors.add(sector);
        fillSectors(Objects.requireNonNull(sector));

        int counter = 0;
        while (counter < 3) {
            int sectorNumber = ThreadLocalRandom.current().nextInt(0, usedSectors.size());
            if (usedSectors.get(sectorNumber) != null) {
                generateSector(usedSectors.get(sectorNumber), points, ++counter, 60);
                usedSectors.remove(usedSectors.get(sectorNumber));
            }
        }

        return points;
    }

    private HashMap<Integer, ArrayList<Point2D>> generateFourClasses() {
        HashMap<Integer, ArrayList<Point2D>> points = new HashMap<>();
        usedSectors = new ArrayList<>();

        int counter = 0;
        while (counter < 4) {
            DataGeneratorSector sector = getSector(ThreadLocalRandom.current().nextInt(5, 9));
            if (!usedSectors.contains(sector)) {
                generateSector(sector, points, ++counter, 60);
                usedSectors.add(sector);
            }
        }

        return points;
    }

    private void generateSector(DataGeneratorSector sector, HashMap<Integer, ArrayList<Point2D>> points, Integer classNumber, Integer amount) {
        for (int i = 0; i < amount; i++) {
            addObject(points, classNumber, new Point2D(
                    ThreadLocalRandom.current().nextDouble(sector.getMinX(), sector.getMaxX()),
                    ThreadLocalRandom.current().nextDouble(sector.getMinY(), sector.getMaxY())));
        }
    }

    private void addObject(HashMap<Integer, ArrayList<Point2D>> points, Integer classNumber, Point2D point) {
        if (points.containsKey(classNumber)) {
            points.get(classNumber).add(point);
        } else {
            ArrayList<Point2D> listOfPoints = new ArrayList<>();
            listOfPoints.add(point);
            points.put(classNumber, listOfPoints);
        }
    }

    private DataGeneratorSector getSector(Integer number) {
        switch (number) {
            case 1:
                return DataGeneratorSector.N;
            case 2:
                return DataGeneratorSector.S;
            case 3:
                return DataGeneratorSector.W;
            case 4:
                return DataGeneratorSector.E;
            case 5:
                return DataGeneratorSector.NW;
            case 6:
                return DataGeneratorSector.NE;
            case 7:
                return DataGeneratorSector.SW;
            case 8:
                return DataGeneratorSector.SE;

            default:
                return null;
        }
    }

    private void fillSectors(DataGeneratorSector sector) {
        switch (sector) {
            case N:
                usedSectors.add(DataGeneratorSector.SW);
                usedSectors.add(DataGeneratorSector.SE);
                break;

            case S:
                usedSectors.add(DataGeneratorSector.NW);
                usedSectors.add(DataGeneratorSector.NE);
                break;

            case E:
                usedSectors.add(DataGeneratorSector.NW);
                usedSectors.add(DataGeneratorSector.SW);
                break;

            case W:
                usedSectors.add(DataGeneratorSector.NE);
                usedSectors.add(DataGeneratorSector.SE);
                break;
        }
    }
}
