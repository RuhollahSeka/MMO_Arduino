import io.ClientHandler;
import model.GameLogic;

public class Main
{
    public static void main(String[] args)
    {
        GameLogic gameLogic = new GameLogic();
        ClientHandler clientHandler = new ClientHandler();
        Coordinator coordinator = new Coordinator(gameLogic, clientHandler);
        coordinator.coordinateGame();
    }
}
