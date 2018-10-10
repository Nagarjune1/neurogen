package pl.wozniaktomek.service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.wozniaktomek.layout.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<Widget.WidgetStyle, ArrayList<String>> getWidgetStyles() {
        HashMap<Widget.WidgetStyle, ArrayList<String>> styles = new HashMap<>();

        ArrayList<String> styleClasses = new ArrayList<>();
        styleClasses.add("widget-primary");
        styleClasses.add("widget-primary-header");
        styleClasses.add("widget-primary-title");
        styles.put(Widget.WidgetStyle.PRIMARY, styleClasses);

        styleClasses = new ArrayList<>();
        styleClasses.add("widget-secondary");
        styleClasses.add("widget-secondary-header");
        styleClasses.add("widget-secondary-title");
        styles.put(Widget.WidgetStyle.SECONDARY, styleClasses);

        return styles;
    }

    public enum TextStyle {WIDGET_PRIMARY_TITLE, WIDGET_SECONDARY_TITLE, HEADING, STATUS}

    public enum ButtonStyle {SECONDARY, WHITE}
}
