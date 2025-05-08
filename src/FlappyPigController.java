import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FlappyPigController implements Initializable
{
    AnimationTimer gameLoop; // Game Loop that repeatedly updates the game

    @FXML
    private AnchorPane plane; // Main game area

    @FXML
    private Rectangle pig; // The pig

    @FXML
    private Text score; // Score on top of the screen

    @FXML
    private Text finalScore; // Final score at the end screen

    @FXML
    private Text highestScore;

    @FXML
    private AnchorPane startScreen; // Start screen

    @FXML
    private AnchorPane endScreen; // End screen

    double yAxisMovement = 0.015; //Gravity acceleration factor
    double accelerationTime = 0;
    double time = 0; // Time passed since last jump
    int gameTime = 0; // Total time passed since the game started
    int jumpHeight = 50;
    double planeHeight = 600;
    double planeWidth = 400;
    int scoreCounter = 0;
    private Pig pigObj; // Pig object handling movement and collision
    private ObstacleManager obstacleManager; // Manages creation and movement of pipes
    private long lastTime = 0; // Used to track the elapsed time

    ArrayList<Rectangle> obstacles = new ArrayList<>(); // ArrayList to hold obstacles
    ScoreManager scoreManager = new ScoreManager(); // Handles file-based score tracking


    private boolean gameStarted = false;
    @FXML
    // Start game when button pressed
    void startButtonPressed(ActionEvent event)
    {
        startGame();
    }

    // Starts the game
    private void startGame()
    {
        startScreen.setVisible(false); // Makes the start screen invisible
        gameStarted = true; // Starts the game
        plane.requestFocus(); // Focuses the screen back to the main game
        gameLoop.start(); // Starts the game loop
        score.setVisible(true); // Makes the score visible
    }

    @FXML
    // Restarts the game if the button is pressed
    void restartButtonPressed(ActionEvent event)
    {
        resetGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Start the game by creating the pig object and managing the obstacles object
        pigObj = new Pig(pig, jumpHeight);
        obstacleManager = new ObstacleManager(plane, planeHeight, planeWidth);

        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                //Uses elapsed instead frame generation so it compatible with different computers and laptops
                if (lastTime > 0)
                {
                    double elapsedTime = (l - lastTime) / 1000000000.0;
                    update(elapsedTime);
                }
                lastTime = l;
            }
        };
        obstacles.addAll(obstacleManager.createObstacles());
    }

    @FXML
    void pressed(KeyEvent event) // Detects a keyboard input
    {
            if(event.getCode() == KeyCode.SPACE)
            {
                // Starts the game if space is pressed and end screen is not visable
                if (!gameStarted && !endScreen.isVisible())
                {
                    startGame();
                }
                if(gameStarted)
                {
                    pigObj.fly(); // Pig flies if space is pressed
                    accelerationTime = 0; // Reset acceleration timer
                }
            }
    }

    // Called every game frame
    private void update(double elapsedTime)
    {
        accelerationTime += elapsedTime;
        gameTime++;

        // Apply gravity to the pig object
        pigObj.movePigY(yAxisMovement * accelerationTime * 100);

        // Move and mange obstacles
        obstacleManager.moveObstacles(obstacles);


        if(updateScore(obstacles, pig))
        {
            scoreCounter++;
            score.setText(String.valueOf(scoreCounter));
        }

        // Creates new obstacles periodically
        if(gameTime % 350 == 0)
        {
            obstacles.addAll(obstacleManager.createObstacles());
        }

        // Checks if the pig is dead and ends the game
        if(pigObj.isPigDead(obstacles, plane))
        {
            Sound.playCollision();
            endGame();
        }
    }


    // Checks if pig has passed an obstacle
    private boolean updateScore(ArrayList<Rectangle> obstacles, Rectangle pig)
    {
        for (Rectangle obstacle: obstacles)
        {
            int pigPositionX = (int) (pig.getLayoutX() + pig.getX()); //gets the pig's location on the x-axis
            int obstaclePositionX = (int) (obstacle.getLayoutX() + obstacle.getX()); //gets the obstacle location on the x-axis
            if(obstaclePositionX == pigPositionX)
            {
                Sound.playScore();
                return true;
            }
        }
        return false;
    }


    // Resets the entire game
    private void resetGame()
    {
        pig.setY(0); // Reset pig position
        plane.getChildren().removeAll(obstacles); // Remove all current obstacles
        obstacles.clear(); // Clear obstacle list
        accelerationTime = 0;
        gameTime = 0;
        time = 0;
        scoreCounter = 0;
        lastTime = 0;
        score.setText("0"); // Reset the score to 0
        score.setVisible(true); // Reveals the score on top of the screen
        endScreen.setVisible(false); // Hides the end screen
        plane.requestFocus(); // Regain key focus
        gameStarted = true;
        gameLoop.start(); // Restart the game
    }

    private void endGame()
    {
        score.setVisible(false); // Hide the score
        scoreManager.addScore(scoreCounter); // Saves the current score
        gameStarted = false;
        finalScore.setText("Score:" + score.getText()); //Reveals your final score
        highestScore.setText("Highest Score:" + scoreManager.topScore()); // Reveals your highest score
        endScreen.setVisible(true); // Reveals end screen
        endScreen.toFront(); // Makes the end screen appear on the front so that pipes don't block the text
        gameLoop.stop();
    }


    @FXML
    // Opens the scoreboard scene
    private void scoreboardButtonPressed(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Scoreboard.fxml"));
            Parent root = loader.load();

            // Passes the scores to the scoreboard
            ScoreboardController controller = loader.getController();
            controller.scoreboard(scoreManager.loadScores());

            Scene scene = new Scene(root);
            FlappyPig.mainStage.setScene(scene);
            FlappyPig.mainStage.setTitle("Your Top 10 Scores");
            FlappyPig.mainStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
