package WebServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            var clientSocket = new Socket();
            var port = 8081;
            clientSocket.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));

            String messageToServer = "";

            while (!messageToServer.equals("exit")){
                System.out.print("Input a command to server: ");
                var scanner = new Scanner(System.in);
                messageToServer = scanner.nextLine();

                var data = messageToServer.getBytes(StandardCharsets.UTF_8);
                clientSocket.getOutputStream().write(data);

                var builder = new StringBuilder();
                data = new byte[1024];

                var bytes = clientSocket.getInputStream().read(data);
                if (bytes == -1){
                    System.out.println("Достигнут предел потока, какие-то неполадки с сервером");
                }
                else {
                    builder.append(new String(data, StandardCharsets.UTF_8), 0, bytes);
                    System.out.println("Server's answer: " + builder);
                }
            }

            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();

        }
        catch (IOException ignored) {

        }
    }
}
