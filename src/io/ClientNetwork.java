package io;

import java.util.concurrent.Semaphore;

public class ClientNetwork
{
    private Semaphore sendingSemaphore;
    private Semaphore receivingSemaphore;

    public ClientNetwork()
    {
        this.sendingSemaphore = new Semaphore(0);
        this.receivingSemaphore = new Semaphore(1);
    }

    public void stopReceiving()
    {
        try
        {
            this.receivingSemaphore.acquire();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void startReceiving()
    {
        this.receivingSemaphore.release();
    }
}
