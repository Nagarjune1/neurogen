package pl.wozniaktomek.service.data;

import java.util.ArrayList;

public class DataObject {
    private ArrayList<Double> inputValues;
    private Integer classNumber;

    public DataObject(ArrayList<Double> inputValues, Integer classNumber) {
        this.inputValues = inputValues;
        this.classNumber = classNumber;
    }

    public ArrayList<Double> getInputValues() {
        return inputValues;
    }

    public Integer getClassNumber() {
        return classNumber;
    }
}
