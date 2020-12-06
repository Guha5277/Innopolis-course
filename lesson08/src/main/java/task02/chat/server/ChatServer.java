package task02.chat.server;

import task02.chat.common.ChatLibrary;
import task02.chat.common.Message;
import task02.chat.common.json.JsonParser;
import task02.network.ServerSocketThread;
import task02.network.ServerSocketThreadListener;
import task02.network.SocketThread;
import task02.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private final int PORT = 5555;
    private final int TIMEOUT = 100;
    private final String SERVER_NAME = "My console chat";
    private final String SERVER_INFO = "serv_info";
    private final String ONLINE_USERS = "serv_online";

    private Long serverStartTime;

    private AtomicInteger ghostsIndex = new AtomicInteger();
    private ServerSocketThread server;
    private CopyOnWriteArrayList<ClientThread> clients = new CopyOnWriteArrayList<>();

    /**
     * Метод запуска сервера
     */
    public void start() {
        serverStartTime = System.currentTimeMillis();
        server = new ServerSocketThread(this, SERVER_NAME, PORT, TIMEOUT);
    }

    /**
     * Метод остановки сервера
     */
    public void stop() {
        server.interrupt();
    }

    //Server events
    @Override
    public void onThreadStart(ServerSocketThread thread) {
        showMsg("Server thread start");
    }

    @Override
    public void onServerStart(ServerSocketThread thread, ServerSocket server) {
        showMsg("Server started at port: " + PORT);
    }

    @Override
    public void onSocketAccepted(ServerSocket server, Socket socket) {
        showMsg("Client connected (" + socket.getInetAddress() + ")");
        new ClientThread(this, "client", socket, ghostsIndex.incrementAndGet());
    }

    @Override
    public void onServerException(ServerSocketThread thread, Exception e) {
        showMsg("Exception: " + e.getMessage());
    }

    @Override
    public void onThreadStop(ServerSocketThread thread) {
        showMsg("Server was stopped");
    }

    //Clients events
    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
        ClientThread client = (ClientThread) thread;
        showMsg("ClientThread start (" + client.getNickname() + ")");
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {
        ClientThread client = (ClientThread) thread;
        showMsg("Client disconnected (" + client.getNickname() + ")");
        if (client.isAuthorized()) {
            sendToAllAuthorizedClients(new Message(ChatLibrary.SERVER_NICKNAME, client.getNickname() + " отключился от сервера", Message.MESSAGE));
            clients.remove(client);
        }
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMsg((ClientThread) thread, socket, msg);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        ClientThread client = (ClientThread) thread;
        showMsg("ClientThread ready (" + client.getNickname() + ")");
        clients.add(client);
        client.authRequest();
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception e) {
        showMsg("ClientThread exception: " + e.getMessage());
    }

    private void handleMsg(ClientThread client, Socket socket, String msg) {
        Message message = JsonParser.clientMessageFromJson(msg);
        showMsg("Received msg: " + msg);
        switch (message.getMessageType()) {
            case Message.AUTH_REQUEST:
                handleAuthRequest(client, message);
                break;
            case Message.MESSAGE:
                if (client.isAuthorized()) {
                    message.setAuthor(client.getNickname());
                    sendToAllAuthorizedClients(message);
                } else {
                    client.authRequest();
                }
                break;
            case Message.INFO:
                handleInfoMsg(client, message);
                break;
            default:
                client.sendMessage(new Message("Message format error!" + msg, Message.ERROR));
        }
    }

    private void handleInfoMsg(ClientThread client, Message message) {
        Message answer;
        String msg;

        if (!client.isAuthorized()) {
            answer = new Message("To get server info authorize firs!", Message.ERROR);
            client.sendMessage(answer);
        } else {
            switch (message.getMessage()) {
                case SERVER_INFO:
                    msg = getServerUptime();
                    answer = new Message(ChatLibrary.SERVER_NICKNAME, msg, Message.INFO);
                    break;
                case ONLINE_USERS:
                    msg = getOnlineUsersList();
                    answer = new Message(ChatLibrary.SERVER_NICKNAME, msg, Message.INFO);
                    break;
                default:
                    answer = new Message("Wrong SERVER_INFO command!", Message.ERROR);
            }
            client.sendMessage(answer);
        }
    }


    private String getServerUptime() {
        int dayMs = 86_400_000;
        int hourMs = 3_600_000;
        int minuteMs = 60_000;
        int secMs = 1000;
        long count;

        StringBuilder sb = new StringBuilder();
        sb.append("Server uptime: ");

        long uptime = System.currentTimeMillis() - serverStartTime;

        count = uptime / dayMs;
        if (count >= 1) {
            sb.append(count).append(" days, ");
        }

        count = uptime / hourMs;
        if (count >= 1) {
            sb.append(count % 24).append(" hours, ");
        }

        count = uptime / minuteMs;
        if (count >= 1) {
            sb.append(count % 60).append(" minutes, ");
        }

        count = uptime / secMs;
        if (count >= 1) {
            sb.append(count % 60).append(" seconds.");
        }

        return sb.toString();
    }

    private String getOnlineUsersList() {
        AtomicInteger i = new AtomicInteger(1);
        return "\n-----Online users-----\n" + clients.stream()
                .map(c -> " " + i.getAndIncrement() + ". " + c.getNickname() + " \n")
                .reduce((c1, c2) -> c1 + c2)
                .orElse("No one is online now!");
    }

    private void sendToAllAuthorizedClients(Message message) {
        clients.stream()
                .filter(ClientThread::isAuthorized)
                .forEach(c -> c.sendMessage(message));
    }


    private void showMsg(String msg) {
        System.out.println(msg);
    }

    private void handleAuthRequest(ClientThread client, Message message) {
        String nickname = message.getMessage();
        boolean isNameTaken = clients.stream().anyMatch(c -> c.getNickname().toLowerCase().equals(nickname.toLowerCase()));
        if (isNameTaken) {
            client.authDenied("The name " + nickname + (nickname.toLowerCase().equals("server") ? " is not accepted! " : " is already taken!"));
        } else {
            client.authAccept(message.getMessage());
            sendToAllAuthorizedClients(new Message(ChatLibrary.SERVER_NICKNAME, nickname + " подключился к чату", Message.MESSAGE));
        }
    }
}
