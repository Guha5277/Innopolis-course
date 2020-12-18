package task02.chat.common.json;

import com.google.gson.Gson;
import task02.chat.common.Message;

/**
 * Парсер сообщений в/из Json-формат
 */
public class JsonParser {
    /**
     * Метод конвертирует строку в объект типа Message
     * @param json json-строка
     * @return объект типа Message
     */
    public static Message clientMessageFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }

    /**
     * Метод кодирующий объект типа Message в json-строку
     * @param msg отправляемый объект типа Message
     * @return json-строка
     */
    public static String jsonFromClientMessage(Message msg) {
        Gson gson = new Gson();
        return gson.toJson(msg);
    }
}
