package task01.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SourceMaker {
    private final String PACKAGE = "package task01.model;";
    private final String CLASS_DECL = "public class SomeClass implements Worker {";
    private final String CLASS_CONTENT = "\t@Override\n" +
            "    public void doWork() {\n";
    private final char BLOCK_END = '}';
    private final char NEWLINE = '\n';
    private final char TAB = '\t';

    private final String SOURCE_DIR = "lesson08\\src\\main\\java\\task01\\model\\";
    private final String SOURCE_FILE_NAME = "SomeClass.java";

    public String getSourceDir() {
        return SOURCE_DIR;
    }

    public String getSourceDest(){
        return SOURCE_DIR + SOURCE_FILE_NAME;
    }

    public void makeSource(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(PACKAGE).append(NEWLINE).append(NEWLINE);
        sb.append(CLASS_DECL).append(NEWLINE);
        sb.append(CLASS_CONTENT);
        sb.append(code);
        sb.append(NEWLINE).append(TAB).append(BLOCK_END);
        sb.append(NEWLINE).append(BLOCK_END);

        File sourceFile = new File(SOURCE_DIR + SOURCE_FILE_NAME);
        writeSourceToDisk(sourceFile, sb.toString());
        sourceFile.deleteOnExit();
    }

    private void writeSourceToDisk(File file, String sourceCode) {
        try (Writer writer = new FileWriter(file)) {
            writer.write(sourceCode.toCharArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write .java file into: " + file.getPath());
        }
    }
}
