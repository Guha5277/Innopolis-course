package task02.chat.server;

import task02.chat.common.Message;
import task02.chat.common.json.JsonParser;
import task02.network.SocketThread;
import task02.network.SocketThreadListener;

import java.net.Socket;

/**
 * Класс-обёртка для клиентского потока, инкапсулирующий общие сведения о клиенте (статус авторизации, никнейм)
 */
public class ClientThread extends SocketThread {
    private boolean isAuthorized;
    private String name;

    /**
     * Конструктор создающий экземпляр класса-клиента
     * @param listener слушатель событий клиентского потока
     * @param threadName имя клиентского потока
     * @param socket объект типа Socket
     * @param ghostIndex индекс гостя, используемый для неавторизованных пользователей
     *
     * @see task02.network.SocketThread
     * @see task02.network.SocketThreadListener
     */
    public ClientThread(SocketThreadListener listener, String threadName, Socket socket, int ghostIndex) {
        super(listener, threadName, socket);
        this.name = "Ghost" + ghostIndex;
    }

    /**
     * Метод проверки состояния авторизации пользователя
     * @return состояние аворизации (авторизован или нет)
     */
    public boolean isAuthorized() {
        return isAuthorized;
    }

    /**
     * Метод отправки запроса клиенту с предложением пройти авторизацию
     */
    public void authRequest(){
        sendMessage(new Message(Message.AUTH_REQUEST));
    }

    /**
     * Метод подтверждающий авторизацию пользователя
     * @param nickname никнейм установленный в ходе авторизации
     */
    public void authAccept(String nickname){
        this.name = nickname;
        isAuthorized = true;
        sendMessage(new Message(nickname, Message.AUTH_ACCEPT));
    }

    /**
     * Отказ в авторизации для пользователя
     * @param reason причина отказа
     */
    public void authDenied(String reason){
        sendMessage(new Message(reason, Message.AUTH_DENIED));
    }

    public String getNickname() {
        return name;
    }

    /**
     * Отправка сообщения пользователю
     * @param message сообщение
     * @return результат отправки сообщения
     */
    public boolean sendMessage(Message message) {
        return super.sendMessage(JsonParser.jsonFromClientMessage(message));
    }
}
