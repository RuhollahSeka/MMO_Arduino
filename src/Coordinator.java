import io.ClientHandler;
import model.GameLogic;
import ui.UIHandler;

import java.util.Scanner;

public class Coordinator
{
    private GameLogic gameLogic;
    private ClientHandler clientHandler;
    private UIHandler uiHandler;
    private boolean gameFinished = false;


    public Coordinator(GameLogic gameLogic, ClientHandler clientHandler, UIHandler uiHandler) // TODO get UI
    {
        this.gameLogic = gameLogic;
        this.clientHandler = clientHandler;
        this.uiHandler = uiHandler;
    }

    public void coordinateGame()
    {
        /*
        init ui
         */


        new Thread(this::listenCommands).start();
        while (true)
        {
            /*
            send to ui
            send to client
            start receiving
            wait
            stop receiving
            receive
            simulate
            end if finished
             */
//            System.err.println("Attempting to send");
            uiHandler.updateScoreboard(gameLogic.getScores());
            clientHandler.sendMessages(gameLogic.getClientMessages());
            clientHandler.startReceivingAll();
            try
            {
                Thread.sleep(300);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            clientHandler.stopReceivingAll();
            gameLogic.removePlayers(clientHandler.getDisconnectedClients());
            gameLogic.performMessages(clientHandler.getReceivedMessages());

            if (gameFinished)
            {
                clientHandler.close();
                System.exit(0);
            }
        }
    }

    private void listenCommands()
    {
        Scanner in = new Scanner(System.in);
        while (true)
        {
            try
            {
                String input = in.nextLine();
                String[] inputParts = input.split(" ");
                if (inputParts[0].equals("add"))
                {
                    int port = Integer.parseInt(inputParts[2]);
                    clientHandler.addClient(inputParts[1], port);
//                    System.err.println("attempted to add client");
                    gameLogic.addPlayer(inputParts[1]);
//                    System.err.println("attempted to add player");
                } else if (inputParts[0].equals("end"))
                {
                    gameFinished = true;
                } else if (inputParts[0].equals("score"))
                {
                    gameLogic.incScore(inputParts[1]);
                } else if (inputParts[0].equals("coords"))
                {
                    gameLogic.getCoords(inputParts[1]);
                } else if (inputParts[0].equals("force"))
                {
                    gameLogic.forceMovePlayer(inputParts[1], inputParts[2]);
                } else if (inputParts[0].equals("save"))
                {
                    gameLogic.saveGame();
                } else if (inputParts[0].equals("load"))
                {
                    gameLogic.loadGame();
                }
            } catch (Exception ignored)
            {

            }
        }
    }
}
