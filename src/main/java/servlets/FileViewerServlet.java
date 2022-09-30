package servlets;

import api.servlet.annotations.SimpleWebServlet;
import api.servlet.exeptions.SimpleServletException;
import api.servlet.http.SimpleHttpServlet;
import api.servlet.http.HttpServletRequest;
import api.servlet.http.HttpServletResponse;
import utils.ServerConfigPath;
import utils.ServerUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@SimpleWebServlet(name = "FileViewer", value = "/FileViewer")
public class FileViewerServlet extends SimpleHttpServlet {
    Map<String, byte[]> filesCache = new HashMap<>();

    private static String dirPath;
    private static final String configFilePath = ServerConfigPath.CONFIG_PATH;


    @Override
    public void init() throws SimpleServletException {
        try {
            dirPath = ServerUtils.getUrlFromConfig(configFilePath);
        } catch (Exception e) {
            throw new SimpleServletException("Something went wrong with config file parsing.\n" +
                    "Please, check config file at: " + configFilePath);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("file");

        try (OutputStream os = response.getOutputStream()) {
            byte[] content;

            if (ServerUtils.getFileNamesFromBaseDir(dirPath).contains(fileName)) {
                if (filesCache.containsKey(fileName)) {
                    content = filesCache.get(fileName);
                } else {
                    File f = new File(dirPath + fileName);
                    content = Files.readAllBytes(f.toPath());

                    filesCache.put(fileName, content);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                content = getNotFoundPageHtmlBytes();
            }

            os.write(content);
            os.flush();
        }
    }

    private byte[] getNotFoundPageHtmlBytes() {
        String html = "<html><head><title>FileViewer</title></head>" +
                "<body style=\"background-color: #000; color: #fff; font-size: 30px\">404 File Not Found</body></html>";
        return html.getBytes();
    }
}
