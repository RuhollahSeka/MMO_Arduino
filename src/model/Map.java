package model;

import java.util.*;

public class Map
{
    private static final int SIDE_WALL_LENGTH = 7;

    private Cell[][] cells;
    private int rowLength;
    private int columnLength;
    private Random random;

    public Map(int rowLength, int columnLength)
    {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.random = new Random();
        initCells();
    }

    private void initCells()
    {
        setSideWalls();
        generateRandomWalls();
        generateRandomCoins();
        setAllNeighbours();
    }

    private void setAllNeighbours()
    {
        for (int i = 0; i < rowLength; i++)
        {
            for (int j = 0; j < columnLength; j++)
            {
                if (cells[i][j].getState() == CellState.WALL)
                {
                    continue;
                }

                setNeighbours(cells[i][j]);
            }
        }
    }

    private void setNeighbours(Cell cell)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();
        int topLeftRow = cell.getRow() - 3;
        int topLeftColumn = cell.getColumn() - 3;

        for (int i = topLeftRow; i < topLeftRow + 7; i++)
        {
            neighbours.addAll(Arrays.asList(cells[i]).subList(topLeftColumn, topLeftColumn + 7));
        }

        cell.setNeighbours(neighbours);
    }

    private void generateRandomCoins()
    {
        for (int i = SIDE_WALL_LENGTH + 5; i < rowLength - SIDE_WALL_LENGTH; i += 11)
        {
            for (int j = SIDE_WALL_LENGTH + 5; j < columnLength - SIDE_WALL_LENGTH; j += 11)
            {
                Set<Cell> chosenCells = getRandomCells(i, j, 11, 3);
                chosenCells.forEach(chosenCell -> chosenCell.setState(CellState.COIN));
            }
        }
    }

    private void generateRandomWalls()
    {
        for (int i = SIDE_WALL_LENGTH + 1; i < rowLength - SIDE_WALL_LENGTH; i += 3)
        {
            for (int j = SIDE_WALL_LENGTH + 1; j < columnLength - SIDE_WALL_LENGTH; j += 3)
            {
                Set<Cell> chosenCells = getRandomCells(i, j, 3, 2);
                chosenCells.forEach(chosenCell -> chosenCell.setState(CellState.WALL));
            }
        }
    }

    private Set<Cell> getRandomCells(int row, int column, int squareSize, int quantity)
    {
        Set<Cell> resultCells = new HashSet<>();
        int topLeftRow = row - squareSize / 2;
        int topLeftColumn = column - squareSize / 2;

        while (resultCells.size() < quantity)
        {
            int randomRow = random.nextInt(squareSize);
            int randomColumn = random.nextInt(squareSize);
            Cell cell = cells[topLeftRow + randomRow][topLeftColumn + randomColumn];
            if (cell.getState() != CellState.EMPTY || resultCells.contains(cell))
            {
                continue;
            }
            resultCells.add(cell);
        }

        return resultCells;
    }

    private void setSideWalls()
    {
        for (int i = 0; i < SIDE_WALL_LENGTH; i++)
        {
            int otherRowIndex = rowLength - i - 1;

            for (int j = 0; j < SIDE_WALL_LENGTH; j++)
            {
                int otherColumnIndex = columnLength - j - 1;

                cells[i][j] = new Cell(i, j, true);
                cells[i][otherColumnIndex] = new Cell(i, otherColumnIndex, true);
                cells[otherRowIndex][j] = new Cell(otherRowIndex, j, true);
                cells[otherColumnIndex][otherColumnIndex] = new Cell(otherRowIndex, otherColumnIndex, true);
            }
        }
    }
}
