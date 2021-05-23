package WebServer;

import ThreadDispather.InfoServerTask;
import ThreadDispather.ThreadDispatcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer implements Runnable {
    private final int port;
    private ServerSocket serverSocket;
    public boolean isStopped;

    public WebServer(int port){
        this.port = port;
        isStopped = false;
    }

    public void createServerSocket(){
        try {
            serverSocket = new ServerSocket(port,50, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        createServerSocket();
        run();
    }

    @Override
    public void run() {
        var threadDispatcher = ThreadDispatcher.getInstance();
        threadDispatcher.setMaxPoolSize(1);
        Socket client;

        while (!isStopped) {
            try {
                client = serverSocket.accept();
                threadDispatcher.add(new InfoServerTask(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
