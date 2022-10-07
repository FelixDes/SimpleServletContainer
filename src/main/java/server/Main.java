package server;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("c")
                .longOpt("config_path")
                .hasArg(true)
                .desc("path to config file (or use --config_path)")
                .required(true)
                .build());
        options.addOption(Option.builder("s")
                .longOpt("servlets_path")
                .hasArg(true)
                .desc("path to servlets jar file (or use --servlets_path)")
                .required(true)
                .build());
        options.addOption(Option.builder("api")
                .longOpt("servlets_api")
                .hasArg(true)
                .desc("path to servlets api jar file (or use --servlets_api)")
                .required(true)
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        cmd = parser.parse(options, args);

        String configPath = cmd.getOptionValue("c");
        String servletsPath = cmd.getOptionValue("s");
        String servletsApiPath = cmd.getOptionValue("api");
// -c "/home/felix/_Programming/Idea_Projects/OOP/OOP2/src/main/resources/config/sever_config.json" -s "/home/felix/Рабочий стол/Servlets-1.0-SNAPSHOT.jar" -api "/home/felix/_Programming/Idea_Projects/OOP/OOP2/jars/javax.servlet-api-1.0.jar"
        SimpleServer ss = new SimpleServer(configPath, servletsPath, servletsApiPath);
        ss.run();
    }
}