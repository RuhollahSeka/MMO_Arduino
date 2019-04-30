package model;

import message.Direction;
import util.Utils;

import java.util.ArrayList;

public class MiniGame
{
    private final static int TABLE_SIZE = 7;

    private int turn;
    private int winnerId;
    private int[] points;
    private int[] columnPointers;
    private ArrayList<ArrayList<Integer>> table;

    public MiniGame()
    {
        this.points = new int[2];
        this.columnPointers = new int[2];
        this.winnerId = -1;
        this.turn = 0;
        initTable();
    }

    private void initTable()
    {
        for (int i = 0; i < 7; i++)
        {
            table.add(new ArrayList<>());
        }
    }

    public void movePointer(int id, Direction direction)
    {
        int currentPos = columnPointers[id];

        switch (direction)
        {
            case RIGHT:
                columnPointers[id] = Math.min(currentPos + 1, TABLE_SIZE - 1);
                break;
            case LEFT:
                columnPointers[id] = Math.max(currentPos - 1, 0);
                break;
        }
    }

    public void putPiece(int id)
    {
        if (id != turn)
        {
            return;
        }

        ArrayList<Integer> column = table.get(columnPointers[id]);
        if (column.size() == 7)
        {
            return;
        }

        column.add(id);
        updateGameState(column.size() - 1, columnPointers[id], id);
    }

    private void updateGameState(int row, int column, int id)
    {

    }

    public void changeTurn()
    {
        turn = 1 - turn;
    }

    public byte[] getBytes(int id)
    {
        byte[] bytes = new byte[16];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = 0;
        }
        int index = (columnPointers[id] > 2) ? 1 : 0;
        bytes[index] = Utils.createByte(bytes[index], 2, index);

        for (int i = 0; i < table.size(); i++)
        {
            ArrayList<Integer> col = table.get(i);

            for (int j = 0; j < col.size(); j++)
            {
                int pattern = col.get(j) == id ? 2 : 1;
                int patternPos = (((-i % 4) + 4) % 4) - 2;
                index = calcIndex(j, i);
                bytes[index] = Utils.createByte(bytes[index], pattern, patternPos);
            }
        }

        return bytes;
    }

    private int calcIndex(int row, int col)
    {
        int colEffect = (col < 3) ? -1 : 0;
        return ((8 - row) * 2) + colEffect;
    }

    public int getWinnerId()
    {
        return winnerId;
    }
}
