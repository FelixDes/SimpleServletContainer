package simple_servlet_api.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface SimpleHttpServletResponse {
    public static final int STATUS_OK = 200;
    public static final int STATUS_NOT_FOUND = 404;

    OutputStream getOutputStream() throws IOException;

    void setStatus(int status) throws IOException;

    void setContentType(String type) throws IOException;

    void setContentLength(long length) throws IOException;

    void setHeader(String name, String value) throws IOException;

    PrintWriter getWriter();
}
