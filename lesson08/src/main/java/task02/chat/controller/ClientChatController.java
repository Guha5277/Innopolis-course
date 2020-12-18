package task02.chat.controller;

import task02.chat.client.ChatClient;
import task02.chat.common.Message;
import task02.chat.common.json.JsonParser;
import task02.chat.gui.GenericView;

/**
 * Класс-контроллер взаимодействия view и backend-компонентов клиентской стороны чата
 */
public class ClientChatController {
    private final String SERVICE = "SERVICE_MESSAGE";
    private GenericView view;
    private ChatClient chatClient;

    /**
     * Конструктор создающий экемпляр класса
     * @param view объект пользовательского интерфейса
     */
    public ClientChatController(GenericView view) {
        this.view = view;
        this.chatClient = new ChatClient(this);
        chatClient.connect();
    }

    /**
     * Передача сообщения клиентской части для последующей отправки на сервер
     * @param message сообщение
     */
    public void sendMessage(Message message) {
        chatClient.sendMsg(JsonParser.jsonFromClientMessage(message));
    }

    /**
     * Уведомление о старте клиентского потока
     */
    public void socketThreadStart() {
        Message message = new Message(SERVICE, "SocketThread started!", Message.INFO);
        view.showMessage(message);
    }

    /**
     * Уведомление об остановке клиентского потока
     */
    public void socketThreadStop() {
        view.lostConnection();
    }

    /**
     * Уведомление о получении сообщения со стороны сервера
     * @param msg сообщение
     */
    public void receiveMessage(String msg) {
        Message message = JsonParser.clientMessageFromJson(msg);
        view.showMessage(message);
    }

    /**
     * Уведомление о готовности клиентского сокета
     */
    public void socketReady() {
        view.connected();
    }

    /**
     * Уведомление о возникновении исключения на стороне клиентской части
     * @param e возникшее исключение
     */
    public void socketThreadException(Exception e) {
        Message message = new Message(SERVICE, e.getMessage(), Message.ERROR);
        view.showMessage(message);
    }
}
