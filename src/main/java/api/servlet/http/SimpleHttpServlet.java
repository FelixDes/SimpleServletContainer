package api.servlet.http;

import api.servlet.exeptions.SimpleServletException;

import java.io.IOException;

public abstract class SimpleHttpServlet {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public void init() throws SimpleServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws SimpleServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Not Supported");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws SimpleServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Not Supported");
    }
}
