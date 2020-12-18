package task01;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс помогающий формировать HTTP-ответы:
 * - Получать заголовки ответов
 * - Получать полный ответ (заголовок + содержимое в html-формате)
 */
public class HtmlHelper {
    private final int NOT_FOUND = 404;
    private final int OK = 200;

    private final String FILE_SEPARATOR = System.getProperty("file.separator");
    private final String OK_MESSAGE = "OK";
    private final String NOT_FOUND_MESSAGE = "Not found";
    private final String HOST_ROOT = "http://localhost/";
    private final String NOT_FOUND_PAGE = "404.html";
    private final Map<String, String> CONTENT_TYPE = new HashMap<String, String>() {{
        put("html", "text/html");
        put("", "text/plain");
    }};

    private final Map<String, Boolean> AVAILABLE_EXTENSIONS = new HashMap<String, Boolean>() {{
        put("html", true);
        put("txt", true);
        put("xml", true);
        put("iml", true);
        put("java", true);
        put("", false);
    }};

    /**
     * Метод получения заголовка со статусом 200 ОК
     *
     * @param contentLength длина передаваемого содержимого
     * @return HTTP-заголовок
     */
    public String getOkHeader(int contentLength) {
        return getHeader(OK, OK_MESSAGE, CONTENT_TYPE.get(""), contentLength);
    }

    /**
     * Метод получения заголовка со статусом 404 Not Found
     *
     * @return HTTP-заголовок
     */
    public String getNotFoundPage() {
        String body = getBody(NOT_FOUND_PAGE);
        return getHeader(NOT_FOUND, NOT_FOUND_MESSAGE, CONTENT_TYPE.get("html"), body.length()) + getBody(NOT_FOUND_PAGE);
    }

    /**
     * Метод анализирует содержимое дирректории и составлят на его основе готовую html-страницу
     * для представления содержимого.
     *
     * @param content     содержимое отображаемой дирректории
     * @param mediatePath промежуточный путь от корневой дирректории до текущей
     * @return готовый HTTP ответ (статус, заголовк и контент)
     */
    public String getContentPage(List<File> content, String mediatePath) {
        StringBuilder contentBuilder = new StringBuilder();

        if (mediatePath.length() > 0) {
            contentBuilder.append(getTextLine("<b>Текущая директория: </b>" + mediatePath));
            String previousDir = getPreviousDirectory(mediatePath);
            contentBuilder.append(getLink(previousDir, "", "&lt;---Назад"));
        }

        if (content.size() == 0) {
            contentBuilder.append(getTextLine("Пустая дирректория :с"));
        } else {
            for (File f : content) {
                contentBuilder.append(getFilePart(f, mediatePath));
            }
        }

        String contentString = getBody(HOST_ROOT + mediatePath, contentBuilder.toString());
        int size = contentString.getBytes().length;
        return getHeader(OK, OK_MESSAGE, CONTENT_TYPE.get("html"), size) + contentString;
    }

    private String getPreviousDirectory(String mediatePath) {
        int from = 0;
        char[] chars = mediatePath.toCharArray();
        for (int i = chars.length - 2; i >= 0; i--) {
            if (chars[i] == FILE_SEPARATOR.charAt(0)) {
                from = i;
                break;
            }
        }

        return (from <= 0) ? "" : mediatePath.substring(0, from);
    }

    private String getFilePart(File file, String mediatePath) {
        if (file.isDirectory() || isAvailableToRead(file)) {
            String filedDest = file.getName();
            return getLink(filedDest, mediatePath, filedDest);
        } else {
            return getTextLine(file.getName());
        }
    }

    private boolean isAvailableToRead(File file) {
        String fileName = file.getName();
        System.out.println(fileName);
        int from = fileName.indexOf('.');
        if (from < 0) return false;
        String extension = fileName.substring(from + 1);
        Boolean result = AVAILABLE_EXTENSIONS.get(extension);
        return (result != null && result);
    }

    private String getLink(String sourceDest, String mediatePath, String linkName) {
        return String.format("<p><a href=\"" + HOST_ROOT + "%s\">%s</a></p>%n", (mediatePath + sourceDest).replace(FILE_SEPARATOR, "/"), linkName);
    }

    private String getTextLine(String lineName) {
        return String.format("<p>%s</>%n", lineName);
    }

    private String getHeader(int statusCode, String statusText, String contentType, int contentLength) {
        return String.format("HTTP/1.1 %d %s%n", statusCode, statusText) +
                String.format("Content-type: %s%n", contentType) +
                String.format("Content-length: %d%n%n", contentLength);
    }

    private String getBody(String title, String content) {
        String header = String.format("<html xmlns=\"https://www.w3.org/1999/xhtml\" xml:lang=\"ru\">\n" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<title>%s</title>" +
                "<body>", title);
        return header + content + "</body></html>";
    }

    private String getBody (String pathToPage){
        File file = new File(pathToPage);
        try {
            Reader reader = new FileReader(file);
            char[] buf = new char[(int) file.length()];
            reader.read(buf, 0, (int) file.length());
            return new String(buf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}