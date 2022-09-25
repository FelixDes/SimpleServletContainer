package utils;

import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ServerUtils {
    private ServerUtils() {
    }

    public static List<String> getFileNamesFromBaseDir(String dirPath) {
        List<String> files = new ArrayList<>();
        File[] listOfFiles = new File(dirPath).listFiles();

        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    files.add(listOfFile.getName());
                }
            }
        }
        return files;
    }

    public static String getUrlFromConfig(String configFilePath) throws Exception {
        JSONObject jsonObject = new JSONObject(Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII));
        return jsonObject.getString("url");
    }
}
