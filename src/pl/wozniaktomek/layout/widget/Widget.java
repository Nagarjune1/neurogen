package pl.wozniaktomek.layout.widget;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.service.LayoutService;

public abstract class Widget {
    LayoutService layoutService;

    private VBox mainContainer;
    private HBox titleContainer;
    private HBox titleTextContainer;
    protected VBox contentContainer;

    private Text widgetTitle;
    private boolean isMinimized;
    private Button minimizationButton;

    Widget() {
        layoutService = new LayoutService();
        initializeContainers();
        initializeWidgetSizeListener();
    }

    void createWidget(String title) {
        initializeTitle(title);
        initializeMinimizationButton();
        setMinimizationVisibility(true);
        setStyle();
    }

    public VBox getWidget() {
        return mainContainer;
    }

    private void setStyle() {
        mainContainer.getStyleClass().add("widget-primary");
        titleContainer.getStyleClass().add("widget-primary-header");
        widgetTitle.getStyleClass().add("widget-primary-title");
    }

    void setMinimizationVisibility(boolean isButtonVisible) {
        minimizationButton.setVisible(isButtonVisible);
    }

    private void minimizeWidget() {
        contentContainer.setVisible(false);
        setMainContainerHeight(64d);
        minimizationButton.setText("+");
        isMinimized = true;
    }

    private void maximizeWidget() {
        contentContainer.setVisible(true);
        setMainContainerHeight(null);
        minimizationButton.setText("-");
        isMinimized = false;
    }

    private void initializeContainers() {
        mainContainer = layoutService.getVBox(null, null, null);
        isMinimized = false;

        titleContainer = layoutService.getHBox(16d, 16d, 12d);
        mainContainer.getChildren().add(titleContainer);

        contentContainer = layoutService.getVBox(12d, 24d, 12d);
        mainContainer.getChildren().add(contentContainer);

        titleTextContainer = layoutService.getHBox(null, null, null);
    }

    private void initializeWidgetSizeListener() {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> titleTextContainer.setPrefWidth(contentContainer.getWidth() - 64);
        contentContainer.widthProperty().addListener(stageSizeListener);
    }

    private void initializeTitle(String title) {
        widgetTitle = layoutService.getText(title, LayoutService.TextStyle.WIDGET_PRIMARY_TITLE);
        titleTextContainer.getChildren().add(widgetTitle);
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

    void setTitle(String title) {
        widgetTitle.setText(title);
    }
}
