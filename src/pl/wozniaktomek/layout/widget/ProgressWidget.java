package pl.wozniaktomek.layout.widget;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class ProgressWidget {
    private HBox progressContainer;
    private Integer progressSteps;
    private Integer progress;

    private enum State {EMPTY, COMPLETED}
    private enum Figure {CIRCLE, RECTANGLE}

    public ProgressWidget(HBox progressContainer, Integer progressSteps) {
        this.progressContainer = progressContainer;
        this.progressSteps = progressSteps;
        this.progress = 0;
        refreshProgress();
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
        refreshProgress();
    }

    private void refreshProgress() {
        progressContainer.getChildren().clear();
        if (progress == 0) {
            progressEmpty();
        } else {
            if (progress.equals(progressSteps))
                progressCompleted();
            else {
                progressInProgress();
            }
        }
    }

    private void progressEmpty() {
        for (int i = 1; i < progressSteps; i++)
            progressContainer.getChildren().addAll(
                    createShape(Figure.CIRCLE, State.EMPTY, String.valueOf(i)),
                    createShape(Figure.RECTANGLE, State.EMPTY, ""));

        progressContainer.getChildren().add(createShape(Figure.CIRCLE, State.EMPTY, String.valueOf(progressSteps)));
    }

    private void progressCompleted() {
        for (int i = 1; i < progressSteps; i++)
            progressContainer.getChildren().addAll(
                    createShape(Figure.CIRCLE, State.COMPLETED, String.valueOf(i)),
                    createShape(Figure.RECTANGLE, State.COMPLETED, ""));

        progressContainer.getChildren().add(createShape(Figure.CIRCLE, State.COMPLETED, String.valueOf(progressSteps)));
    }

    private void progressInProgress() {
        for (int i = 1; i <= progress; i++)
            progressContainer.getChildren().addAll(
                    createShape(Figure.CIRCLE, State.COMPLETED, String.valueOf(i)),
                    createShape(Figure.RECTANGLE, State.COMPLETED, ""));

        if (progress < progressSteps)
            for (int i = progress + 1; i < progressSteps; i++)
                progressContainer.getChildren().addAll(
                        createShape(Figure.CIRCLE, State.EMPTY, String.valueOf(i)),
                        createShape(Figure.RECTANGLE, State.EMPTY, ""));

        progressContainer.getChildren().add(createShape(Figure.CIRCLE, State.EMPTY, String.valueOf(progressSteps)));
    }

    private StackPane createShape(Figure figure, State state, String number) {
        StackPane stackPane = new StackPane();
        Text text = new Text(number);
        Shape shape = null;

        switch (figure) {
            case CIRCLE:
                shape = new Circle();
                ((Circle) shape).setRadius(42.0);
                break;

            case RECTANGLE:
                shape = new Rectangle();
                ((Rectangle) shape).setWidth(63.0);
                ((Rectangle) shape).setHeight(21.0);
                break;
        }

        switch (state) {
            case EMPTY:
                shape.setFill(Color.rgb(113, 140, 158, 0.0));
                text.setStyle("-fx-fill: rgba(113, 140, 158, 1.0); -fx-font-size: 28px");
                break;

            case COMPLETED:
                shape.setFill(Color.rgb(113, 140, 158, 1.0));
                text.setStyle("-fx-fill: rgba(255, 255, 255, 1.0); -fx-font-size: 28px");
                break;
        }

        shape.setSmooth(true);
        shape.setStrokeWidth(2.0);
        shape.setStroke(Color.rgb(113, 140, 158, 1.0));

        stackPane.getChildren().addAll(shape, text);
        return stackPane;
    }
}
