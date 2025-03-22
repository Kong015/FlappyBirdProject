import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
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
    int jumpHeight = 50;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        load();

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
        }
    }

    private void fly()
    {
        if(bird.getLayoutY() <= jumpHeight)
        {
            moveBirdY(-(bird.getLayoutY() + bird.getY()));
            time = 0;
            return;
        }

        moveBirdY(-jumpHeight);
        time = 0;
    }

    //Called every game frame
    private void update()
    {
        time ++;
        moveBirdY(yAxis * time);

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
        return birdY >= plane.getHeight();
    }

    private void resetBird()
    {
        bird.setY(0);
        time = 0;
    }

}
