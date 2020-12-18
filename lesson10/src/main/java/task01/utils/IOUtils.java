package task01.utils;

import java.io.*;

/**
 * Утилитный класс для записи и чтения файлов
 */
public class IOUtils {
    /**
     * Метод чтения данных из файла
     * @param file считываемый файл
     * @return массив считанных символов
     * @throws IOException в случае проблем с чтением файла
     */
    public static char[] readFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char[] buf = new char[(int) file.length()];
        reader.read(buf);
        reader.close();
        return buf;
    }

    /**
     * Метод записи данных в файл
     * @param contains записываемые данные
     * @param fileName файл назначения
     * @throws IOException в слуае проблем с записью файла
     */
    public static void saveFile(char[] contains, File fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(contains);
        writer.close();
    }
}
