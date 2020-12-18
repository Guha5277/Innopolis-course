package task02.network;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Интерфейс-слушатель событий сервера
 */
public interface ServerSocketThreadListener {

    /**
     * Метод оповещающий о запуске потока
     * @param thread поток который был запущен
     */
    void onThreadStart(ServerSocketThread thread);

    /**
     * Метод оповещающий о старте сервера
     * @param thread поток ассоциируемый с сервером
     * @param server сервер
     */
    void onServerStart(ServerSocketThread thread, ServerSocket server);

    /**
     * Метод вызываемый по таймауту ожидания сервера клиентов
     * @param thread поток ассоциируемый с сервером
     * @param server сервер
     */
    default void onServerAcceptTimeout(ServerSocketThread thread, ServerSocket server){}

    /**
     * Метод вызываемый при подключении клиента
     * @param server сервер
     * @param socket сокет подключенного клиента
     */
    void onSocketAccepted(ServerSocket server, Socket socket);

    /**
     * Метод вызываемый в случае возникновения исключения
     * @param thread поток ассоциируемый с сервером
     * @param e исключение
     */
    void onServerException(ServerSocketThread thread, Exception e);

    /**
     * Метод извещающий об остановке потока
     * @param thread поток ассоциируемый с сервером
     */
    void onThreadStop(ServerSocketThread thread);
}
