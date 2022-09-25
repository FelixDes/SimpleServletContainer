package simple_servlet_api.http;

public interface SimpleHttpServletRequest {
    public String getMethod();
    String getParameter(String file);
}
