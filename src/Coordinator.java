import io.ClientHandler;
import model.GameLogic;

import java.util.Scanner;

public class Coordinator
{
    private GameLogic gameLogic;
    private ClientHandler clientHandler;


    public Coordinator(GameLogic gameLogic, ClientHandler clientHandler) // TODO get UI
    {
        this.gameLogic = gameLogic;
        this.clientHandler = clientHandler;
    }

    public void coordinateGame()
    {
        /*
        init ui
         */
        Scanner in = new Scanner(System.in);
        clientHandler.addClient("lol", 6666);

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
            clientHandler.sendMessages(gameLogic.getClientMessages());
            clientHandler.startReceivingAll();
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            clientHandler.stopReceivingAll();
            gameLogic.performMessages(clientHandler.getReceivedMessages());

            String inp = in.nextLine();
            if (inp.equals("end"))
            {
                clientHandler.close();
            }
        }
    }
}
