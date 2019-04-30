package io;

import message.ClientMessage;
import message.ReceivedMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler
{
    private Map<String, ClientNetwork> clientNetworks;

    public ClientHandler()
    {
        this.clientNetworks = new HashMap<>();
    }

    public void stopReceivingAll()
    {
        clientNetworks.forEach((s, clientNetwork) -> clientNetwork.stopReceiving());
    }

    public void startReceivingAll()
    {
        clientNetworks.forEach((s, clientNetwork) -> clientNetwork.startReceiving());
    }

    public void sendMessages(ArrayList<ClientMessage> clientMessages)
    {
        clientMessages.forEach(clientMessage ->
                clientNetworks.get(clientMessage.getUsername()).send(clientMessage.getMessage()));
    }

    public void addClient(String username, int port)
    {
        ClientNetwork client = new ClientNetwork(username, port);
        clientNetworks.put(username, client);
    }

    public List<ReceivedMessage> getReceivedMessages()
    {
        List<ReceivedMessage> receivedMessages = new ArrayList<>();
        clientNetworks.forEach((s, clientNetwork) -> receivedMessages.add(clientNetwork.getReceivedMessage()));
        return receivedMessages;
    }

    public void close()
    {
        clientNetworks.forEach((s, clientNetwork) -> clientNetwork.close());
    }
}
