package io;

import java.util.ArrayList;

public class ClientHandler
{
    private ArrayList<ClientNetwork> clientNetworks;

    public ClientHandler()
    {
        this.clientNetworks = new ArrayList<>();
    }

    public void stopReceivingAll()
    {
        clientNetworks.forEach(ClientNetwork::stopReceiving);
    }
}
