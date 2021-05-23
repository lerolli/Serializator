import WebServer.WebServer;

public class Program {

    public static void main(String[] args){
        var server = new WebServer(8081);
        server.startServer();
    }
}