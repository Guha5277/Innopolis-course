package task02.chat.gui;

import task02.chat.common.Message;

/**
 * Общий интерфейс вывода
 */
public interface GenericView {
    /**
     * Метод обработки и вывода полученного сообщения
     * @param message полученное сообщение
     */
    void showMessage(Message message);

    /**
     * Метод вызываемый в случае успешного установления соединения с сервером
     */
    void connected();

    /**
     * Метод вызываемый при потере соединения с сервером
     */
    void lostConnection();
}
