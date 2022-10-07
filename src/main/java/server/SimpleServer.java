package server;

import servlet.annotations.SimpleWebServlet;
import servlet.exeptions.SimpleServletException;
import servlet.http.SimpleHttpServlet;
import utils.ServerUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SimpleServer {
    private final String configFilePath;
    private final int port;
    private final List<Class<?>> servletClasses;
    private final List<SimpleHttpServlet> servlets;
    private final Map<String, SimpleHttpServlet> mapping;


    private final String servletsApiPath;


    public SimpleServer(String configFilePath, String servletsJarPath, String servletsApiPath) throws Exception {
        this.servletsApiPath = servletsApiPath;

        this.configFilePath = configFilePath;
        port = ServerUtils.getPortFromConfig(configFilePath);

        var classesFromJar = readClassesFromJar(servletsJarPath);

        servletClasses = readServletFromClasses(classesFromJar);
        servlets = readServlets();
        mapping = readMapping();
    }

    private List<Class<?>> readClassesFromJar(String servletsJarPath) throws IOException, ClassNotFoundException {
        List<Class<?>> classesFromJar = new ArrayList<>();

        JarFile jarFile = new JarFile(servletsJarPath);

        Enumeration<JarEntry> jarEntries = jarFile.entries();

        URLClassLoader cl = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + servletsJarPath + "!/"), new URL("jar:file:" + servletsApiPath + "!/")});

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }

            String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6).replace('/', '.');
            classesFromJar.add(cl.loadClass(className));
        }
        return classesFromJar;
    }

    private List<Class<?>> readServletFromClasses(List<Class<?>> classesFromJar) {
        List<Class<?>> servlets = new ArrayList<>();

        for (Class<?> aClass : classesFromJar) {
            if (Arrays.stream(aClass.getAnnotations()).anyMatch(elem -> elem.annotationType().equals(SimpleWebServlet.class)) &&
                    aClass.getSuperclass().equals(SimpleHttpServlet.class)) {
                servlets.add(aClass);
            }
        }
        return servlets;
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
