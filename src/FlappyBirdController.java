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
    AnimationTimer gameLoop;

    @FXML
    private AnchorPane plane;

    @FXML
    private Rectangle bird;

    double yAxis = 0.01;
    double time = 0;
    int gameTime = 0;
    int jumpHeight = 50;
    double planeHeight = 600;
    double planeWidth = 400;
    Random random = new Random();
    ArrayList<Rectangle> obstacles = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        load();
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
    void pressed(KeyEvent event)
    {
        if(event.getCode() == KeyCode.SPACE)
        {
            fly();
            time = 0;
        }
    }

    private void fly()
    {
        if(bird.getLayoutY() <= jumpHeight)
        {
            moveBirdY(-(bird.getLayoutY() + bird.getY()));
            return;
        }
        moveBirdY(-jumpHeight);
    }

    //Called every game frame
    private void update()
    {
        time++;
        gameTime++;
        moveBirdY(yAxis * time);
        moveObstacles(obstacles);

        if(gameTime % 350 == 0)
        {
            createObstacles();
        }
        if(isBirdDead())
        {
            resetBird();
        }
    }

    //Everything called once, at the game start
    private void load()
    {
        System.out.println("Game starting");
    }

    private void moveBirdY(double positionChange)
    {
        bird.setY(bird.getY() + positionChange);
    }

    private boolean isBirdDead()
    {
        double birdY = bird.getLayoutY() + bird.getY();
        if(collisionDetection())
        {
            System.out.println("Bird collision");
            return true;
        }
        return birdY >= plane.getHeight();
    }

    private void resetBird()
    {
        bird.setY(0);
        plane.getChildren().removeAll(obstacles);
        obstacles.clear();
        gameTime = 0;
        time = 0;
    }

    private void createObstacles()
    {
        int width = 25;
        double xPos = planeWidth;
        double space = 200;
        double recTopHeight = random.nextInt((int)(planeHeight - space - 100)) + 50;
        double recBottomHeight = planeHeight - space - recTopHeight;

        Rectangle rectangleTop = new Rectangle(xPos, 0, width, recTopHeight);
        Rectangle rectangleBottom = new Rectangle(xPos, recTopHeight + space, width, recBottomHeight);
        obstacles.add(rectangleTop);
        obstacles.add(rectangleBottom);
        plane.getChildren().addAll(rectangleTop,rectangleBottom);
    }

    private void moveRectangle(Rectangle rectangle, double amount)
    {
        rectangle.setX(rectangle.getX() + amount);
    }

    private void moveObstacles(ArrayList<Rectangle> obstacles)
    {
        ArrayList<Rectangle> outOfScreen = new ArrayList<>();

        for(Rectangle rectangle : obstacles)
        {
            moveRectangle(rectangle, -0.75);
            if(rectangle.getX() <= -rectangle.getWidth())
            {
                outOfScreen.add(rectangle);
            }
        }
        obstacles.removeAll(outOfScreen);
        plane.getChildren().removeAll(outOfScreen);
    }

    private boolean collisionDetection()
    {
        for (Rectangle rectangle : obstacles)
        {
            if(rectangle.getBoundsInParent().intersects(bird.getBoundsInParent()))
            {
                return true;
            }
        }
        return false;
    }
}
