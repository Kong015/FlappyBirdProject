import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound
{
    // General method to play a sound file from the classpath
    private static void playSound(String soundFileName)
    {
        try
        {
            URL soundURL = Sound.class.getResource(soundFileName);
            if (soundURL == null)
            {
                System.err.println("Sound file not found: " + soundFileName);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    // Sound effect: jump
    public static void playJump()
    {
        playSound("/Sounds/sfx_jump.wav");
    }

    // Sound effect: collision
    public static void playCollision()
    {
        playSound("/Sounds/sfx_hit.wav");
    }

    // Sound effect: score
    public static void playScore()
    {
        playSound("/Sounds/sfx_score.wav");
    }
}