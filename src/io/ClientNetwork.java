package io;

import message.ReceivedMessage;
import util.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ClientNetwork
{
    private Semaphore sendingSemaphore;
    private Semaphore receivingSemaphore;
    private InputStream in;
    private OutputStream out;
    private String username;

    private ArrayList<Closeable> closeables;
    private ReceivedMessage receivedMessage;

    private Thread receivingThread;

    public ClientNetwork(String username, int port)
    {
        this.sendingSemaphore = new Semaphore(0);
        this.receivingSemaphore = new Semaphore(0);
        this.username = username;
        this.closeables = new ArrayList<>();
        createSocket(port);
    }

    private void createSocket(int port)
    {
        new Thread(() -> {
            try
            {
                ServerSocket serverSocket = new ServerSocket(port);
                this.closeables.add(serverSocket);
                Socket socket = serverSocket.accept();
                this.closeables.add(socket);
                in = socket.getInputStream();
                out = socket.getOutputStream();
                closeables.add(in);
                closeables.add(out);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopReceiving()
    {
//        try
//        {
//            this.receivingSemaphore.acquire();
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
        if (receivingThread.isAlive())
        {
            receivingThread.interrupt();
        }
    }

    public void startReceiving()
    {
//        this.receivingSemaphore.release();
        receivingThread = new Thread(this::receive);
        receivingThread.start();
    }

    public void receive()
    {
        try
        {
//            receivingSemaphore.acquire();
            int dir = in.read();
            boolean pushed = in.read() == 1;
            Thread.sleep(10);
            this.receivedMessage = new ReceivedMessage(username, Utils.getDirection(dir), pushed);
//            receivingSemaphore.release();
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void send(byte[] message)
    {
        if (out == null)
        {
            return;
        }

        new Thread(() -> {
            try
            {
                out.write(message);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public ReceivedMessage getReceivedMessage()
    {
        return receivedMessage;
    }

    public void close()
    {
        closeables.forEach(closeable -> {
            try
            {
                closeable.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });

    }
}
