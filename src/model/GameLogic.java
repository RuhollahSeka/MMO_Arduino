package model;

import message.Direction;
import message.ReceivedMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameLogic
{
    private Map map;
    private HashMap<String, Player> playersMap;
    private HashMap<String, MiniGame> miniGames;

    public GameLogic()
    {
        this.map = new Map(100, 100);
        this.playersMap = new HashMap<>();
        this.miniGames = new HashMap<>();
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
        startMiniGames();
    }

    private void startMiniGames()
    {
        // TODO
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
}
