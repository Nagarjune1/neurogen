package pl.wozniaktomek.layout.widget;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class Widget {
    protected VBox mainContainer;
    protected VBox contentContainer;

    private Text title;
    private HBox titleContainer;

    public Widget() {
        initializeMainContainer();
        initializeTitleContainer();
        initializeContentContainer();
    }

    public VBox getWidget() {
        return mainContainer;
    }

    public void setStyle(WidgetStyle widgetStyle) {
        if (widgetStyle.equals(WidgetStyle.PRIMARY)) {
            mainContainer.getStyleClass().add("widget-pane-primary");
            mainContainer.getStyleClass().remove("widget-pane-secondary");
            titleContainer.getStyleClass().add("widget-pane-primary-background-fill");
            titleContainer.getStyleClass().remove("widget-pane-secondary-background-fill");
        } else if (widgetStyle.equals(WidgetStyle.SECONDARY)) {
            mainContainer.getStyleClass().add("widget-pane-secondary");
            mainContainer.getStyleClass().remove("widget-pane-primary");
            titleContainer.getStyleClass().add("widget-pane-secondary-background-fill");
            titleContainer.getStyleClass().remove("widget-pane-primary-background-fill");
        }
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    private void initializeMainContainer() {
        mainContainer = new VBox();
        mainContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.getStyleClass().add("widget-pane-primary");
    }

    private void initializeTitleContainer() {
        titleContainer = new HBox();
        titleContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        titleContainer.setPadding(new Insets(12));
        titleContainer.setSpacing(12d);
        titleContainer.getStyleClass().add("widget-pane-primary-background-fill");

        title = new Text("Widget");
        title.getStyleClass().add("section-title-background");

        titleContainer.getChildren().add(title);
        mainContainer.getChildren().add(titleContainer);
    }

    private void initializeContentContainer() {
        contentContainer = new VBox();
        contentContainer.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        contentContainer.setPadding(new Insets(12));
        contentContainer.setSpacing(12d);

        mainContainer.getChildren().add(contentContainer);
    }

    private enum WidgetStyle {PRIMARY, SECONDARY}
}
