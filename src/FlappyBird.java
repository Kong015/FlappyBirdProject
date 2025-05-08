import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlappyBird extends Application
{
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("FlappyBird.fxml"));
        Scene scene = new Scene(root); // attach scene graph to the scene
        mainStage.setTitle("Flappy Pig"); // displayed in window's title bar
        scene.getRoot().requestFocus(); // allows inputs from keyboard and mouse
        mainStage.setScene(scene); // attach the scene to stage
        mainStage.show();  // display the stage
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
