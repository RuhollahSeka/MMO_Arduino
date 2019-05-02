package model;

import javafx.util.Pair;
import message.ClientMessage;
import message.Direction;
import message.ReceivedMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameLogic
{
    private Map map;
    private HashMap<String, Player> playersMap;
    private Set<MiniGame> miniGames;
    private HashMap<String, Integer> scoreHistory;

    public GameLogic()
    {
        this.map = new Map(100, 100);
        this.playersMap = new HashMap<>();
        this.miniGames = new HashSet<>();
        this.scoreHistory = new HashMap<>();
    }

    public boolean addPlayer(String username)
    {
        if (playersMap.containsKey(username))
        {
            return false;
        }
        int score = 0;
        if (scoreHistory.containsKey(username))
        {
            score = scoreHistory.get(username);
        }

        Cell cell = map.getRandomEmptyCell();
        Player player = new Player(username, cell, score);
        playersMap.put(username, player);
        return true;
    }

    public void performMessages(List<ReceivedMessage> receivedMessages)
    {
        for (ReceivedMessage receivedMessage : receivedMessages)
        {
            String username = receivedMessage.getUsername();
            Direction direction = receivedMessage.getDirection();
            boolean isButtonPushed = receivedMessage.isButtonPushed();
            Player player = playersMap.get(username);
            movePlayer(player, direction);
            player.playMiniGame(isButtonPushed, direction);
        }

        deleteFinishedMiniGames();
        respawnPlayers();
        checkCoins();
//        changeMiniGameTurns();
        startMiniGames();
        playersMap.forEach((s, player) -> scoreHistory.put(s, player.getScore()));
    }

    private void respawnPlayers()
    {
        for (Player player : playersMap.values())
        {
            if (player.getCell() != null || (player.getMiniGame() != null && player.getMiniGame().getWinnerId() == -1))
            {
                continue;
            }

            player.endMiniGame();
            player.move(map.getRandomEmptyCell());
        }
    }

    private void changeMiniGameTurns()
    {
        miniGames.forEach(MiniGame::changeTurn);
    }

    private void deleteFinishedMiniGames()
    {
        Set<MiniGame> finishedMiniGames = new HashSet<>();

        for (MiniGame miniGame : miniGames)
        {
            if (miniGame.getWinnerId() != -1)
            {
                finishedMiniGames.add(miniGame);
            }
        }
        miniGames.removeAll(finishedMiniGames);
    }

    private void startMiniGames()
    {
        Set<Cell> conflictCells = findConflictedCells();
        ArrayList<Pair<Player, Player>> matchedPlayers = matchPlayers(conflictCells);
        matchedPlayers.forEach(aMatch -> {
            MiniGame miniGame = new MiniGame();
            miniGames.add(miniGame);
            aMatch.getKey().setMiniGame(miniGame, 0);
            aMatch.getValue().setMiniGame(miniGame, 1);
        });
    }

    private ArrayList<Pair<Player, Player>> matchPlayers(Set<Cell> conflictCells)
    {
        ArrayList<Pair<Player, Player>> resultPlayers = new ArrayList<>();

        for (Cell cell : conflictCells)
        {
            ArrayList<Pair<Player, Player>> cellMatches = cell.getPlayerMatches();
            resultPlayers.addAll(cellMatches);
        }

        return resultPlayers;
    }

    private Set<Cell> findConflictedCells()
    {
        Set<Cell> resultCells = new HashSet<>();

        for (Player player : playersMap.values())
        {
            Cell cell = player.getCell();
            if (cell == null)
            {
                continue;
            }
            if (cell.getPlayers().size() > 1)
            {
                resultCells.add(cell);
            }
        }

        return resultCells;
    }

    private void checkCoins()
    {
        Set<Cell> coinCells = map.getCoinCells();
        Set<Cell> emptyCells = new HashSet<>();

        for (Cell coinCell : coinCells)
        {
            if (!coinCell.hasPlayers())
            {
                continue;
            }

            for (Player player : coinCell.getPlayers())
            {
                player.receiveCoin();
            }
            coinCell.setHasCoin(false);
            emptyCells.add(coinCell);
        }

        coinCells.removeAll(emptyCells);
    }

    private void movePlayer(Player player, Direction direction)
    {
        if (player.getCell() == null)
        {
            return;
        }
        Cell targetCell = map.getNeighbour(player.getCell(), direction);
        player.move(targetCell);
    }

    public ArrayList<ClientMessage> getClientMessages()
    {
        ArrayList<ClientMessage> clientMessages = new ArrayList<>();
        playersMap.forEach((s, player) -> clientMessages.add(player.getClientMessage()));
        return clientMessages;
    }

    public ArrayList<Pair<String, Integer>> getScores()
    {
        ArrayList<Pair<String, Integer>> scores = new ArrayList<>();
        playersMap.forEach((s, player) -> {
            scores.add(new Pair<>(s, player.getScore()));
//            System.err.println(s + " with score " + player.getScore());
        });
        return scores;
    }

    public void incScore(String username)
    {
        Player player = playersMap.get(username);
        if (player == null)
        {
            return;
        }

        player.incScore();
    }

    public void getCoords(String username)
    {
        Player player = playersMap.get(username);
        if (player == null)
        {
            return;
        }
        Cell cell = player.getCell();
        if (cell == null)
        {
            return;
        }

        System.err.println("User: " + username + "Row: " + cell.getRow() + ", Col: " + cell.getColumn());
    }

    public void forceMovePlayer(String firstUsername, String secondUsername)
    {
        Player firstPlayer = playersMap.get(firstUsername);
        Player secondPlayer = playersMap.get(secondUsername);

        if (secondPlayer == null || firstPlayer == null || firstPlayer.getCell() == null ||
                secondPlayer.getCell() == null)
        {
            return;
        }

        Cell cell = map.getRandomEmptyCell();

        firstPlayer.move(cell);
        secondPlayer.move(cell);
    }

    public void updateScoreHistory(HashMap<String, Integer> scoreHistory)
    {
//        this.scoreHistory.clear();
        this.scoreHistory.putAll(scoreHistory);
    }

    public void removePlayers(ArrayList<String> disconnectedClients)
    {
        disconnectedClients.forEach(disconnectedClient -> playersMap.remove(disconnectedClient));
    }

    public void saveGame() throws IOException
    {
        StringBuilder content = new StringBuilder();
        scoreHistory.forEach((username, score) -> content.append(username).append(" ").append(score).append("\n"));
        Files.write(Paths.get("data.dat"), content.toString().getBytes());
    }

    public void loadGame() throws IOException
    {
        try(Scanner in = new Scanner(new File("data.dat")))
        {
            while (in.hasNext())
            {
                String line = in.nextLine();
                String[] lineParts = line.split(" ");
                scoreHistory.put(lineParts[0], Integer.parseInt(lineParts[1]));
            }
        }
    }
}
