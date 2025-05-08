import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import java.util.List;


public class ScoreboardController
{

    @FXML
    private AnchorPane plane;

    @FXML
    private Text scoreboardText;

    // Displays the top ten scores of the user
    public void scoreboard(List<Integer> scores)
    {
        String scoreboard = "";

        for(int x = 0; x < scores.size(); x++)
        {
            scoreboard += "Score: " + scores.get(x) + "\n";
        }
        scoreboardText.setText(scoreboard);
    }

    @FXML
    // Returns the scene back to the game
    void backToGameButton(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FlappyPig.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            FlappyPig.mainStage.setScene(scene);
            FlappyPig.mainStage.setTitle("Your 10 Scores");
            FlappyPig.mainStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

