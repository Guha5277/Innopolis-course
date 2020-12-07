package task01;

import java.io.*;
import java.net.Socket;

/**
 * Класс реализующий сессию клиент-серверного соединения
 */
public class ClientSession extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private RequestHandler handler;

    /**
     * Конструктор по умолчанию
     * @param socket сокет полученный в результате соединения
     * @throws IOException
     */
    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
        handler = new RequestHandler();
    }

    @Override
    public void run() {
        String header = getFullRequest();
        String request = getSourceRequestPart(header);
        String answer = handler.handleRequest(request);
        sendAnswer(answer);
    }

    private void sendAnswer(String answer) {
        try {
            out.write(answer.getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSourceRequestPart(String request) {
        int from = request.indexOf(' ') + 1;
        int to = request.indexOf(' ', from);
        return request.substring(from, to);
    }

    private String getFullRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String str = reader.readLine();
                if (str == null || str.length() == 0) break;
                sb.append(str).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
