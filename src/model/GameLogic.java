package model;

import javafx.util.Pair;
import message.ClientMessage;
import message.Direction;
import message.ReceivedMessage;

import java.util.*;

public class GameLogic
{
    private Map map;
    private HashMap<String, Player> playersMap;
    private Set<MiniGame> miniGames;

    public GameLogic()
    {
        this.map = new Map(100, 100);
        this.playersMap = new HashMap<>();
        this.miniGames = new HashSet<>();
    }

    public boolean addPlayer(String username)
    {
        if (playersMap.containsKey(username))
        {
            return false;
        }

        Cell cell = map.getRandomEmptyCell();
        Player player = new Player(username, cell);
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

        checkCoins();
        deleteFinishedMiniGames(); // TODO respawn players
        changeMiniGameTurns();
        startMiniGames();
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
        Cell targetCell = map.getNeighbour(player.getCell(), direction);
        player.move(targetCell);
    }

    public ArrayList<ClientMessage> getClientMessages()
    {
        ArrayList<ClientMessage> clientMessages = new ArrayList<>();
        playersMap.forEach((s, player) -> clientMessages.add(player.getClientMessage()));
        return clientMessages;
    }
}
