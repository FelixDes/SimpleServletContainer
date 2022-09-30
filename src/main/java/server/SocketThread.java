package server;

import simple_servlet_api.http.SimpleHttpServlet;
import simple_servlet_api.http.SimpleHttpServletRequestProcessor;
import simple_servlet_api.http.SimpleHttpServletResponse;
import simple_servlet_api.http.SimpleHttpServletResponseProcessor;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class SocketThread implements Runnable {
    private final InputStream is;
    private final OutputStream os;
    private final Map<String, SimpleHttpServlet> servletsMapping;

    public SocketThread(Socket socket, Map<String, SimpleHttpServlet> servletsMapping) throws IOException {
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.servletsMapping = servletsMapping;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            var request = new SimpleHttpServletRequestProcessor(br);

            var response = new SimpleHttpServletResponseProcessor();

            String mapping = request.getPath();

            if (servletsMapping.containsKey(mapping)) {
                String method = request.getMethod();

                switch (method) {
                    case SimpleHttpServlet.METHOD_GET -> servletsMapping.get(mapping).doGet(request, response);
                    case SimpleHttpServlet.METHOD_POST -> servletsMapping.get(mapping).doPost(request, response);
                }
            } else {
                response.sendError(SimpleHttpServletResponse.SC_NOT_FOUND, "Not Found");
            }

            os.write(response.getBytes());
            os.flush();
            os.close();

            br.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}