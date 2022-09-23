package server;

import org.json.JSONObject;
import org.reflections.Reflections;
import simple_servlet_api.annotations.SimpleWebServlet;
import simple_servlet_api.http.SimpleHttpServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimpleServer {
    private static int port;
    private static String url;
    private static String SERVLETS_PACKAGE_NAME = "servlets";
    private static final String configFilePath = "/home/felix/_Programming/Idea_Projects/OOP/OOP_1/config/config.json";

    private List<Class<?>> servletClasses;
    private List<SimpleHttpServlet> servlets;
    private Map<String, SimpleHttpServlet> mapping;


    public SimpleServer() throws Exception {
        setServletClasses();
        setServletsAndMapping();
    }

    private void setServletClasses() {
        Reflections reflections = new Reflections(SERVLETS_PACKAGE_NAME);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(SimpleWebServlet.class);
        servletClasses = set.stream().toList();
    }

    private void setServletsAndMapping() throws Exception {
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

//        parsePortAndUrl();
//
//        try (ServerSocket ss = new ServerSocket(port)) {
//            while (true) {
//                Socket socket = ss.accept();
//                new Thread(new SocketThread(socket, url)).start();
//            }
//        } catch (Exception e) {
//            throw new Exception("Something went wrong with ServerSocket initialization on port: " + port +
//                    "\nPlease, check config file at: " + configFilePath);
//        }
    }

    private void initServlets() {
        servlets.forEach(SimpleHttpServlet::init);
    }

    public void parsePortAndUrl() throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII));
            port = jsonObject.getInt("port");
            url = jsonObject.getString("url");
        } catch (IOException e) {
            throw new Exception("Something went wrong with config file parsing. \nPlease, check config file at: " + configFilePath);
        }
    }
}
