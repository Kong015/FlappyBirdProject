import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Bird
{
    private Rectangle bird;
    private int jumpHeight;

    public Bird(Rectangle bird, int jumpHeight)
    {
        this.bird = bird;
        this.jumpHeight = jumpHeight;
    }

    public void fly()
    {
        // If bird is too close to the top, move it only as far as allowed
        if(bird.getLayoutY() <= jumpHeight)
        {
            moveBirdY(-(bird.getLayoutY() + bird.getY()));
        }

        // Otherwise, move bird up by jump height
        moveBirdY(-jumpHeight);
    }

    // Moves bird vertically
    public void moveBirdY(double positionChange)
    {
        // Adding a ceiling so that limits the high of where the bird can fly
        if(bird.getY() + positionChange < -200)
        {
            bird.setY(-200);
        }
        System.out.println(bird.getY());
        bird.setY(bird.getY() + positionChange);
    }

    // Checks if the bird falls below the certain amount or collides with the obstacles
    public boolean isBirdDead(ArrayList<Rectangle> obstacles, AnchorPane plane)
    {
        double birdY = bird.getLayoutY() + bird.getY();
        if(collisionDetection(obstacles, bird))
        {
            return true;
        }
        return birdY >= plane.getHeight();
    }

    // Checks for collision between the bird and any obstacle
    public boolean collisionDetection(ArrayList<Rectangle> obstacles, Rectangle bird)
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

    public Rectangle getBird() {
        return bird;
    }

    public void setBird(Rectangle bird) {
        this.bird = bird;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
}
