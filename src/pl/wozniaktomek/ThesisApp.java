package pl.wozniaktomek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pl.wozniaktomek.layout.control.WindowControl;

public class ThesisApp extends Application {
    public static WindowControl windowControl;
    public static Stage stage;
    private Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/control/view/window.fxml"));
        root = fxmlLoader.load();
        setStage(primaryStage);

        windowControl = fxmlLoader.getController();
        windowControl.initialization();

        primaryStage.show();
    }

    private void setStage(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("NeuroGen");
        setResolution(stage);
        stage.setMinWidth(1024);
        stage.setMinHeight(600);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void setResolution(Stage stage) {
        Rectangle2D screenResolution = Screen.getPrimary().getVisualBounds();
        System.out.println(screenResolution.getWidth() + " x " + screenResolution.getHeight());

        double width, height;

        if (screenResolution.getWidth() <= 1280) {
            width = 1024;
        } else {
            width = 1280;
        }

        if (screenResolution.getHeight() <= 728) {
            height = 628;
        } else {
            height = 720;
        }

        stage.setScene(new Scene(root, width, height));
    }
}
