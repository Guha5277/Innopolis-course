package task01.utils;

import java.io.*;

public class IOUtils {
    public static char[] readFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        char[] buf = new char[(int) file.length()];
        reader.read(buf);
        reader.close();
        return buf;
    }

    public static void saveFile(char[] contains, File fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(contains);
        writer.close();
    }
}
