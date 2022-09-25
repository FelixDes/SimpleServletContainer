package server;

import org.json.JSONObject;
import org.reflections.Reflections;
import simple_servlet_api.annotations.SimpleWebServlet;
import simple_servlet_api.exeptions.SimpleServletException;
import simple_servlet_api.http.SimpleHttpServlet;
import utils.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimpleServer {
    private static int port;
    private static String url;
    private static String servlets_package;
    private static final String configFilePath = ServerConfig.configPath;

    private List<Class<?>> servletClasses;
    private List<SimpleHttpServlet> servlets;
    private Map<String, SimpleHttpServlet> mapping;


    public SimpleServer() throws Exception {
        parseConfig();

        setServletClasses();
        setServletsMapping();
    }

    private void setServletClasses() {
        Reflections reflections = new Reflections(servlets_package);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(SimpleWebServlet.class);
        servletClasses = set.stream().toList();
    }

    private void setServletsMapping() throws Exception {
        mapping = new HashMap<>();
        servlets = new ArrayList<>();

        for (Class<?> aClass : servletClasses) {
            SimpleHttpServlet servlet = (SimpleHttpServlet) aClass.getDeclaredConstructor().newInstance();
            servlets.add(servlet);

            SimpleWebServlet annotation = aClass.getAnnotation(SimpleWebServlet.class);
            var value = annotation.value();

            for (String url : value) {
                if (mapping.containsKey(url)) {
                    throw new Exception("Mapping: " + url + " is not valid. Servlet " + mapping.get(url) + " already has this mapping");
                } else {
                    mapping.put(url, servlet);
                }
            }
        }
    }

    public void run() throws Exception {
        initServlets();

        try (ServerSocket ss = new ServerSocket(port)) {
            while (true) {
                Socket socket = ss.accept();
                new Thread(new SocketThread(socket, mapping)).start();
            }
        } catch (Exception e) {
            throw new Exception("Something went wrong with ServerSocket initialization on port: " + port +
                    "\nPlease, check config file at: " + configFilePath);
        }
    }

    private void initServlets() throws SimpleServletException {
        for (SimpleHttpServlet servlet : servlets) {
            servlet.init();
        }
    }

    public void parseConfig() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII));
            port = jsonObject.getInt("port");
            url = jsonObject.getString("url");
            servlets_package = jsonObject.getString("servlets_package");
        } catch (IOException e) {
            throw new Exception("Something went wrong with config file parsing. \nPlease, check config file at: " + configFilePath);
        }
    }
}
