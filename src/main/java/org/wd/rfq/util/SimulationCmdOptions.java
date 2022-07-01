package org.wd.rfq.util;

import static java.lang.System.exit;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Class to parse command line options.
 */
public final class SimulationCmdOptions {
    @Option(name = "-c", aliases = "--config", usage = "Config file absolute path.", required = true)
    private String configPath;

    @Option(name = "-l", aliases = "--logPath", usage = "Absolute path to store log results to file.")
    private String logPath;

    @Option(name = "-e", aliases = "--enableConsoleLog", usage = "Flag to print log results to std out.")
    private Boolean printLog;

    public static SimulationCmdOptions create(final String[] args) {
        SimulationCmdOptions options = new SimulationCmdOptions();
        final CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (final CmdLineException e) {
            System.out.println("Failed to parse command line arguments");
            e.printStackTrace();
            parser.printUsage(System.err);
            exit(-1);
        }
        return options;
    }

    public String GetConfigPath() {
        return configPath;
    }

    public boolean isConsoleLogEnabled() {
        if (printLog != null) {
            return printLog;
        }
        else {
            return false;
        }
    }

    public String getLogPath() {
        return logPath;
    }
}