package task02.network;

import java.net.Socket;

/**
 * Интерфейс-слушатель клиентских событий
 */
public interface SocketThreadListener {
    /**
     * Метод оповещающий о запуска потока
     * @param thread поток который был запущен
     * @param socket сокет ассоциированный с потоком
     */
    void onSocketThreadStart(SocketThread thread, Socket socket);

    /**
     * Метод извещающий об осстановке потока
     * @param thread поток, который был остановлен
     */
    void onSocketThreadStop(SocketThread thread);

    /**
     * Метод вызываемый после получения сообщений
     * @param thread поток в котором пришло сообщение
     * @param socket сокет ассоциированный с потоком
     * @param msg полученное сообщение
     */
    void onReceiveString(SocketThread thread, Socket socket, String msg);

    /**
     * Метод вызываемый по готовности потока
     * @param thread поток который был инициализирован и готов к работе
     * @param socket сокет ассоциированный с потоком
     */
    void onSocketReady(SocketThread thread, Socket socket);

    /**
     * Метод вызываемый в случае возникновения исключения
     * @param thread поток в котором произошло исключение
     * @param e произошедшее исключение
     */
    void onSocketThreadException(SocketThread thread, Exception e);
}
