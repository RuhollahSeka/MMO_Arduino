package model;

import javafx.util.Pair;
import message.ClientMessage;
import message.Direction;
import util.Utils;

import java.util.List;

public class Player
{
    private String username;
    private Cell cell;
    private int score;

    private Pair<MiniGame, Integer> miniGameData;

    public Player(String username, Cell cell)
    {
        this.username = username;
        this.cell = cell;
        this.cell.addPlayer(this);
        this.score = 0;
    }

    public void playMiniGame(boolean isButtonPushed, Direction direction)
    {
        if (miniGameData == null)
        {
            return;
        }
        MiniGame miniGame = miniGameData.getKey();
        int id = miniGameData.getValue();

        if (!isButtonPushed)
        {
            miniGame.movePointer(id, direction);
            return;
        }
        int winnerId = miniGame.getWinnerId();
        miniGame.putPiece(id);
        if (winnerId == -1)
        {
            return;
        }

        miniGameData = null;
        if (winnerId == -2)
        {
            receiveCoin(1);
            return;
        }
        receiveCoin(5);
    }

    public void move(Cell targetCell)
    {
        if (miniGameData != null || targetCell == null)
        {
            return;
        }

        this.cell.removePlayer(this);
        targetCell.addPlayer(this);
        this.cell = targetCell;
    }

    public void receiveCoin()
    {
        receiveCoin(1);
    }

    public void receiveCoin(int num)
    {
        this.score += num;
    }

    public void setMiniGame(MiniGame miniGame, int id)
    {
        this.miniGameData = new Pair<>(miniGame, id);
        this.cell.removePlayer(this);
        this.cell = null;
    }

    public ClientMessage getClientMessage()
    {
        ClientMessage clientMessage = new ClientMessage(username);

        if (miniGameData != null)
        {
            clientMessage.setMessageBytes(miniGameData.getKey().getBytes(miniGameData.getValue()));
        } else
        {
            clientMessage.setMessageBytes(getMapBytes());
        }

        return clientMessage;
    }

    private byte[] getMapBytes()
    {
        List<Cell> neighbours = cell.getNeighbours();
        int byteIndex = 2;
        int cellIndex = 0;
        byte[] bytes = new byte[16];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = 0;
        }

        while (cellIndex < neighbours.size())
        {
            int forLen = (cellIndex % 7 == 0)? 3 : 4;

            for (int i = 0; i < forLen; i++)
            {
                Cell cell = neighbours.get(cellIndex + i);
                bytes[byteIndex] = Utils.createByte(bytes[byteIndex], cell.getPattern(), forLen - i - 1);
            }
            byteIndex++;
            cellIndex += forLen;
        }

        return bytes;
    }

    public Cell getCell()
    {
        return cell;
    }

    public int getScore()
    {
        return score;
    }
}
