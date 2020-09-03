import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {

    static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println(getClass().getResource("ui.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui.fxml"));
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setMaximized(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("NoseJob v1.0");
        primaryStage.setScene(scene);

        stage = primaryStage;
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream( "icon.png" )));
        primaryStage.show();

    }

    public static void main(String args[]){
        launch(args);
    }
}
