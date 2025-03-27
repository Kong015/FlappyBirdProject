import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
public class FlappyBirdController implements Initializable
{
    AnimationTimer gameLoop; // Game Loop that repeatedly updates the game

    @FXML
    private AnchorPane plane; // Main game area

    @FXML
    private Rectangle bird; // The bird

    double yAxis = 0.01; //Gravity acceleration factor
    double time = 0; // Time passed since last jump
    int gameTime = 0; // Total time passed since the game started
    int jumpHeight = 50;
    double planeHeight = 600;
    double planeWidth = 400;
    Random random = new Random();
    ArrayList<Rectangle> obstacles = new ArrayList<>(); // ArrayList to hold obstacles

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // Start the game by creating initial obstacles and starting the game loop
        createObstacles();
        moveObstacles(obstacles);
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                update();
            }
        };
        gameLoop.start();
    }

    @FXML
    void pressed(KeyEvent event) // Detects a keyboard input
    {
        // Bird flies if space is pressed
        if(event.getCode() == KeyCode.SPACE)
        {
            fly();
            time = 0; // Reset gravity timer
        }
    }

    private void fly()
    {
        // If bird is too close to the top, move it only as far as allowed
        if(bird.getLayoutY() <= jumpHeight)
        {
            moveBirdY(-(bird.getLayoutY() + bird.getY()));
            return;
        }

        // Otherwise, move bird up by jump height
        moveBirdY(-jumpHeight);
    }

    // Called every game frame
    private void update()
    {
        time++;
        gameTime++;

        // Apply gravity
        moveBirdY(yAxis * time);

        // Move and mange obstacles
        moveObstacles(obstacles);

        // Creates new obstacles periodically
        if(gameTime % 350 == 0)
        {
            createObstacles();
        }

        // Checks if the bird is dead
        if(isBirdDead())
        {
            resetBird();
        }
    }

    // Moves bird vertically
    private void moveBirdY(double positionChange)
    {
        bird.setY(bird.getY() + positionChange);
    }

    // Checks if the bird falls below the certain amount or collides with the obstacles
    private boolean isBirdDead()
    {
        double birdY = bird.getLayoutY() + bird.getY();
        if(collisionDetection())
        {
            return true;
        }
        return birdY >= plane.getHeight();
    }

    // Resets the entire game
    private void resetBird()
    {
        bird.setY(0); // Reset bird position
        plane.getChildren().removeAll(obstacles); // Remove all current obstacles
        obstacles.clear(); // Clear obstacle list
        gameTime = 0;
        time = 0;
    }

    // Create the obstacles
    private void createObstacles()
    {
        int width = 50; // Width of obstacle
        double xPos = planeWidth; // Location of where it will spawn
        double space = 200; // Space between the tubes
        double recTopHeight = random.nextInt((int)(planeHeight - space - 100)) + 50; // Height of the top tube
        double recBottomHeight = planeHeight - space - recTopHeight; // Height of the bottom tube

        // Create both of the tubes
        Rectangle rectangleTop = new Rectangle(xPos, 0, width, recTopHeight);
        Rectangle rectangleBottom = new Rectangle(xPos, recTopHeight + space, width, recBottomHeight);

        // Add the tubes to the obstacles arraylist and the game plane
        obstacles.add(rectangleTop);
        obstacles.add(rectangleBottom);
        plane.getChildren().addAll(rectangleTop,rectangleBottom);
    }

    // Moves a rectangle horizontally
    private void moveRectangle(Rectangle rectangle, double amount)
    {
        rectangle.setX(rectangle.getX() + amount);
    }

    // Moves the obstacles in the arraylist towards the bird and removes those off-screen
    private void moveObstacles(ArrayList<Rectangle> obstacles)
    {
        // Collects the obstacles that go off the screen
        ArrayList<Rectangle> outOfScreen = new ArrayList<>();

        for(Rectangle rectangle : obstacles)
        {
            moveRectangle(rectangle, -0.75); // Move left
            if(rectangle.getX() <= -rectangle.getWidth())
            {
                outOfScreen.add(rectangle); // Mark for removal
            }
        }

        // Removes all the obstacles that are off-screen
        obstacles.removeAll(outOfScreen);
        plane.getChildren().removeAll(outOfScreen);
    }

    // Checks for collision between the bird and any obstacle
    private boolean collisionDetection()
    {
        for (Rectangle rectangle : obstacles)
        {
            if(rectangle.getBoundsInParent().intersects(bird.getBoundsInParent()))
            {
                return true; // Collision detected
            }
        }
        return false; // No Collision
    }
}
