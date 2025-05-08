import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScoreManager
{
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

        scores.sort(Collections.reverseOrder());

        scores = sortScores(scores);

        return scores;
    }


    public void addScore(int newScore)
    {
        List <Integer> scores = loadScores();
        scores.add(newScore);
        scores.sort(Collections.reverseOrder());

        scores = sortScores(scores);

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

    private List<Integer> sortScores(List<Integer> scores)
    {
        if (scores.size() > 10)
        {
            scores = new ArrayList<>(scores.subList(0, 10));
        }
        return scores;
    }

    public int topScore()
    {
        List <Integer> scores = loadScores();
        scores = sortScores(scores);

        return scores.get(0);
    }

}
