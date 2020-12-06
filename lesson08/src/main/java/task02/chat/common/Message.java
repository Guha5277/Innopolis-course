package task02.chat.common;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс представляющий сообщение, инкапсулирующий в себе необходимые сведения о сообщении (автор, тип, дата и само сообщение)
 */
public class Message {
    public static final int AUTH_REQUEST = 1;
    public static final int AUTH_ACCEPT = 2;
    public static final int AUTH_DENIED = 3;
    public static final int MESSAGE = 4;
    public static final int ERROR = 5;
    public static final int INFO = 6;

    private int messageType;
    private String author;
    private String message;
    private LocalTime time;

    private Message(){
        this.time = LocalTime.now();
    }

    /**
     * Конструктор с одним параметром на входе определяющим тип сообщения
     * @param messageType тип сообщения
     */
    public Message(int messageType) {
        this();
        this.messageType = messageType;
    }

    /**
     * Конструктор определяющий тип сообщения и само сообщение
     * @param message сообщение
     * @param messageType тип сообщения
     */
    public Message(String message, int messageType) {
        this();
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * Конструктор определяющий максимальное количество параметров сообщения
     * @param author автор сообщения
     * @param message сообщение
     * @param messageType тип сообщения
     */
    public Message(String author, String message, int messageType) {
        this(message, messageType);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeString = time.format(formatter);
        return timeString + " " + author + ": " + message;
    }
}
