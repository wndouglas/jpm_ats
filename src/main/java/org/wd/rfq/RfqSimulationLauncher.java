package org.wd.rfq;

import org.wd.rfq.util.CustomLogFormatter;
import org.wd.rfq.util.SimulationCmdOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;

/**
 * The following class provides the entry point to the RFQ simulator.
 */
public final class RfqSimulationLauncher {
    public static void main(String[] args) throws IOException {
        SimulationCmdOptions simulationCmdOptions = SimulationCmdOptions.create(args);

        String logPath = simulationCmdOptions.getLogPath();
        boolean isConsoleLogEnabled = simulationCmdOptions.isConsoleLogEnabled();
        constructLogger(logPath, isConsoleLogEnabled);

        String configPath = simulationCmdOptions.GetConfigPath();
        RfqSimulator rfqSimulator = RfqSimulator.RfqSimulatorBuilder.buildFromJsonString(readFile(configPath));
        rfqSimulator.run();
    }

    public static void constructLogger(String fileLogPath, boolean isConsoleLogEnabled) throws IOException {
        LogManager.getLogManager().reset();
        Logger baseLogger = Logger.getGlobal();
        CustomLogFormatter formatter = new CustomLogFormatter();
        if (fileLogPath != null) {
            FileHandler fileHandler = new FileHandler(fileLogPath);
            baseLogger.addHandler(fileHandler);
            fileHandler.setFormatter(formatter);
        }
        if (isConsoleLogEnabled) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            baseLogger.addHandler(consoleHandler);
            consoleHandler.setFormatter(formatter);
        }
    }

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}

