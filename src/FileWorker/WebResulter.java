package FileWorker;

import ThreadDispather.ThreadDispatcher;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class WebResulter implements IResulter {

    Socket socket;
    public WebResulter(Socket s) {
        socket = s;
    }

    @Override
    public void showResult(ArrayList<String> result) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n");

        for (var str : result) {
            stringBuilder.append(str);
            stringBuilder.append("\r\n");
        }

        try {
            socket.getOutputStream().write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            ThreadDispatcher.getInstance().threadMonitor.update();
        } catch (IOException ignored) {
        }
    }
}
