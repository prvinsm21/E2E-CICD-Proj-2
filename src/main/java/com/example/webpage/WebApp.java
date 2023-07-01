package com.example.webpage;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebApp {
    public static void main(String[] args) throws Exception {
        int port = 8080; // Change this if you want to use a different port

        Server server = new Server(port);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setResourceBase("src/main/webapp");
        webapp.setParentLoaderPriority(true);

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
