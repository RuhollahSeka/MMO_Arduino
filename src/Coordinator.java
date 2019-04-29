import io.ClientHandler;
import model.GameLogic;

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
        }
    }
}
