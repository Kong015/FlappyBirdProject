import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

public class Pig
{
    private Rectangle pig;
    private int jumpHeight;

    public Pig(Rectangle pig, int jumpHeight)
    {
        this.pig = pig;
        this.jumpHeight = jumpHeight;
    }

    // Makes the pig jump upward
    public void fly()
    {
        // If pig is too close to the top, move it only as far as allowed
        if(pig.getLayoutY() <= jumpHeight)
        {
            movePigY(-(pig.getLayoutY() + pig.getY()));
        }

        // Otherwise, move pig up by jump height
        movePigY(-jumpHeight);
    }

    // Moves pig vertically/gravity
    public void movePigY(double positionChange)
    {
        // Adding a ceiling so that limits the high of where the pig can fly
        if(pig.getY() + positionChange < -200)
        {
            pig.setY(-200);
        }
        pig.setY(pig.getY() + positionChange);
    }

    // Checks if the pig falls below the certain amount or collides with the obstacles
    public boolean isPigDead(ArrayList<Rectangle> obstacles, AnchorPane plane)
    {
        double pigY = pig.getLayoutY() + pig.getY();
        if(collisionDetection(obstacles, pig))
        {
            return true;
        }
        return pigY >= plane.getHeight(); // pig dies if it falls below screen
    }

    // Checks for collision between the pig and any obstacle
    public boolean collisionDetection(ArrayList<Rectangle> obstacles, Rectangle pig)
    {
        for (Rectangle rectangle : obstacles)
        {
            if(rectangle.getBoundsInParent().intersects(pig.getBoundsInParent()))
            {
                return true; // Collision detected
            }
        }
        return false; // No Collision
    }

    public Rectangle getPig() {
        return pig;
    }

    public void setPig(Rectangle pig) {
        this.pig = pig;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
}
