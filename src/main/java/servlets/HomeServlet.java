package servlets;

import api.servlet.annotations.SimpleWebServlet;
import api.servlet.exeptions.SimpleServletException;
import api.servlet.http.SimpleHttpServlet;
import api.servlet.http.HttpServletRequest;
import api.servlet.http.HttpServletResponse;
import utils.ServletConfigPath;
import utils.ServerUtils;

import java.io.IOException;
import java.io.PrintWriter;

@SimpleWebServlet(name = "FilesServlet", value = {"/", "/home"})
public class HomeServlet extends SimpleHttpServlet {
    private static String dirPath;
    private static final String configFilePath = ServletConfigPath.CONFIG_PATH;

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
        response.setContentType("text/html");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(getHomePageHtml());
        }
    }

    private String getHomePageHtml() {
        StringBuilder filesHtml = new StringBuilder();
        for (String file : ServerUtils.getFileNamesFromBaseDir(dirPath)) {
            filesHtml.append(String.format("<a href='FileViewer?file=%s'>%s</a><br>", file, file));
        }
        return "<html><head><title>Home page</title></head>" +
                "<body style=\"font-size: 30px\"><h1>Files available:</h1>" + filesHtml +
                "</body></html>";
    }
}