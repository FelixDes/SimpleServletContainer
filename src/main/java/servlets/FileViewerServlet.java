package servlets;

import simple_servlet_api.annotations.SimpleWebServlet;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@SimpleWebServlet(name = "FileViewer", value = "/FileViewer")
@WebServlet(name = "FileViewer", value = "/FileViewer")
public class FileViewerServlet extends HttpServlet {
    Map<String, byte[]> filesCache = new HashMap<>();

    private static String dirPath;
    private static final String configFilePath = utils.ServletConfig.configPath;


    @Override
    public void init(javax.servlet.ServletConfig config) throws ServletException {
        super.init(config);
        try {
            dirPath = ServletUtils.getFileUrlFromConfig(configFilePath);
        } catch (Exception e) {
            throw new ServletException("Something went wrong with config file parsing.\n" +
                    "Please, check config file at: " + configFilePath);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("file");

        OutputStream os = response.getOutputStream();

        byte[] content;

        if (ServletUtils.getFileNamesFromBaseDir(dirPath).contains(fileName)) {
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
        os.close();
    }

    private byte[] getNotFoundPageHtmlBytes() {
        String html = "<html><head><title>FileViewer</title></head>" +
                "<body style=\"background-color: #000; color: #fff; font-size: 30px\">404 File Not Found</body></html>";
        return html.getBytes();
    }
}
