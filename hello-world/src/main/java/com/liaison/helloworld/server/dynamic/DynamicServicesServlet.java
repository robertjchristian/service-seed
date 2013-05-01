package com.liaison.helloworld.server.dynamic;

import com.google.common.io.Resources;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DynamicServicesServlet extends HttpServlet{





    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException{



        String path = request.getPathInfo();






        // for now, always expect service config here under classpath
        String rawServiceConfig = readFileFromClassPath("/dynamic-bindings.json");



        // make pretty
        String prettyServiceConfig = prettifyJSON(rawServiceConfig);

        PrintWriter out = response.getWriter();
        out.println("<html><title>Dynamic Services</title>");
        out.println("<body>");
        out.println("<h1>Dynamic Services</h1><hr>");

        out.println(path);

        // raw config (prettified)
        out.println("<h2>Raw configuration</h2>");
        out.println("<pre>" + prettyServiceConfig + "</pre>");

        out.println("</body>");
        out.println("</html>");
    }

    private String readFileFromClassPath(String path) {
        URL url = Resources.getResource(path);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving " + path + " from classpath.", e);
        }

    }

    private String prettifyJSON(String rawJSON) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(rawJSON);
        String pretty = gson.toJson(je);
        return pretty;
    }

}