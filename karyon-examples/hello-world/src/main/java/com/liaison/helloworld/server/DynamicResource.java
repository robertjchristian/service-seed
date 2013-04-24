package com.liaison.helloworld.server;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

// TODO move to framework
// TODO make component for initialization of routes

@Path("/dyn")
public class DynamicResource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResource.class);

    // Tomcat:  curl -H "Content-Type: application/json" --data-ascii "{}" http://localhost:8080/hello-world-1.0.16-SNAPSHOT/rest/v1/dyn/add
    // Jetty:   curl -H "Content-Type: application/json" --data-ascii "{}" http://localhost:8989/hello-world/rest/v1/dyn/add

    private HashMap<String, String> endpoints = new HashMap<String, String>();

    public DynamicResource() {
        HashSet<String> routes = new HashSet<String>();
        try {
            InputStream is = this.getClass().getResourceAsStream("/dynamic-services.routes");
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while((line = br.readLine()) != null) {
                if (line.trim().length() == 0) continue;
                logger.info("Line read:  " + line);

                // format expected is (ie): /dyn/math-service/[add|subtract] => /service-scripts/examples/math


                // now let's parse the line

                String s[] = line.split("=>");
                if (s.length != 2) {
                    logger.error("Expected:  url/[method(s)] mapped to script via \"=>\" symbol, (ie)\n/dyn/math-service/[add|subtract] => /service-scripts/examples/math");
                }

                String scriptLocation = s[1].trim() + ".js";
                logger.info("Service script location specified at: " + scriptLocation + "... checking on classpath...");
                InputStream in = this.getClass().getResourceAsStream(scriptLocation);
                if (in == null) {
                    logger.error("Service script not found at " + scriptLocation);
                    continue; // no reason to continue if we cannot locate the script
                } else {
                    logger.info("Script found on classpath at " + scriptLocation);
                }

                String baseUrl = s[0].trim().substring(0, s[0].indexOf('['));
                logger.info("Base url:  " + baseUrl);

                String operationList = s[0].substring(s[0].indexOf('[') + 1, s[0].indexOf(']'));
                logger.info("Operation list:  " + operationList);

                // here's where we add the endpoints
                for (String method : operationList.split("\\|")) {
                  String endpoint = baseUrl + method;
                  logger.info("Adding endpoint: " + endpoint + ", which will run the \"" + method + "\" function in " + scriptLocation);

                  // assumption:  endpoints are unique and point to a single script
                  endpoints.put(endpoint, scriptLocation);
                }

            }
        } catch (Exception e) {
            logger.error("Error processing request.", e);
            throw new RuntimeException(e);
        }
    }


   // this will be called if user sends
   // curl -H "Content-Type: application/json" --data-ascii "{}" http://localhost:8080/hello-world-1.0.16-SNAPSHOT/rest/v1/dyn/service/fooservice
   // curl -H "Content-Type: application/json" --data-ascii "{}" http://localhost:8080/hello-world-1.0.16-SNAPSHOT/rest/v1/dyn/service/fooservice

    @Path("/service/{servicePath}")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    // Friendly service indicating to client that JSON is expected (otherwise can't marshall)
    public Response dyn(@PathParam("servicePath") String servicePath, Object payload) {

        JSONObject response = new JSONObject();
        logger.error("Service expected JSON and received: " + payload.toString());

        try {
            response.put("Error", "U need to has JSON in yer rekwest plz.");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return Response.ok(response.toString()).build();
    }

    @Path("/service/{servicePath}/{operation}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dyn(@PathParam("servicePath") String servicePath, @PathParam("operation") String operation, JSONObject payload) {

        // TODO inject bfg
        // TODO better error handling
        // TODO if no payload / method and params missing, default to get
        // TODO params as list within json:  [ { "params": [a, b, c] } ]
        // TODO look for services off of classpath based on JVM property (allows to read from command line)
        // Need WADL?

        JSONObject response = new JSONObject();

        // use service in path to load javascript
        logger.info("Received request on dynamic service endpoint listener");
        logger.info("Service path is: " + servicePath);
        logger.info("Operation is: " + operation);


        // TODO fix this

        InputStream in = this.getClass().getResourceAsStream("/" + servicePath + ".js");
        InputStreamReader reader = new InputStreamReader(in);

        try {

            // setup engine
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");

            // evaluate (parse) the script first
            engine.eval(reader);

            String methodName = payload.getString("method");

            logger.info("invoking function:  " + methodName);
            Object result = ((Invocable) engine).invokeFunction(methodName, 3, 1);
            logger.info("result of invoking function:  " + result);

            response.put("result", result);

        } catch (Exception ex) {
            logger.error("Error processing request.", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(response.toString()).build();

    }

}
