package pl.wozniaktomek.service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LayoutService {
    public Button getButton(String text, Double width, Double height, ButtonStyle buttonStyle) {
        Button button = new Button();
        button.setText(text);

        if (width != null) {
            button.setPrefWidth(width);
            button.setMinWidth(width);
        }

        if (height != null) {
            button.setPrefHeight(height);
            button.setMinHeight(height);
        }

        if (buttonStyle != null) {
            if (buttonStyle.equals(ButtonStyle.SECONDARY)) {
                button.getStyleClass().add("button-secondary");
            } else if (buttonStyle.equals(ButtonStyle.WHITE)) {
                button.getStyleClass().add("button-white");
            }
        }

        return button;
    }

    public Button getButton(String text) {
        Button button = new Button();
        button.setText(text);
        return button;
    }

    public CheckBox getCheckBox(String text, Double size) {
        CheckBox checkBox = new CheckBox();
        checkBox.setText(text);
        if (size != null) {
            checkBox.setPrefSize(size, size);
        } else {
            checkBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        }

        return checkBox;
    }

    public HBox getHBox(Double paddingVertical, Double paddingHorizontal, Double spacing) {
        HBox hBox = new HBox();

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setAlignment(Pos.CENTER_LEFT);

        if (paddingHorizontal != null && paddingVertical != null) {
            hBox.setPadding(new Insets(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal));
        }

        if (spacing != null) {
            hBox.setSpacing(spacing);
        }

        return hBox;
    }

    public VBox getVBox(Double paddingVertical, Double paddingHorizontal, Double spacing) {
        VBox vBox = new VBox();

        vBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        vBox.setAlignment(Pos.TOP_LEFT);

        if (paddingHorizontal != null && paddingVertical != null) {
            vBox.setPadding(new Insets(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal));
        }

        if (spacing != null) {
            vBox.setSpacing(spacing);
        }

        return vBox;
    }

    public Text getText(String content, TextStyle textStyle) {
        Text text = new Text(content);

        switch (textStyle) {
            case WIDGET_PRIMARY_TITLE:
                text.getStyleClass().add("widget-primary-title");
                break;

            case WIDGET_SECONDARY_TITLE:
                text.getStyleClass().add("widget-secondary-title");
                break;

            case HEADING:
                text.getStyleClass().add("text-heading");
                break;

            case STATUS:
                text.getStyleClass().add("text-status");
                break;

            case STATUS_WHITE:
                text.getStyleClass().add("text-status-white");
                break;

            case PARAGRAPH:
                text.getStyleClass().add("text-paragraph");
                break;

            case PARAGRAPH_THEME:
                text.getStyleClass().add("text-paragraph-theme");
                break;

            case PARAGRAPH_WHITE:
                text.getStyleClass().add("text-paragraph-white");
                break;
        }

        return text;
    }

    public TextFlow getTextFlow(Double paddingVertical, Double paddingHorizontal, Double width, Text content) {
        TextFlow textFlow = new TextFlow();

        if (paddingHorizontal != null && paddingVertical != null) {
            textFlow.setPadding(new Insets(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal));
        } else if (paddingVertical != null) {
            textFlow.setPadding(new Insets(paddingVertical, paddingVertical * 2, paddingVertical, paddingVertical * 2));
        }

        if (width != null) {
            textFlow.setPrefWidth(width);
        }

        if (content != null) {
            textFlow.getChildren().add(content);
        }

        return textFlow;
    }

    public enum TextStyle {WIDGET_PRIMARY_TITLE, WIDGET_SECONDARY_TITLE, HEADING, STATUS, STATUS_WHITE, PARAGRAPH, PARAGRAPH_THEME, PARAGRAPH_WHITE}

    public enum ButtonStyle {SECONDARY, WHITE}
}
