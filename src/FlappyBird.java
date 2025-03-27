import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlappyBird extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("FlappyBirdController.fxml"));
        Scene scene = new Scene(root); // attach scene graph to the scene
        primaryStage.setTitle("Test"); // displayed in window's title bar
        scene.getRoot().requestFocus(); // allows inputs from keyboard and mouse
        primaryStage.setScene(scene); // attach the scene to stage
        primaryStage.show();  // display the stage
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
