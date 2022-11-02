package server;

import servlet.http.SimpleHttpServlet;
import servlet.http.SimpleHttpServletRequest;
import servlet.http.HttpServletResponse;
import servlet.http.SimpleHttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.util.*;

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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is)); is; os) {
            var request = new SimpleHttpServletRequest(br);
            var response = new SimpleHttpServletResponse();

            String mapping = request.getPath();

            if (servletsMapping.containsKey(mapping)) {
                String method = request.getMethod();

                switch (method) {
                    case SimpleHttpServlet.METHOD_GET -> servletsMapping.get(mapping).doGet(request, response);
                    case SimpleHttpServlet.METHOD_POST -> servletsMapping.get(mapping).doPost(request, response);
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
            }

            os.write(response.getBytes());

            os.flush();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}