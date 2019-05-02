package io;

import message.ReceivedMessage;
import util.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    private boolean isConnected = true;

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
                serverSocket.setSoTimeout(10000);
                this.closeables.add(serverSocket);
                Socket socket = serverSocket.accept();
                socket.setReceiveBufferSize(2);
                socket.setSoTimeout(200);
                this.closeables.add(socket);
                in = socket.getInputStream();
                out = socket.getOutputStream();
                closeables.add(in);
                closeables.add(out);
                System.err.println("socket created");
            } catch (IOException e)
            {
                System.err.println("Socket failed for " + username);
                this.isConnected = false;
//                e.printStackTrace();
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
        if (receivingThread != null && receivingThread.isAlive())
        {
            receivingThread.interrupt();
        }
    }

    public void startReceiving()
    {
//        this.receivingSemaphore.release();
        if (in == null)
        {
            return;
        }
        receivingThread = new Thread(this::receive);
        receivingThread.start();
    }

    public void receive()
    {
        try
        {
//            receivingSemaphore.acquire();
            byte[] bytes = new byte[2];
            in.read(bytes);
            int dir = bytes[0];
            boolean pushed = bytes[1] == 1;
            Thread.sleep(10);
//            System.err.println("Received, dir: " + dir + ", pushed: " + pushed);
            this.receivedMessage = new ReceivedMessage(username, Utils.getDirection(dir), pushed);
//            receivingSemaphore.release();
        } catch (IOException | InterruptedException e)
        {
            if (e instanceof SocketTimeoutException)
            {
//                System.err.println("Socket timeout for " + username);
            } else
            {
                this.isConnected = false;
                System.err.println("Failed to receive from " + username);
//                e.printStackTrace();
            }
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
//                System.err.println("Message: " + message);
                out.write(message);
            } catch (IOException e)
            {
                this.isConnected = false;
                System.err.println("Failed to send to " + username);
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

    public boolean isConnected()
    {
        return isConnected;
    }
}
