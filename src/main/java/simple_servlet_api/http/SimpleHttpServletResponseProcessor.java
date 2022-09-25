package simple_servlet_api.http;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.*;

public class SimpleHttpServletResponseProcessor implements SimpleHttpServletResponse {
    private final ByteArrayOutputStream contentOutputStream;
    private final PrintWriter contentPrintWriter;
    private final Map<String, String> headersMap = new HashMap<>();
    private int status = STATUS_OK;

    private enum commonHeaders {
        CONNECTION("Connection"),
        CONTENT_TYPE("Content-type"),
        CONTENT_LENGTH("Content-Length");

        public final String name;

        commonHeaders(String s) {
            name = s;
        }
    }

    public SimpleHttpServletResponseProcessor() {
        this.contentOutputStream = new ByteArrayOutputStream();
        this.contentPrintWriter = new PrintWriter(contentOutputStream);

        setDefaultHeaderValues();
    }

    private void setDefaultHeaderValues() {
        headersMap.put(commonHeaders.CONNECTION.name, "close");
    }

    private String prepareStatus() {
        return "HTTP " + this.status;
    }

    @Override
    public OutputStream getOutputStream() {
        return contentOutputStream;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setContentType(String type) {
        String name = commonHeaders.CONTENT_TYPE.name;
        setHeader(name, String.valueOf(type));
    }

    @Override
    public void setContentLength(long length) {
        String name = commonHeaders.CONTENT_LENGTH.name;
        setHeader(name, String.valueOf(length));
    }

    @Override
    public void setHeader(String name, String value) {
        headersMap.put(name, value);
    }

    @Override
    public PrintWriter getWriter() {
        return contentPrintWriter;
    }

    public byte[] getBytes() {
        StringBuilder result = new StringBuilder();

        result.append(prepareStatus()).append("\r\n");


        contentPrintWriter.flush();

        byte[] contentBytes = contentOutputStream.toByteArray();


        if (!headersMap.containsKey(commonHeaders.CONTENT_LENGTH.toString())) {
            setContentLength(contentBytes.length);
        }

        for (Map.Entry<String, String> item : headersMap.entrySet()) {
            result.append(item.getKey()).append(": ").append(item.getValue()).append("\r\n");
        }

        result.append("\r\n");

        byte[] header = result.toString().getBytes();

        byte[] headerWithBody = header;
        if (contentBytes.length != 0) {
            headerWithBody = ArrayUtils.addAll(header, contentBytes);
        }
        return headerWithBody;
    }
}