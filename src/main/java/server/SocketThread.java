package server;

import simple_servlet_api.http.SimpleHttpServlet;
import simple_servlet_api.http.SimpleHttpServletRequestProcessor;
import simple_servlet_api.http.SimpleHttpServletResponseProcessor;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class SocketThread implements Runnable {
    private final InputStream is;
    private final OutputStream os;
    private Map<String, SimpleHttpServlet> servletsMapping;

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
                servletsMapping.get(mapping).doGet(request, response);
            }
            os.write(response.getBytes());
            os.flush();
            os.close();

            br.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

//    private List<String> getStringsFromRequest() {
//        List<String> result = new ArrayList<>();
//        try () {
//            String line;
//            while ((line = br.readLine()) != null) {
//                result.add(line);
//            }
//            return result;
//        } catch (RuntimeException | IOException e) {
//            throw new RuntimeException("Something went wrong with socket input stream. Perhaps socket connection was lost");
//        }
//    }


//    private void writeResponseForPath(String path) throws IOException {
//        byte[] html;
//        byte[] header;
//
//        if (Objects.equals(path, "")) {
//            html = getHomePageHtmlBytes();
//            header = getOkHeaderBytes(html);
//        } else if (getFileNamesFromBaseDir().contains(path)) {
//            File f = new File(fileUrl + path);
//            html = Files.readAllBytes(f.toPath());
//            header = getOkHeaderBytes(html);
//        } else {
//            html = getNotFoundPageHtmlBytes();
//            header = getNotFoundHeaderBytes(html);
//        }
//        try {
//            os.write(header);
//            os.write(html);
//            os.flush();
//        } catch (IOException e) {
//            throw new RuntimeException("Something went wrong with socket output stream. Perhaps socket connection was lost");
//        }
//    }

//    private byte[] getHomePageHtmlBytes() {
//        StringBuilder filesHtml = new StringBuilder();
//        for (String file : getFileNamesFromBaseDir()) {
//            filesHtml.append(String.format("<a href=\"%s\">%s</a><br>", file, file));
//        }
//        String html = "<html><head><title>Home page</title></head><body style=\"font-size: 30px\"><h1>Files available:</h1>" + filesHtml + "</body></html>";
//        return html.getBytes();
//    }
//
//    private byte[] getNotFoundPageHtmlBytes() {
//        String html = "<html><body style=\"background-color: #000; color: #fff; font-size: 30px\">404 File Not Found</body></html>";
//        return html.getBytes();
//    }
//
//    private byte[] getOkHeaderBytes(byte[] content) {
//        String header = "HTTP 200 OK\r\n" +
//                "Content-Length: " + content.length + "\r\n" +
//                "Connection: close\r\n\r\n";
//        return header.getBytes();
//    }
//
//    private byte[] getNotFoundHeaderBytes(byte[] content) {
//        String header = "HTTP 404 Not Found\r\n" +
//                "Content-Length: " + content.length + "\r\n" +
//                "Connection: close\r\n\r\n";
//        return header.getBytes();
//    }
//
//    private List<String> getFileNamesFromBaseDir() {
//        List<String> files = new ArrayList<>();
//        File[] listOfFiles = new File(fileUrl).listFiles();
//
//        if (listOfFiles != null) {
//            for (File listOfFile : listOfFiles) {
//                if (listOfFile.isFile()) {
//                    files.add(listOfFile.getName());
//                }
//            }
//        }
//        return files;
//    }
}