package simple_servlet_api.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SimpleHttpServletRequestProcessor implements SimpleHttpServletRequest {
    private String method;
    private String path;
    private String queryString;
    private Map<String, String> queryParameters;
    private final Map<String, String[]> headersMap = new HashMap<>();

    public SimpleHttpServletRequestProcessor(BufferedReader request) throws IOException {
        parseFirstLine(request.readLine());
        parseHeaders(request);
    }

    private void parseHeaders(BufferedReader request) throws IOException {
        String line;
        while ((line = request.readLine()) != null) {
            if (line.equals("")) {
                break;
            }
            String name = line.split(": ")[0];
            String value = line.split(": ")[1];
            Stream<String> stringStream = (Arrays.stream(value.split(",")).map(String::trim));
            headersMap.put(name, stringStream.toArray(String[]::new));
        }
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getParameter(String parameter) {
        return queryParameters.get(parameter);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public Map<String, String[]> getHeaders() {
        return headersMap;
    }

    private void parseFirstLine(String firstLine) {
        int startOfPath = firstLine.indexOf(' ') + 1;
        int startOfQuery = firstLine.indexOf('?');
        int endOfQuery = firstLine.indexOf(' ', startOfPath);

        method = firstLine.substring(0, startOfPath);

        String query = "";

        if (startOfQuery != -1) {
            path = firstLine.substring(startOfPath, startOfQuery);
            query = firstLine.substring(startOfQuery + 1, endOfQuery);
        } else {
            path = firstLine.substring(startOfPath, endOfQuery);
        }

        queryString = query;

        queryParameters = parseQuery(query);
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryParameters = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            if (queryParameter.contains("=")) {
                String name = queryParameter.split("=")[0], val = queryParameter.split("=")[1];
                queryParameters.put(name, val);
            }
        }
        return queryParameters;
    }

}
