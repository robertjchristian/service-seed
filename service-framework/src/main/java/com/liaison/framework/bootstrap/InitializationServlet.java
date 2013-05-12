package com.liaison.framework.bootstrap;

import com.netflix.blitz4j.LoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitializationServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(InitializationServlet.class);

    public void init(ServletConfig config) throws ServletException {
        logger.info("Initializing Blitz4J...");
        LoggingConfiguration.getInstance().configure();

    }
}
