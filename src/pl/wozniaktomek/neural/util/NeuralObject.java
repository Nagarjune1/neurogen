package pl.wozniaktomek.neural.util;

import java.util.ArrayList;

public class NeuralObject {
    private ArrayList<Double> inputValues;
    private ArrayList<Double> correctAnswer;
    private Integer classNumber;

    public NeuralObject(ArrayList<Double> inputValues, Integer classNumber) {
        this.inputValues = inputValues;
        this.classNumber = classNumber;
    }

    /* Getters */
    public ArrayList<Double> getInputValues() {
        return inputValues;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public ArrayList<Double> getCorrectAnswer() {
        return correctAnswer;
    }

    /* Setter */
    public void setCorrectAnswer(ArrayList<Double> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
