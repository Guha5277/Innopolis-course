package task01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс обрабатывающий HTTP-запрос клиента
 */
public class RequestHandler {
    private final String ROOT_DIR = "";
    private final String FILE_SEPARATOR = System.getProperty("file.separator");
    private File file;
    private HtmlHelper htmlHelper;

    public RequestHandler() {
        this.htmlHelper = new HtmlHelper();
    }

    /**
     * Основной метод для обработки HTTP запроса
     * @param request запрашиваемая директория или файл
     * @return готовый HTTP ответ на запрос
     */
    public String handleRequest(String request) {
        System.out.println("Handling client request: " + request);
        file = new File(ROOT_DIR + request);
        if (!file.exists()) {
            System.out.println("File/dir " + file.getAbsolutePath() + " not found!");
            return htmlHelper.getNotFoundPage();
        }
        if (file.isDirectory()) {
            System.out.println("Is a directory: " + file.getName());
            String mediatePath = getMediatePath(file);
            System.out.println("Mediate path: " + mediatePath);
            List<File> content = getContent(file, mediatePath);
            return htmlHelper.getContentPage(content, mediatePath);
        } else {
            boolean isAvailableToReadFile = isAvailableFile(file);
            System.out.println("File/dir " + file.getName() + "is available: " + isAvailableToReadFile);
            if (isAvailableToReadFile){
                String fileContent = readFile(file);
                return htmlHelper.getOkHeader(fileContent.length()) + fileContent;
            } else {
                return htmlHelper.getNotFoundPage();
            }
        }
    }

    private String readFile(File file) {
        try {
            FileReader reader = new FileReader(file);
            char[] ch = new char[(int)file.length()];
            reader.read(ch, 0, ch.length);
            return new String(ch);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean isAvailableFile(File file) {
        final String XML = "xml";
        final String IML = "iml";
        final String TXT = "txt";
        final String HTML = "html";
        final String JAVA = "java";

        String filename = file.getName();
        System.out.println("Filename: " + filename);

        String extension = filename.substring(filename.indexOf('.') + 1);
        System.out.println("extension: " + extension);

        switch (extension) {
            case XML:
            case IML:
            case TXT:
            case HTML:
            case JAVA:
                return true;
            default:
                return false;
        }
    }

    private List<File> getContent(File file, String mediatePath) {
        File[] content = file.listFiles();

        return Arrays.stream(content).sorted((o1, o2) -> {
            boolean isDir = o1.isDirectory();
            boolean isDir2 = o2.isDirectory();
            return (isDir == isDir2) ? 0 : ((isDir) ? -1 : 1);
        }).collect(Collectors.toList());
    }

    private String getMediatePath(File file) {
        List<String> mediatePathDirs = new ArrayList<>();
        File parentFile = file;
        while (parentFile != null && !parentFile.getName().equals(ROOT_DIR)) {
            mediatePathDirs.add(parentFile.getName());
            parentFile = parentFile.getParentFile();
        }

        if (mediatePathDirs.size() == 0) return "";

        StringBuilder result = new StringBuilder();
        for (int i = mediatePathDirs.size() - 1; i >= 0; i--) {
            result.append(mediatePathDirs.get(i)).append(FILE_SEPARATOR);
        }
        return result.toString();
    }
}
