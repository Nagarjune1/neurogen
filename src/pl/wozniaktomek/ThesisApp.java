package pl.wozniaktomek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/view/window.fxml"));
        root = fxmlLoader.load();
        setStage(primaryStage);

        windowControl = fxmlLoader.getController();
        windowControl.initialization();

        primaryStage.show();
    }

    private void setStage(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("NeuroGen");
        stage.setScene(new Scene(root, 1280, 720));
        stage.setMinWidth(1024);
        stage.setMinHeight(600);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
