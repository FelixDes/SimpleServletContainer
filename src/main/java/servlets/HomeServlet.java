package servlets;

import simple_servlet_api.annotations.SimpleWebServlet;
import simple_servlet_api.exeptions.SimpleServletException;
import simple_servlet_api.http.SimpleHttpServlet;
import simple_servlet_api.http.SimpleHttpServletRequest;
import simple_servlet_api.http.SimpleHttpServletResponse;
import utils.ServerConfig;
import utils.ServerUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SimpleWebServlet(name = "FilesServlet", value = {"/", "/home"})
public class HomeServlet extends SimpleHttpServlet {
    private static String dirPath;
    private static final String configFilePath = ServerConfig.configPath;

    @Override
    public void init() throws SimpleServletException {
        try {
            dirPath = ServerUtils.getFileUrlFromConfig(configFilePath);
        } catch (Exception e) {
            throw new SimpleServletException("Something went wrong with config file parsing.\n" +
                    "Please, check config file at: " + configFilePath);
        }
    }

    @Override
    public void doGet(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(getHomePageHtml());
        printWriter.close();
    }

    private String getHomePageHtml() {
        StringBuilder filesHtml = new StringBuilder();
        for (String file : ServerUtils.getFileNamesFromBaseDir(dirPath)) {
            filesHtml.append(String.format("<a href='FileViewer?file=%s'>%s</a><br>", file, file));
        }
        return "<html><head><title>Home page</title></head>" +
                "<body style=\"font-size: 30px\"><h1>Files available:</h1>" + filesHtml + "</body></html>";
    }
}