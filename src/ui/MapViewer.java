package ui;

import javafx.util.Pair;

import java.util.ArrayList;

public class MapViewer extends MyFrame
{
    private static final int MAP_SIZE = 100;

    private ArrayList<Pair<Integer, Integer>> wallPositions;
    private ArrayList<Pair<Integer, Integer>> coinPositions;
    private ArrayList<Pair<Integer, Integer>> playerPositions;

    public MapViewer(int width, int height)
    {
        super(width, height);
        this.wallPositions = new ArrayList<>();
        this.coinPositions = new ArrayList<>();
        this.playerPositions = new ArrayList<>();
    }


}
