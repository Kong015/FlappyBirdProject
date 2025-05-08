import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ObstacleManager
{
    private AnchorPane plane;
    private double planeHeight;
    private double planeWidth;
    Random random = new Random();

    public ObstacleManager(AnchorPane plane, double planeHeight, double planeWidth)
    {
        this.plane = plane;
        this.planeHeight = planeHeight;
        this.planeWidth = planeWidth;
    }

    // Create the obstacles
    public ArrayList<Rectangle> createObstacles()
    {
        int width = 60; // Width of obstacle
        double xPos = planeWidth; // Location of where it will spawn
        double space = 200; // Space between the tubes
        double recTopHeight = random.nextInt((int)(planeHeight - space - 100)) + 50; // Height of the top tube
        double recBottomHeight = planeHeight - space - recTopHeight; // Height of the bottom tube

        // Create both of the tubes
        Rectangle rectangleTop = new Rectangle(xPos, 0, width, recTopHeight);
        Rectangle rectangleBottom = new Rectangle(xPos, recTopHeight + space, width, recBottomHeight);

        // Set the color of the obstacles to green
        rectangleTop.setFill(Color.GREEN);
        rectangleBottom.setFill(Color.GREEN);

        // Add the tubes to the obstacles arraylist and the game plane
        plane.getChildren().addAll(rectangleTop,rectangleBottom);
        return new ArrayList<>(Arrays.asList(rectangleTop,rectangleBottom));
    }

    // Moves a rectangle horizontally
    public void moveObstacleHorizontally(Rectangle rectangle, double distance)
    {
        rectangle.setX(rectangle.getX() + distance);
    }

    // Moves the obstacles in the arraylist towards the pig and removes those off-screen
    public void moveObstacles(ArrayList<Rectangle> obstacles)
    {
        // Collects the obstacles that go off the screen
        ArrayList<Rectangle> outOfScreen = new ArrayList<>();

        for(Rectangle rectangle : obstacles)
        {
            moveObstacleHorizontally(rectangle, -1); // Move left
            if(rectangle.getX() <= -rectangle.getWidth())
            {
                outOfScreen.add(rectangle); // Mark for removal
            }
        }

        // Removes all the obstacles that are off-screen
        obstacles.removeAll(outOfScreen);
        plane.getChildren().removeAll(outOfScreen);
    }

    public AnchorPane getPlane() {
        return plane;
    }

    public void setPlane(AnchorPane plane) {
        this.plane = plane;
    }

    public double getPlaneHeight() {
        return planeHeight;
    }

    public void setPlaneHeight(double planeHeight) {
        this.planeHeight = planeHeight;
    }

    public double getPlaneWidth() {
        return planeWidth;
    }

    public void setPlaneWidth(double planeWidth) {
        this.planeWidth = planeWidth;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
