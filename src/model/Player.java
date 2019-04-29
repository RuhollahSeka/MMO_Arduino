package model;

import message.Direction;

public class Player
{
    private String username;
    private Cell cell;
    private int score;

    private MiniGame miniGame;

    public Player(String username, Cell cell)
    {
        this.username = username;
        this.cell = cell;
        this.cell.addPlayer(this);
        this.score = 0;
    }

    public void playMiniGame(boolean isButtonPushed, Direction direction)
    {
        if (miniGame == null)
        {
            return;
        }
        // TODO
    }

    public Cell getCell()
    {
        return cell;
    }

    public void move(Cell targetCell)
    {
        if (miniGame != null || targetCell == null)
        {
            return;
        }

        this.cell.removePlayer(this);
        targetCell.addPlayer(this);
        this.cell = targetCell;
    }

    public void receiveCoin()
    {
        this.score++;
    }
}
