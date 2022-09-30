package utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ServerUtils {
    private ServerUtils() {
    }

    public static List<String> getFileNamesFromBaseDir(String dirPath) {
        try (var st = Files.list(Paths.get(dirPath))) {
            return st.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUrlFromConfig(String configFilePath) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.getString("url");
    }

    public static int getPortFromConfig(String configFilePath) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.getInt("port");
    }
}
