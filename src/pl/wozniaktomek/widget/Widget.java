package pl.wozniaktomek.widget;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.service.LayoutService;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Widget {
    protected LayoutService layoutService;
    private HashMap<WidgetStyle, ArrayList<String>> styles;

    private VBox mainContainer;
    private HBox titleContainer;
    private HBox titleTextContainer;
    VBox contentContainer;

    private Text title;
    private boolean isMinimized;
    private Button minimizationButton;

    Widget() {
        layoutService = new LayoutService();

        initializeStyles();
        initializeContainers();
        initializeWidgetSizeListener();

        initializeTitle();
        initializeMinimizationButton();
        setMinimizationVisibility(true);

        setStyle(WidgetStyle.PRIMARY);
    }

    public VBox getWidget() {
        return mainContainer;
    }

    void setStyle(WidgetStyle widgetStyle) {
        for (WidgetStyle style : WidgetStyle.values()) {
            if (widgetStyle.equals(style)) {
                mainContainer.getStyleClass().add(styles.get(style).get(0));
                titleContainer.getStyleClass().add(styles.get(style).get(1));
            } else {
                mainContainer.getStyleClass().remove(styles.get(style).get(0));
                titleContainer.getStyleClass().remove(styles.get(style).get(1));
            }
        }
    }

    void setTitle(String titleText) {
        title.setText(titleText);
    }

    void setMinimizationVisibility(boolean isButtonVisible) {
        minimizationButton.setVisible(isButtonVisible);
    }

    void minimizeWidget() {
        contentContainer.setVisible(false);
        setMainContainerHeight(45d);
        minimizationButton.setText("+");
        isMinimized = true;
    }

   void maximizeWidget() {
        contentContainer.setVisible(true);
        setMainContainerHeight(null);
        minimizationButton.setText("-");
        isMinimized = false;
    }

    private void initializeStyles() {
        styles = layoutService.getWidgetStyles();
    }

    private void initializeContainers() {
        mainContainer = layoutService.getVBox(null, null, null);
        isMinimized = false;

        titleContainer = layoutService.getHBox(6d, 12d, 12d);
        mainContainer.getChildren().add(titleContainer);

        contentContainer = layoutService.getVBox(6d, 12d, 12d);
        mainContainer.getChildren().add(contentContainer);

        titleTextContainer = layoutService.getHBox(null, null, null);
    }

    private void initializeWidgetSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> titleTextContainer.setPrefWidth(contentContainer.getWidth() - 64);
        contentContainer.widthProperty().addListener(stageSizeListener);
    }

    private void initializeTitle() {
        title = layoutService.getText("Widget", LayoutService.TextStyle.SECTION_BACKGROUND);
        titleTextContainer.getChildren().add(title);
        titleContainer.getChildren().add(titleTextContainer);
    }

    private void initializeMinimizationButton() {
        minimizationButton = layoutService.getButton("-", 32d, 32d, LayoutService.ButtonStyle.WHITE);

        minimizationButton.setOnAction(event -> {
            if (isMinimized) {
                maximizeWidget();
            } else {
                minimizeWidget();
            }
        });

        titleContainer.getChildren().add(minimizationButton);
    }

    private void setMainContainerHeight(Double height) {
        if (height != null) {
            mainContainer.setMaxHeight(height);
            mainContainer.setMinHeight(height);
            mainContainer.setPrefHeight(height);
        } else {
            mainContainer.setMaxHeight(Region.USE_COMPUTED_SIZE);
            mainContainer.setMinHeight(Region.USE_COMPUTED_SIZE);
            mainContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        }
    }

    public enum WidgetStyle {PRIMARY, SECONDARY, SUCCESS, FAILURE}
}
