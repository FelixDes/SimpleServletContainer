package server;

import org.json.JSONObject;
import org.reflections.Reflections;
import api.servlet.annotations.SimpleWebServlet;
import api.servlet.exeptions.SimpleServletException;
import api.servlet.http.SimpleHttpServlet;
import utils.ServerConfigPath;
import utils.ServerUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SimpleServer {
    private static final String configFilePath = ServerConfigPath.CONFIG_PATH;
    private static final int port = ServerUtils.getPortFromConfig(configFilePath);
    private static String servletsPackage = "servlets";

    private final List<Class<?>> servletClasses;
    private final List<SimpleHttpServlet> servlets;
    private final Map<String, SimpleHttpServlet> mapping;


    public SimpleServer() throws Exception {
        servletClasses = readServletClasses();
        servlets = readServlets();
        mapping = readMapping();
    }

    private List<Class<?>> readServletClasses() {
        Reflections reflections = new Reflections(servletsPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(SimpleWebServlet.class);
        return set.stream().toList();
    }

    private List<SimpleHttpServlet> readServlets() throws Exception {
        List<SimpleHttpServlet> servlets = new ArrayList<>();

        for (Class<?> aClass : servletClasses) {
            SimpleHttpServlet servlet = (SimpleHttpServlet) aClass.getDeclaredConstructor().newInstance();
            servlets.add(servlet);
        }
        return servlets;
    }

    private Map<String, SimpleHttpServlet> readMapping() throws Exception {
        Map<String, SimpleHttpServlet> mapping = new HashMap<>();

        for (int i = 0, servletClassesSize = servletClasses.size(); i < servletClassesSize; i++) {
            Class<?> aClass = servletClasses.get(i);
            SimpleWebServlet annotation = aClass.getAnnotation(SimpleWebServlet.class);
            var value = annotation.value();

            for (String url : value) {
                if (mapping.containsKey(url)) {
                    throw new Exception("Mapping: " + url + " is not valid. Servlet " + mapping.get(url) + " already has this mapping");
                } else {
                    mapping.put(url, servlets.get(i));
                }
            }
        }
        return mapping;
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
}
