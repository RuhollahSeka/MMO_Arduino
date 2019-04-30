package model;

import javafx.util.Pair;

import java.util.*;

public class Cell
{
    private int row;
    private int column;
    private boolean isWall;
    private boolean hasCoin;
    private Set<Player> players;
    private List<Cell> neighbours;

    public Cell(int row, int column, boolean isWall)
    {
        this.row = row;
        this.column = column;
        this.isWall = isWall;
        this.hasCoin = false;
        this.players = new HashSet<>();
    }

    public Cell(int row, int column)
    {
        this(row, column, false);
    }

    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    public void removePlayer(Player player)
    {
        this.players.remove(player);
    }

    public boolean hasPlayers()
    {
        return !players.isEmpty();
    }

    public ArrayList<Pair<Player, Player>> getPlayerMatches()
    {
        ArrayList<Pair<Player, Player>> matchedPlayers = new ArrayList<>();
        Iterator<Player> playerIterator = players.iterator();

        for (int i = 0; i < players.size(); i += 2)
        {
            Player firstPlayer = playerIterator.next();
            Player secondPlayer = playerIterator.next();
            Pair<Player, Player> aMatch = new Pair<>(firstPlayer, secondPlayer);
            matchedPlayers.add(aMatch);
        }

        return matchedPlayers;
    }

    public int getPattern()
    {
        if (hasCoin)
            return 3;
        if (isWall)
            return 1;
        if (players.size() != 0)
            return 2;
        return 0;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public boolean isWall()
    {
        return isWall;
    }

    public boolean isHasCoin()
    {
        return hasCoin;
    }

    public void setWall(boolean wall)
    {
        isWall = wall;
    }

    public void setHasCoin(boolean hasCoin)
    {
        this.hasCoin = hasCoin;
    }

    public List<Cell> getNeighbours()
    {
        return neighbours;
    }

    public void setNeighbours(List<Cell> neighbours)
    {
        this.neighbours = neighbours;
    }

    public Set<Player> getPlayers()
    {
        return players;
    }
}
