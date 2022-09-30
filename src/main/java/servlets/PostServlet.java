package servlets;

import api.servlet.annotations.SimpleWebServlet;
import api.servlet.exeptions.SimpleServletException;
import api.servlet.http.SimpleHttpServlet;
import api.servlet.http.HttpServletRequest;
import api.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@SimpleWebServlet(name = "PostServlet", value = "/PostServlet")
public class PostServlet extends SimpleHttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(getPostPageHtml());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SimpleServletException, IOException {
        response.setContentType("text/text");

        try (PrintWriter printWriter = response.getWriter();) {
            printWriter.write(request.getParameter("username"));
            printWriter.write('\n');
            printWriter.write(request.getParameter("password"));
        }
    }

    private String getPostPageHtml() {
        return "<html><head><title>Post servlet</title></head>" +
                "<body style=\"font-size: 30px\">" +
                "<form name=\"loginForm\" method=\"post\" action=\"PostServlet\">" +
                "    Username: <input type=\"text\" name=\"username\"/> <br/>" +
                "    Password: <input type=\"password\" name=\"password\"/> <br/>" +
                "    <input type=\"submit\" value=\"Login\" />\n" +
                "</form>" +
                "</body></html>";
    }
}
