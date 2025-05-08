import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScoreManager
{

    // Loads all scores from file and returns the top 10 in order of highest to lowest
    public List<Integer> loadScores()
    {
        List<Integer> scores = new ArrayList<Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                try
                {
                    scores.add(Integer.parseInt(line));
                }
                catch (NumberFormatException e)
                {
                    System.err.println("Error adding score to list");
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Error reading scores file");
        }

        scores.sort(Collections.reverseOrder()); // Sort file descending order

        scores = sortScores(scores); // Keeps only the top 10 while deleting the rest

        return scores;
    }


    public void addScore(int newScore)
    {
        List <Integer> scores = loadScores(); // Load existing scores
        scores.add(newScore); // Add the new score
        scores.sort(Collections.reverseOrder()); // Sort file descending order

        scores = sortScores(scores); // Keeps only the top 10 while deleting the rest

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt")))
        {
            for (int score : scores)
            {
                writer.write(score + "\n");
            }
        }
        catch (IOException e)
        {
            System.err.println("Error writing scores file");
        }
    }

    // Makes sure that the ArrayList does not have more than 10 numbers
    private List<Integer> sortScores(List<Integer> scores)
    {
        if (scores.size() > 10)
        {
            scores = new ArrayList<>(scores.subList(0, 10));
        }
        return scores;
    }


    // Gets the top score from the file
    public int topScore()
    {
        List <Integer> scores = loadScores();
        scores = sortScores(scores);

        return scores.get(0);
    }

}
