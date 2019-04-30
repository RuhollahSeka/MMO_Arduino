package message;

public class ClientMessage
{
    private String username;
    private byte[] message;

    public ClientMessage(String username)
    {
        this.username = username;
    }

    public void setMessageBytes(byte[] message)
    {
        this.message = message;
    }

    public byte[] getMessage()
    {
        return message;
    }

    public String getUsername()
    {
        return username;
    }
}
