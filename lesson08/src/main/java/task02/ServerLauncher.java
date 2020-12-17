package task02;

import task02.chat.server.ChatServer;

public class ServerLauncher {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
