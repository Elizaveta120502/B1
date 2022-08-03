package com.test.task.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerProvider {
    private static final Logger LOG = LogManager.getLogger(LoggerProvider.class);

    public LoggerProvider() {
    }

    public static Logger getLOG() {
        return LOG;
    }
}
