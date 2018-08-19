package pl.wozniaktomek.service.data;

import java.util.ArrayList;

public class DataObject {
    private ArrayList<Double> values;
    private Integer classNumber;

    public DataObject(ArrayList<Double> values, Integer classNumber) {
        this.values = values;
        this.classNumber = classNumber;
    }

    public Integer getClassNumber() {
        return classNumber;
    }
}
