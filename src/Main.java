import io.ClientHandler;
import model.GameLogic;
import ui.UIHandler;

import java.util.concurrent.Semaphore;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        Semaphore uiSemaphore = new Semaphore(0);

        GameLogic gameLogic = new GameLogic();
        ClientHandler clientHandler = new ClientHandler();
        UIHandler uiHandler = new UIHandler(uiSemaphore);
        uiSemaphore.acquire();
        Coordinator coordinator = new Coordinator(gameLogic, clientHandler, uiHandler);
        coordinator.coordinateGame();
    }
}
