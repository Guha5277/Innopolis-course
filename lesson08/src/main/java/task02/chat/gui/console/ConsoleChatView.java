package task02.chat.gui.console;

import task02.chat.common.ChatLibrary;
import task02.chat.common.Message;
import task02.chat.controller.ClientChatController;
import task02.chat.gui.GenericView;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Класс реализующий вывод сообщений в консоль и получение сообщений от клиента
 */
public class ConsoleChatView implements GenericView {
    private boolean isInterrupted;
    private boolean isAuthMode;
    private Scanner scanner;
    private PrintStream output;
    private PrintStream errOutput;
    private ClientChatController controller;
    private boolean isConnected;

    public ConsoleChatView() {
        String encoding = System.getProperty("console.encoding", "utf-8");

        scanner = new Scanner(System.in, encoding);
        try {
            output = new PrintStream(System.out, true, encoding);
            errOutput = new PrintStream(System.err, true, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        controller = new ClientChatController(this);
        start();
    }

    /**
     * Старт обработчика сообщений
     */
    public void start() {
        output.println(ChatLibrary.WELCOME_MSG);
        while (!isInterrupted) {
            handleConsoleMessage(scanner.nextLine());
        }
    }

    /**
     * Остановка обработчика сообщений
     */
    public void stop() {
        isInterrupted = true;
    }

    @Override
    public void showMessage(Message message) {
        switch (message.getMessageType()) {
            case Message.AUTH_REQUEST:
                isAuthMode = true;
                showMsg(ChatLibrary.AUTH_REQUEST);
                break;
            case Message.AUTH_ACCEPT:
                isAuthMode = false;
                showMsg("Успено авторизован как " + message.getMessage() + "!");
                break;
            case Message.AUTH_DENIED:
                isAuthMode = true;
                showError("Ошибка аворизации! " + message.getMessage());
                break;
            case Message.ERROR:
                showError("Ошибка!" + message.getMessage());
                break;
            case Message.MESSAGE:
                String author = message.getAuthor();
                if (author != null && author.equals(ChatLibrary.SERVER_NICKNAME)) {
                    showError(message.toString());
                    break;
                }
                output.println(message);
                break;
            case Message.INFO:
                output.println(message);
                break;
        }
    }

    @Override
    public void connected() {
        setConnected(true);
    }

    @Override
    public void lostConnection() {
        setConnected(false);
    }

    private void handleConsoleMessage(String msg) {
        if (msg.length() == 0) {
            showError("Введите сообщение!");
            return;
        }
        if (!isConnected) {
            showError("Нет соедениения с сервером...");
            return;
        }
        if (isAuthMode) {
            boolean validNickname = isNicknameValid(msg);
            if (!validNickname) {
                showError("Никнейм должен содержать не менее 4х символов, без пробелов!");
                showError(ChatLibrary.AUTH_REQUEST);
            } else {
                showMsg("Привет, " + msg + "! Пробуем авторизоваться...");
                controller.sendMessage(new Message(msg, Message.AUTH_REQUEST));
            }
        } else {
            Message message;
            if (msg.startsWith("/")) {
                showMsg("Отправляем запрос на сервер");
                message = new Message(msg.substring(1), Message.INFO);
            } else {
                message = new Message(msg, Message.MESSAGE);
            }
            controller.sendMessage(message);
        }
    }

    private boolean isNicknameValid(String nickname) {
        return nickname.length() >= 4 && !nickname.contains(" ");
    }

    private void setConnected(boolean connected) {
        if (isConnected && !connected) {
            showError("Соединение потреяно!");
        } else if (!isConnected && connected) {
            showError("Соединение установлено!");
        }
        isConnected = connected;
    }

    private void showMsg(String msg) {
        output.println(msg);
    }

    private void showError(String errMsg) {
        errOutput.println(errMsg);
    }
}
