package ui;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Created by msi1 on 4/20/2018.
 */
public class UIHandler
{
    private Scoreboard scoreboard;
    private MapViewer mapViewer;

    public UIHandler(Semaphore uiSemaphore)
    {
        startUI(uiSemaphore);
    }

//    public static void main(String[] args)
//    {
//        new UIHandler().startUI();
//    }

    private void startUI(Semaphore uiSemaphore)
    {
        EventQueue.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth() - 50;
            int height = (int) screenSize.getHeight() - 50;
            scoreboard = new Scoreboard(width, height);
//            mapViewer = new MapViewer(width, height);
            uiSemaphore.release();
//            new Thread(this::addScore).start();
//            mapViewer = new MapViewer();
        });
    }

    public void updateScoreboard(ArrayList<Pair<String, Integer>> scores)
    {
        this.scoreboard.setScores(scores);
    }

    private void addScore()
    {
        ArrayList<Pair<String, Integer>> scores = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        while (true)
        {
            String name = in.next();
            int score = in.nextInt();
            scores.add(new Pair<>(name, score));
            scoreboard.setScores(scores);
        }
    }
}
