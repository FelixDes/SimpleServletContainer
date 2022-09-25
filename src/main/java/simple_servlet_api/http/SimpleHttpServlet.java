package simple_servlet_api.http;

import simple_servlet_api.exeptions.SimpleServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SimpleHttpServlet {
    public abstract void init() throws SimpleServletException;

    public abstract void doGet(SimpleHttpServletRequest request, SimpleHttpServletResponse response) throws SimpleServletException, IOException;
}
