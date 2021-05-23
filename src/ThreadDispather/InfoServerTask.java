package ThreadDispather;

import FileWorker.CommandFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class InfoServerTask extends ThreadedTask {
    private final Socket socket;
    public InfoServerTask(Socket client) {
        socket = client;
    }

    @Override
    public void start() {
        try {
            while (true) {
                StringBuilder builder = new StringBuilder();
                byte[] data = new byte[1024];
                int bytes = socket.getInputStream().read(data);
                builder.append(new String(data, StandardCharsets.UTF_8), 0, bytes);
                String[] command = builder.toString().toLowerCase().split(" ");

                switch (command[0]) {

                    // Делает сериализацию файла
                    case "size":
                        if (command.length > 1) {
                            CommandFactory.createInstance(new SerializatorCommand(command[1]), socket);
                            break;
                        }

                    // Закрывает поток с клиентом
                    case "exit":
                        var m = "Goodbye!";
                        socket.getOutputStream().write(m.getBytes(StandardCharsets.UTF_8));
                        socket.shutdownOutput();
                        socket.shutdownInput();
                        break;
                    default:
                        var error = "Error: Command not found";
                        socket.getOutputStream().write(error.getBytes(StandardCharsets.UTF_8));
                        break;
                }
            }
        } catch (IOException ignored) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {ex.printStackTrace(); }
        }
    }

    @Override
    public String getName() {
        return "InfoServer Task";
    }
}
