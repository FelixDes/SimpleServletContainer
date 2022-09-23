package servlets;

import simple_servlet_api.annotations.SimpleWebServlet;
import utils.ServletConfig;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SimpleWebServlet(name = "FilesServlet", value = {"/", "/home"})
@WebServlet(name = "FilesServlet", value = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private static String dirPath;
    private static final String configFilePath = ServletConfig.configPath;

    @Override
//    public void init(javax.servlet.ServletConfig config) throws ServletException {
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
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(getHomePageHtml());
        printWriter.close();
    }

    private String getHomePageHtml() {
        StringBuilder filesHtml = new StringBuilder();
        for (String file : ServletUtils.getFileNamesFromBaseDir(dirPath)) {
            filesHtml.append(String.format("<a href='FileViewer?file=%s'>%s</a><br>", file, file));
        }
        return "<html><head><title>Home page</title></head>" +
                "<body style=\"font-size: 30px\"><h1>Files available:</h1>" + filesHtml + "</body></html>";
    }
}