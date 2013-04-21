package com.liaison.helloworld.server;

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
import java.util.HashSet;

// TODO move to framework
// TODO make component for initialization of routes

@Path("/dyn")
public class DynamicResource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResource.class);

    // curl -H "Content-Type: application/json" --data-asc80/hello-world-1.0.16-SNAPSHOT/rest/v1/dyn/

    public DynamicResource() {
        HashSet<String> routes = new HashSet<String>();
        try {
            InputStream is = this.getClass().getResourceAsStream("/dynamic-services.routes");
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);
            String line = null;

            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                logger.info("Line read:  " + line);
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error("Error processing request.", e);
            throw new RuntimeException(e);
        }
    }


    @Path("/{servicePath}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})

    public Response dyn(@PathParam("servicePath") String servicePath, JSONObject payload) {

        // TODO inject bfg
        // TODO better error handling
        // TODO if no payload / method and params missing, default to get
        // TODO params as list within json:  [ { "params": [a, b, c] } ]
        // TODO look for services off of classpath based on JVM property (allows to read from command line)
        // Need WADL?

        JSONObject response = new JSONObject();

        // use service in path to load javascript
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