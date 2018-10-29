package pl.wozniaktomek.neural.util;

public class ActivationFunction {
    private FunctionType functionType;

    public ActivationFunction(FunctionType functionType) {
        this.functionType = functionType;
    }

    public Double useFunction(Double x) {
        switch (functionType) {
            case SIGMOID:
                return 1d / (1d + Math.exp(-x));

                default:
                    return 0d;
        }
    }

    public enum FunctionType {SIGMOID}
}
