package com.liaison.helloworld.server.dynamic;

import com.google.common.io.Resources;
import com.google.common.base.Charsets;
import com.google.gson.*;

import java.io.*;
import java.net.URL;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DynamicServicesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // for now, always expect service config here under classpath
        String rawServiceConfig = readFileFromClassPath("/dyn/bindings.json");

        // parse service configuration
        DynamicBindings serviceBindings = new Gson().fromJson(rawServiceConfig, DynamicBindings.class);

        // ie if http://localhost:8989/hello-world/dyn/foo/bar/baz, then /foo/bar/baz
        String path = request.getPathInfo();

        // if no path info, show landing page information
        if (path == null || path.equals("/")) {

            // read landing page template
            String template = readFileFromClassPath("/dyn/landing.template");

            // swap raw pretty config token
            template = template.replace("{{rawConfigJSON}}", prettifyJSON(rawServiceConfig));

            // build and swap parsed section

            String s = "";
            for (DynamicBinding db : serviceBindings.bindings) {
               s += db.toHTML();
            }
            template = template.replace("{{parsedConfiguration}}", s);




            response.getWriter().print(template);

            return;
        }

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