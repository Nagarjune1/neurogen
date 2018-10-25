package pl.wozniaktomek.neural.operation;

public class ActivationFunction {

    public Double getSigmoid(Double x) {
        return 1d / (1d + Math.exp(-x));
    }

    public Double getSigmoidDerivative(Double x) {
        return getSigmoid(x) * (1 - getSigmoid(x));
    }
}
