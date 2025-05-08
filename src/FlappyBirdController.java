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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FlappyBirdController implements Initializable
{
    AnimationTimer gameLoop; // Game Loop that repeatedly updates the game

    @FXML
    private AnchorPane plane; // Main game area

    @FXML
    private Rectangle bird; // The bird


    @FXML
    private Text score; //score on top of the screen

    @FXML
    private Text finalScore; //final score at the end screen

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
    private Bird birdObj;
    private ObstacleManager obstacleManager;

    ArrayList<Rectangle> obstacles = new ArrayList<>(); // ArrayList to hold obstacles
    ScoreManager scoreManager = new ScoreManager();


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
        score.setVisible(true);
    }

    @FXML
    void restartButtonPressed(ActionEvent event)
    {
        resetGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Start the game by creating the bird object and managing the obstacles object
        birdObj = new Bird(bird, jumpHeight);
        obstacleManager = new ObstacleManager(plane, planeHeight, planeWidth);

        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                update();
            }
        };


        obstacles.addAll(obstacleManager.createObstacles());
    }

    @FXML
    void pressed(KeyEvent event) // Detects a keyboard input
    {
            if(event.getCode() == KeyCode.SPACE)
            {
                if (!gameStarted && !endScreen.isVisible())
                {
                    startGame();
                }

                if(gameStarted)
                {
                    birdObj.fly(); // Bird flies if space is pressed
                    accelerationTime = 0; // Reset acceleration timer
                }
            }
    }

    // Called every game frame
    private void update()
    {
        accelerationTime++;
        gameTime++;

        // Apply gravity to the bird object
        birdObj.moveBirdY(yAxisMovement * accelerationTime);

        // Move and mange obstacles
        obstacleManager.moveObstacles(obstacles);


        if(updateScore(obstacles, bird))
        {
            scoreCounter++;
            score.setText(String.valueOf(scoreCounter));
        }

        // Creates new obstacles periodically
        if(gameTime % 350 == 0)
        {
            obstacles.addAll(obstacleManager.createObstacles());
        }

        // Checks if the bird is dead
        if(birdObj.isBirdDead(obstacles, plane))
        {
            endGame();
        }
    }


    private boolean updateScore(ArrayList<Rectangle> obstacles, Rectangle bird)
    {
        for (Rectangle obstacle: obstacles)
        {
            int birdPositionX = (int) (bird.getLayoutX() + bird.getX());
            int obstaclePositionX = (int) (obstacle.getLayoutX() + obstacle.getX());
            if(obstaclePositionX == birdPositionX)
            {
                return true;
            }
        }
        return false;
    }


    // Resets the entire game
    private void resetGame()
    {
        bird.setY(0); // Reset bird position
        plane.getChildren().removeAll(obstacles); // Remove all current obstacles
        obstacles.clear(); // Clear obstacle list
        accelerationTime = 0;
        gameTime = 0;
        time = 0;
        scoreCounter = 0;
        score.setText("0"); // Reset the score to 0
        score.setVisible(true);
        endScreen.setVisible(false);
        plane.requestFocus();
        gameStarted = true;
        gameLoop.start();
    }

    private void endGame()
    {
        score.setVisible(false);
        scoreManager.addScore(scoreCounter); // Saves the current score
        gameStarted = false;
        finalScore.setText("Score:" + score.getText());
        highestScore.setText("Highest Score:" + scoreManager.topScore());
        endScreen.setVisible(true);
        endScreen.toFront();
        gameLoop.stop();
    }


    @FXML
    private void scoreboardButtonPressed(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Scoreboard.fxml"));
            Parent root = loader.load();

            ScoreboardController controller = loader.getController();
            controller.scoreboard(scoreManager.loadScores());

            Scene scene = new Scene(root);
            FlappyBird.mainStage.setScene(scene);
            FlappyBird.mainStage.setTitle("Your 10 Scores");
            FlappyBird.mainStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
