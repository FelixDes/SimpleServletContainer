package simple_servlet_api.http;

import simple_servlet_api.exeptions.SimpleServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SimpleHttpServlet {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public void init() throws SimpleServletException {
    }

    public void doGet(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws SimpleServletException, IOException {
        response.sendError(SimpleHttpServletResponse.SC_METHOD_NOT_ALLOWED, "Not Supported");
    }

    public void doPost(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws SimpleServletException, IOException {
        response.sendError(SimpleHttpServletResponse.SC_METHOD_NOT_ALLOWED, "Not Supported");
    }
}
