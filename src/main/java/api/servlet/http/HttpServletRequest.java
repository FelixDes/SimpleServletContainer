package api.servlet.http;

import java.util.Map;

public interface HttpServletRequest {
    String getMethod();

    String getParameter(String file);

    String getPath();

    String getQueryString();

    Map<String, String[]> getHeaders();
}
