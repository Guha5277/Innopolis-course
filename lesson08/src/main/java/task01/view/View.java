package task01.view;

import task01.compiler.CodeCompiler;

import java.io.PrintStream;
import java.util.Scanner;

public class View {
    private boolean isInterrupted;
    private final Scanner scanner;
    private final PrintStream output;
    private final String WELCOME_MSG = "Введите код метода doWork() построчно. Введите пустую строку для завершения ввода\n";
    private StringBuilder codeReceiver = new StringBuilder();
    private CodeCompiler compiler = new CodeCompiler();

    public View() {
        scanner = new Scanner(System.in);
        output = new PrintStream(System.out);
    }

    public void start(){
        output.print(WELCOME_MSG);
        while (!isInterrupted) {
            handleMsg(scanner.nextLine());
        }
    }

    public void stop(){
        isInterrupted = true;
    }

    private void handleMsg(String msg) {
        if (msg.length() == 0) {
            if (codeReceiver.toString().length() > 0) codeReceiver.deleteCharAt(codeReceiver.lastIndexOf("\n"));
            output.println("Save, compile and run code...");
            compiler.compileAndRun(codeReceiver.toString());
            stop();
        } else {
            codeReceiver.append("\t\t");
            codeReceiver.append(msg);
            codeReceiver.append('\n');
        }
    }
}
