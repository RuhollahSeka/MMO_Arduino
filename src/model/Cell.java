package model;

import java.util.ArrayList;

public class Cell
{
    private int row;
    private int column;
    private CellState state;
    private ArrayList<Cell> neighbours;

    public Cell(int row, int column, boolean isWall)
    {
        this.row = row;
        this.column = column;
        this.state = isWall ? CellState.WALL : CellState.EMPTY;
    }

    public Cell(int row, int column)
    {
        this(row, column, false);
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public CellState getState()
    {
        return state;
    }

    public void setState(CellState state)
    {
        this.state = state;
    }

    public ArrayList<Cell> getNeighbours()
    {
        return neighbours;
    }

    public void setNeighbours(ArrayList<Cell> neighbours)
    {
        this.neighbours = neighbours;
    }
}
