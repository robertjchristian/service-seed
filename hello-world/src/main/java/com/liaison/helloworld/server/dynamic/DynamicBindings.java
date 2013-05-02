package com.liaison.helloworld.server.dynamic;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.net.URL;
import java.util.List;

/**
 * POJO representing dynamic service bindings
 *
 * Author: Rob
 * Date: 5/1/13
 * Time: 4:26 PM
 */

class DynamicBinding {

    public DynamicBinding(About about, Operation[] operations, String baseURI, String scriptLocation) {
        this.about = about;
        this.operations = operations;
        this.baseURI = baseURI;
        this.scriptLocation = scriptLocation;
    }

    public About about;
    public Operation[] operations;
    public String baseURI;
    public String scriptLocation;

    // TODO move this to utils
    private String readFileFromClassPath(String path) {
        URL url = Resources.getResource(path);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error resolving " + path + " from classpath.", e);
        }
    }


    // TODO move landing page builder
    public String toHTML() {

        String parsed = readFileFromClassPath("/dyn/parsed.template");
        parsed = parsed.replace("{{binding-title}}", "Binding");
        parsed = parsed.replace("{{written-by}}", about.author);
        parsed = parsed.replace("{{description}}", about.description);
        parsed = parsed.replace("{{base-uri}}", baseURI);
        parsed = parsed.replace("{{script-location}}", scriptLocation);

        StringBuilder operationSB = new StringBuilder();
        for (Operation o : operations) {

            String allowedMethods = "";
            for (String s : o.allowedMethods) {
                allowedMethods += s + " | ";
            }
            String operationTemplate = readFileFromClassPath("/dyn/operation-rows.template");
            operationTemplate = operationTemplate.replace("{{operation-url}}", o.operationUrl);
            operationTemplate = operationTemplate.replace("{{allowed-http-methods}}", allowedMethods);

            operationSB.append(operationTemplate);
        }

        parsed = parsed.replace("{{operation-rows}}", operationSB.toString());

        return parsed;

    }


}

class About {
    public About(String author, String description) {
        this.author = author;
        this.description = description;
    }

    public String author;
    public String description;
}

class Operation {
    public Operation(String operationUrl, String[] allowedMethods) {
        this.operationUrl = operationUrl;
        this.allowedMethods = allowedMethods;
    }

    public String operationUrl;
    public String[] allowedMethods;
}

public class DynamicBindings {

    public DynamicBindings(DynamicBinding[] bindings) {
        this.bindings = bindings;
    }

    public DynamicBinding[] bindings;

    // build dummy object to gen initial json
    // DynamicBindings configuration = DynamicBindings.buildMockBindings();
    // Gson gson = new Gson();
    // String json = gson.toJson(configuration);

    public static DynamicBindings buildMockBindings() {
        Operation[] operations= new Operation[] {
                new Operation("multiply/{a}/{b}", new String[] { "GET", "POST" }),
                new Operation("divide/{a}/{b}", new String[] { "GET", "POST" })
        };
        About a = new About("rchristian@liaison", "simple math service");
        DynamicBinding binding = new DynamicBinding(a,  operations, "/v1/math", "classpath://scripts/foo.js");
        DynamicBinding[] bindings = new DynamicBinding[]{ binding, binding };
        DynamicBindings db = new DynamicBindings(bindings);
        return db;
    }

}
