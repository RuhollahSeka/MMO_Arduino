package model;

import message.Direction;

import java.util.*;

public class Map
{
    private static final int SIDE_WALL_LENGTH = 7;

    private Cell[][] cells;
    private int rowLength;
    private int columnLength;
    private Random random;
    private Position position;

    private Set<Cell> coinCells;
    private Set<Cell> wallCells;


    public Map(int rowLength, int columnLength)
    {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.random = new Random();
        this.position = Position.TOP_LEFT;
        this.coinCells = new HashSet<>();
        this.wallCells = new HashSet<>();
        initCells();
    }

    private void initCells()
    {
        cells = new Cell[rowLength][columnLength];
        setAllEmpty();
        setSideWalls();
        generateRandomWalls();
        generateRandomCoins();
        setAllNeighbours();
    }

    private void setAllEmpty()
    {
        for (int i = 0; i < rowLength; i++)
        {
            for (int j = 0; j < columnLength; j++)
            {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    private void setAllNeighbours()
    {
        for (int i = 0; i < rowLength; i++)
        {
            for (int j = 0; j < columnLength; j++)
            {
                if (cells[i][j].isWall())
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
                chosenCells.forEach(chosenCell -> addCoin(chosenCell.getRow(), chosenCell.getColumn()));
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
                chosenCells.forEach(chosenCell -> addWall(chosenCell.getRow(), chosenCell.getColumn()));
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
            if (cell.isWall() || resultCells.contains(cell) || cell.isHasCoin() || cell.hasPlayers())
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

            for (int j = 0; j < columnLength; j++)
            {
                addWall(i, j);
                addWall(otherRowIndex, j);
                addWall(j, i);
                addWall(j, otherRowIndex);
            }
        }
    }

    private void addWall(int row, int column)
    {
        cells[row][column].setWall(true);
        wallCells.add(cells[row][column]);
    }

    private void addCoin(int row, int column)
    {
        cells[row][column].setHasCoin(true);
        coinCells.add(cells[row][column]);
    }

    public Cell getRandomEmptyCell()
    {
        int row = (position == Position.TOP_LEFT || position == Position.BOTTOM_LEFT) ? 27 : 60;
        int column = (position == Position.TOP_RIGHT || position == Position.BOTTOM_RIGHT) ? 27 : 60;

        Set<Cell> cells = getRandomCells(row, column, 42, 1);
        changePosition();
        return cells.iterator().next();
    }

    private void changePosition()
    {
        switch (position)
        {
            case TOP_LEFT:
                position = Position.TOP_RIGHT;
                break;
            case TOP_RIGHT:
                position = Position.BOTTOM_RIGHT;
                break;
            case BOTTOM_RIGHT:
                position = Position.BOTTOM_LEFT;
                break;
            case BOTTOM_LEFT:
                position = Position.TOP_LEFT;
        }
    }

    public Cell getNeighbour(Cell cell, Direction direction)
    {
        int row = cell.getRow();
        int column = cell.getColumn();
        Cell resultCell = null;

        switch (direction)
        {
            case LEFT:
                resultCell = cells[row][column - 1];
                break;
            case UP:
                resultCell = cells[row - 1][column];
                break;
            case DOWN:
                resultCell = cells[row + 1][column];
                break;
            case RIGHT:
                resultCell = cells[row][column + 1];
                break;
            case NONE:
                break;
        }

        if (resultCell == null || resultCell.isWall())
        {
            return null;
        }
        return resultCell;
    }

    public Set<Cell> getCoinCells()
    {
        return coinCells;
    }

    public Set<Cell> getWallCells()
    {
        return wallCells;
    }

    private enum Position
    {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
