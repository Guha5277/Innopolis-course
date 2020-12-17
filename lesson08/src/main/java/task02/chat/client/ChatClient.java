package task02.chat.client;

import task02.chat.controller.ClientChatController;
import task02.network.SocketThread;
import task02.network.SocketThreadListener;

import java.io.IOException;
import java.net.Socket;

/**
 * Клиентская часть чата. Инкапсулирует в себе сведения о сервере, клиентский поток и контроллер для обработки событий
 */
public class ChatClient implements SocketThreadListener {
    private final String IP = "127.0.0.1";
    private final int PORT = 5555;
    private SocketThread socketThread;
    private ClientChatController controller;

    /**
     * Конструктор для создания экземпляра класса
     * @param controller Контроллер связывающий чат с выводом, отвечающий за обработку событий чата (сообщения, состояния подключения и т.д.)
     */
    public ChatClient(ClientChatController controller) {
        this.controller = controller;
    }

    /**
     * Метод отправки сообщения серверу
     * @param msg отправляемое сообщение
     */
    public void sendMsg(String msg){
        socketThread.sendMessage(msg);
    }

    /**
     * Метод инициирующий соединение с сервером
     */
    public void connect(){
        try {
            Socket socket = new Socket(IP, PORT);
            socketThread = new SocketThread(this, "client", socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
        controller.socketThreadStart();
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {
        controller.socketThreadStop();
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        controller.receiveMessage(msg);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        controller.socketReady();
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception e) {
        controller.socketThreadException(e);
    }
}
