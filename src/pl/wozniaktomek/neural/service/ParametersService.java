package pl.wozniaktomek.neural.service;

import pl.wozniaktomek.neural.NeuralNetwork;
import pl.wozniaktomek.neural.util.NeuralObject;
import pl.wozniaktomek.neural.util.Parameters;

import java.util.ArrayList;

public class ParametersService {
    private NeuralNetwork neuralNetwork;
    private Parameters parameters;

    public ParametersService(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        parameters = neuralNetwork.getParameters();
    }

    public void approveData(ArrayList<NeuralObject> learningData) {
        setCorrectAnswers(learningData);

        neuralNetwork.getStructure().clearStructure();
        neuralNetwork.getStructure().addLayer(parameters.getInputSize());
        neuralNetwork.getStructure().addLayer(parameters.getOutputSize());

        neuralNetwork.getLearning().getLearningWidget().enableControls();
    }

    public void denyData() {
        parameters.setInputSize(0);
        parameters.setOutputSize(0);
        neuralNetwork.getStructure().clearStructure();

        neuralNetwork.getLearning().getLearningWidget().disableControls();
    }

    private void setCorrectAnswers(ArrayList<NeuralObject> neuralObjects) {
        for (NeuralObject neuralObject : neuralObjects) {
            ArrayList<Double> correctAnswer = new ArrayList<>();

            for (int i = 1; i <= parameters.getOutputSize(); i++) {
                if (i == neuralObject.getClassNumber()) {
                    correctAnswer.add(1.0);
                } else {
                    correctAnswer.add(0.0);
                }
            }

            neuralObject.setCorrectAnswer(correctAnswer);
        }
    }
}
