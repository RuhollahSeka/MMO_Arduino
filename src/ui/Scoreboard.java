package ui;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Scoreboard extends MyFrame
{
    private static int LABEL_HEIGHT = 55;

    private ArrayList<Pair<String, Integer>> scores;
    private ArrayList<JLabel> playerLabels;
    private ArrayList<JLabel> rankLabels;
    private ArrayList<JLabel> scoreLabels;
    private MyPanel panel;

    public Scoreboard(int width, int height)
    {
        super(width, height);
        this.scores = new ArrayList<>();
        this.playerLabels = new ArrayList<>();
        this.rankLabels = new ArrayList<>();
        this.scoreLabels = new ArrayList<>();
        this.panel = new MyPanel(getWidth(), getHeight(), null, null);
        setPanel(panel);
    }

    public void setScores(ArrayList<Pair<String, Integer>> scores)
    {
        clearLabels();
        this.scores.clear();
        this.scores.addAll(scores);
        addNewLabels();
        this.scores.sort((p1, p2) -> p2.getValue() - p1.getValue());
        setLabelTexts();
        panel.repaint();
    }

    private void clearLabels()
    {
        for (int i = 0; i < rankLabels.size(); i++)
        {
            rankLabels.get(i).setText("");
            playerLabels.get(i).setText("");
            scoreLabels.get(i).setText("");
        }
    }

    private void setLabelTexts()
    {
        for (int i = 0; i < scores.size(); i++)
        {
            Pair<String, Integer> score = scores.get(i);
            JLabel playerLabel = playerLabels.get(i);
            JLabel scoreLabel = scoreLabels.get(i);
            JLabel rankLabel = rankLabels.get(i);
            playerLabel.setText(score.getKey());
            scoreLabel.setText(String.valueOf(score.getValue()));
            rankLabel.setText(String.valueOf(i + 1));
//            System.err.println("X: " + playerLabel.getX() + ", Y: " + playerLabel.getY() +
//                    ", W: " + playerLabel.getWidth() + ", H: " + playerLabel.getHeight());
        }
    }

    private void addNewLabels()
    {
        int newLabelsCount = scores.size() - playerLabels.size();
        int offset = playerLabels.size();
        int width = getWidth();

        for (int i = 0; i < newLabelsCount; i++)
        {
            JLabel rankLabel = new JLabel((offset + i + 1) + ".");
            rankLabel.setBounds((width / 3) - 50, (offset + i) * LABEL_HEIGHT, 50, LABEL_HEIGHT);
            rankLabel.setFont(new Font(rankLabel.getFont().getName(), Font.BOLD, 20));
//            rankLabel.setOpaque(true);
//            rankLabel.setBackground(Color.orange);
            rankLabels.add(rankLabel);
            panel.add(rankLabel);

            JLabel playerLabel = new JLabel();
            playerLabel.setBounds(width / 3, (offset + i) * LABEL_HEIGHT, width / 3, LABEL_HEIGHT);
            playerLabel.setFont(new Font(playerLabel.getFont().getName(), Font.BOLD, 20));
            playerLabels.add(playerLabel);
            panel.add(playerLabel);

            JLabel scoreLabel = new JLabel();
            scoreLabel.setBounds((width * 2) / 3, (offset + i) * LABEL_HEIGHT, 50, LABEL_HEIGHT);
            scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
            scoreLabels.add(scoreLabel);
            panel.add(scoreLabel);
        }
    }
}
