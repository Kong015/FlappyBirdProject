import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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

    double yAxisMovement = 0.01; //Gravity acceleration factor
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
        gameLoop.start();
    }

    @FXML
    void pressed(KeyEvent event) // Detects a keyboard input
    {
        // Bird flies if space is pressed
        if(event.getCode() == KeyCode.SPACE)
        {
            birdObj.fly();
            accelerationTime = 0; // Reset acceleration timer
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
            resetGame();
        }
    }


    private boolean updateScore(ArrayList<Rectangle> obstacles, Rectangle bird)
    {
        for (Rectangle obstacle: obstacles)
        {
            int birdPositionX = (int) (bird.getLayoutX() + bird.getX());
            int obstaclePostionX = (int) (obstacle.getLayoutX() + obstacle.getX());
            if(obstaclePostionX == birdPositionX)
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
        scoreCounter = 0;
        score.setText("0"); // Reset the score to 0
        time = 0;
    }
}
