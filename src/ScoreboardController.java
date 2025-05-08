import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.util.List;


public class ScoreboardController
{

    @FXML
    private AnchorPane plane;

    @FXML
    private Text scoreboardText;

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
    void backToGameButton(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FlappyBird.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            FlappyBird.mainStage.setScene(scene);
            FlappyBird.mainStage.setTitle("Your 10 Scores");
            FlappyBird.mainStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

