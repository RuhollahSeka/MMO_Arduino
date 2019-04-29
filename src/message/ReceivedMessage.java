package message;

public class ReceivedMessage
{
    private String username;
    private Direction direction;
    private boolean buttonPushed;

    public ReceivedMessage(String username, Direction direction, boolean buttonPushed)
    {
        this.username = username;
        this.direction = direction;
        this.buttonPushed = buttonPushed;
    }

    public String getUsername()
    {
        return username;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public boolean isButtonPushed()
    {
        return buttonPushed;
    }
}
