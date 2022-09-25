package simple_servlet_api.http;

import java.util.Map;

public interface SimpleHttpServletRequest {
    String getMethod();

    String getParameter(String file);

    String getPath();

    String getQueryString();

    Map<String, String[]> getHeaders();
}
