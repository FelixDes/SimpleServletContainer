package servlets;

import simple_servlet_api.annotations.SimpleWebServlet;
import simple_servlet_api.exeptions.SimpleServletException;
import simple_servlet_api.http.SimpleHttpServlet;
import simple_servlet_api.http.SimpleHttpServletRequest;
import simple_servlet_api.http.SimpleHttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@SimpleWebServlet(name = "PostServlet", value = "/PostServlet")
public class PostServlet extends SimpleHttpServlet {

    @Override
    public void doGet(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(getPostPageHtml());
        printWriter.close();
    }

    @Override
    public void doPost(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws SimpleServletException, IOException {
        response.setContentType("text/text");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(request.getParameter("username"));
        printWriter.write('\n');
        printWriter.write(request.getParameter("password"));
        printWriter.close();
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
