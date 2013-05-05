package com.liaison.helloworld.server.dynamic;

import com.google.gson.Gson;
import com.liaison.framework.util.ServiceUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DynamicServicesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // TODO:  Probably shouldn't build and load every time, but don't want to cache and hold indefinitely either
        // needs to be updatable in real-time
        // for now, always expect service config here under classpath
        String rawBindingConfiguration = ServiceUtils.readFileFromClassPath("/dyn/bindings.json");

        // parse service configuration
        DynamicBindings serviceBindings = new Gson().fromJson(rawBindingConfiguration, DynamicBindings.class);

        // ie if http://localhost:8989/hello-world/dyn/foo/bar/baz, then /foo/bar/baz
        String path = request.getPathInfo();

        // if no path info, show landing page information
        if (path == null || path.equals("/")) {

            // TODO: Probably shouldn't build every time...
            String html = DynamicServicesWebPageBuilder.buildHTMLPageFromBindings(rawBindingConfiguration, serviceBindings);
            response.getWriter().print(html);

            return;
        }

    }

}