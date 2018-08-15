package pl.wozniaktomek.service.util;

import javafx.geometry.Point2D;
import javafx.scene.chart.ScatterChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DataGenerator {
    private ScatterChart chart;

    public DataGenerator(ScatterChart chart) {
        this.chart = chart;
    }

    public HashMap<Integer, ArrayList<Point2D>> generateObjects(Integer classes) {
        System.out.println(classes);
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
        HashMap<Integer, ArrayList<Point2D>> objects = new HashMap<>();
        return objects;
    }

    private HashMap<Integer, ArrayList<Point2D>> generateThreeClasses() {
        HashMap<Integer, ArrayList<Point2D>> objects = new HashMap<>();
        return objects;
    }

    private HashMap<Integer, ArrayList<Point2D>> generateFourClasses() {
        HashMap<Integer, ArrayList<Point2D>> objects = new HashMap<>();
        return objects;
    }
}
