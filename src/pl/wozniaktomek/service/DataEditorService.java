package pl.wozniaktomek.service;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class DataEditorService {
    /* Minor changing in objects position */
    public HashMap<Integer, ArrayList<Point2D>> shuffleObjects(HashMap<Integer, ArrayList<Point2D>> points) {
        for (int i = 1; i < points.size() + 1; i++) {
            ArrayList<Point2D> oldPoints = points.get(i);
            ArrayList<Point2D> newPoints = new ArrayList<>();

            for (Point2D point : oldPoints) {
                newPoints.add(new Point2D(
                        point.getX() + (ThreadLocalRandom.current().nextDouble(-0.2, 0.2)),
                        point.getY() + (ThreadLocalRandom.current().nextDouble(-0.2, 0.2))));
            }

            points.replace(i, newPoints);
        }

        return points;
    }

    /* Generating random data set */
    public HashMap<Integer, ArrayList<Point2D>> generateObjects() {
        DataGeneratorService dataGeneratorService = new DataGeneratorService();
        return dataGeneratorService.generateObjects(ThreadLocalRandom.current().nextInt(2, 6));
    }
}
