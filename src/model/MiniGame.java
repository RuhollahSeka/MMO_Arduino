package model;

import javafx.util.Pair;
import message.Direction;
import util.Utils;

import java.util.ArrayList;
import java.util.Random;

public class MiniGame
{
    private final static int TABLE_SIZE = 7;

    private int turn;
    private int winnerId;
    private int[] points;
    private int[] columnPointers;
    private ArrayList<ArrayList<Integer>> table;
    private Random random;

    public MiniGame()
    {
        this.points = new int[2];
        this.columnPointers = new int[2];
        this.winnerId = -1;
        this.turn = 0;
        this.table = new ArrayList<>();
        this.random = new Random();
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

    public boolean putPiece(int id)
    {
        if (id != turn)
        {
            return false;
        }

        ArrayList<Integer> column = table.get(columnPointers[id]);
        if (column.size() == 7)
        {
            return false;
        }
        column.add(id);
        turn = 1 - turn;
        if (isGameFinished(column.size() - 1, columnPointers[id]))
        {
            winnerId = id;
            return true;
        }
        if (!roomLeft())
        {
            winnerId = -2;
        }

        return true;
    }

    public void putRandomPiece(int id)
    {
        ArrayList<Integer> col = getRandomEmptyCol();
        col.add(id);
        turn = 1 - turn;
        if (isGameFinished(col.size() - 1, columnPointers[id]))
        {
            winnerId = id;
            return;
        }
        if (!roomLeft())
        {
            winnerId = -2;
        }
    }

    private ArrayList<Integer> getRandomEmptyCol()
    {
        while (true)
        {
            ArrayList<Integer> col = table.get(random.nextInt(7));
            if (col.size() < 7)
            {
                return col;
            }
        }
    }

    private boolean roomLeft()
    {
        for (ArrayList<Integer> list : table)
        {
            if (list.size() != 7)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isGameFinished(int row, int column)
    {
        // Horizontal
        for (int i = 0; i < TABLE_SIZE - 3; i++)
        {
            if (table.get(i).size() <= row || table.get(i + 1).size() <= row || table.get(i + 2).size() <= row ||
                    table.get(i + 3).size() <= row)
            {
                continue;
            }

            int first = table.get(i).get(row);
            int second = table.get(i + 1).get(row);
            int third = table.get(i + 2).get(row);
            int forth = table.get(i + 3).get(row);
            if (first == second && second == third && third == forth)
            {
                return true;
            }
        }

        // Vertical
        for (int i = 0; i < TABLE_SIZE - 3; i++)
        {
            if (table.get(column).size() <= i || table.get(column).size() <= i + 1 || table.get(column).size() <= i + 2
                    || table.get(column).size() <= i + 3)
            {
                continue;
            }

            int first = table.get(column).get(i);
            int second = table.get(column).get(i + 1);
            int third = table.get(column).get(i + 2);
            int forth = table.get(column).get(i + 3);
            if (first == second && second == third && third == forth)
            {
                return true;
            }
        }

        Pair<Integer, Integer> startingPoint = new Pair<>(row - Math.min(row, column), column - Math.min(row, column));
        // Diagonal 1
        for (int i = 0; i < TABLE_SIZE - 3; i++)
        {
            int startingRow = startingPoint.getKey() + i;
            int startingCol = startingPoint.getValue() + i;
            if (startingRow + 3 >= TABLE_SIZE || startingCol + 3 >= TABLE_SIZE)
            {
                break;
            }

            if (table.get(startingCol).size() <= startingRow || table.get(startingCol + 1).size() <= startingRow + 1
                    || table.get(startingCol + 2).size() <= startingRow + 2
                    || table.get(startingCol + 3).size() <= startingRow + 3)
            {
                continue;
            }
            int first = table.get(startingCol).get(startingRow);
            int second = table.get(startingCol + 1).get(startingRow + 1);
            int third = table.get(startingCol + 2).get(startingRow + 2);
            int forth = table.get(startingCol + 3).get(startingRow + 3);
            if (first == second && second == third && third == forth)
            {
                return true;
            }
        }

        // Diagonal 2
        for (int i = 0; i < TABLE_SIZE - 3; i++)
        {
            int startingRow = startingPoint.getKey() + i;
            int startingCol = startingPoint.getValue() - i;
            if (startingRow + 3 >= TABLE_SIZE || startingCol - 3 < 0)
            {
                break;
            }

            if (table.get(startingCol).size() <= startingRow || table.get(startingCol - 1).size() <= startingRow + 1
                    || table.get(startingCol - 2).size() <= startingRow + 2
                    || table.get(startingCol - 3).size() <= startingRow + 3)
            {
                continue;
            }
            int first = table.get(startingCol).get(startingRow);
            int second = table.get(startingCol - 1).get(startingRow + 1);
            int third = table.get(startingCol - 2).get(startingRow + 2);
            int forth = table.get(startingCol - 3).get(startingRow + 3);
            if (first == second && second == third && third == forth)
            {
                return true;
            }
        }

        return false;
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
        int index = (columnPointers[turn] > 2) ? 1 : 0;
        int colorPattern = (id == turn) ? 2 : 1;
        bytes[index] = Utils.createByte(bytes[index], colorPattern, (((-(columnPointers[turn] - 2) % 4) + 4) % 4));

        for (int i = 0; i < table.size(); i++)
        {
            ArrayList<Integer> col = table.get(i);

            for (int j = 0; j < col.size(); j++)
            {
                int pattern = col.get(j) == id ? 2 : 1;
                int patternPos = (((-(i - 2) % 4) + 4) % 4);
                index = calcIndex(j, i);
                bytes[index] = Utils.createByte(bytes[index], pattern, patternPos);
            }
        }

        return bytes;
    }

    private int calcIndex(int row, int col)
    {
        int colEffect = (col < 3) ? -1 : 0;
        return ((8 - row) * 2) + colEffect - 1;
    }

    public int getWinnerId()
    {
        return winnerId;
    }
}
