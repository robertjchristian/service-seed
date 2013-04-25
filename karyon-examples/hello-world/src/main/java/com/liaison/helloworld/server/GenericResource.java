package com.liaison.helloworld.server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liaison.helloworld.core.HelloBean;

@Path("/dynamic")
public class GenericResource {

    private static final Logger logger = LoggerFactory.getLogger(GenericResource.class);

	// GET verb
	// ===========================================
	// {service} maps to a js file
	// {method} maps to a js method in that file
	// ===========================================

    @GET
	@Path("/{service}/{method}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
	public Response get(
			@PathParam("service") String service,
			@PathParam("method") String method,
			@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponse) {
		return (Response) dynamicGet(service, method, servletRequest, servletResponse);
	}
	
	// POST verb
	// ===========================================
	// {service} maps to a js file
	// {method} maps to a js method in that file
	// ===========================================

	@POST
	@Path("/{service}/{method}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
	public  Response multipart(
			@PathParam("service") String service,
			@PathParam("method") String method,
			@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponse) {
		return (Response) dynamicPost(service, method, servletRequest, servletResponse);
	}
	
	@POST
	@Path("/{service}/{method}")
	@Produces("multipart/related, multipart/mixed, multipart/form-data")
	@Consumes("multipart/related, multipart/mixed, multipart/form-data")
	public  MultipartBody multipart(
			@PathParam("service") String service,
			@PathParam("method") String method,
			MultipartBody body,
			@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponse) {
		return (MultipartBody) dynamicPost(service, method, servletRequest, servletResponse, body);
	}
	
	/**
	 * All GET endpoints feed into this method.
	 * 
	 * This demonstrates variable serialization
	 * 
	 * @param service
	 * @param method
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 */
    protected Object dynamicGet(
    		String service,
    		String method,
			@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponse) 
    {
    	String acceptContentType = servletRequest.getHeader("Accept");
    	
    	MediaType responseMediaType = null;
    	if (acceptContentType.toLowerCase().contains("application/json")) {
    		responseMediaType = MediaType.valueOf("application/json");    		
    	} else {
    		// default to XML for better backward compatibility
    		responseMediaType = MediaType.valueOf("application/xml");    		
    	}
    	
    	javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
    	
    	@SuppressWarnings("unchecked")
		Enumeration<String> headerNames = servletRequest.getHeaderNames();
    	for (int i = 0; headerNames.hasMoreElements(); i++) {
    		String name = headerNames.nextElement();
    		@SuppressWarnings("unchecked")
			Enumeration<String> headerValues = servletRequest.getHeaders(name);
    		// do something interesting with headers
    	}
    	
    	@SuppressWarnings("unchecked")
		Map<String, String> parameters = servletRequest.getParameterMap();

    	String name = parameters.get("name");
    	HelloBean helloBean = new HelloBean();
    	helloBean.setName(name);
    	helloBean.setGreeting("Hello " + helloBean.getName() + "!!!");
    	
    	// This is where the magic happens in serializing to JSON or XML 
    	// based on the standard "Accept" header
    	return Response.ok(helloBean, responseMediaType).build();
    }
	
	/**
	 * All POST endpoints feed into this method.
	 * 
	 * This demonstrates invoking a javascript method
	 * 
	 * @param servletRequest
	 * @param servletResponset
	 * @param body				- this will be null unless the POST is multipart
	 * @return
	 */
    protected Object dynamicPost(
    		String service,
    		String method,
    		@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponset,
			MultipartBody body) {
    	JSONObject response = null;
        try {
        	// unpack request context objects
        	String contentType = servletRequest.getContentType();
        	String acceptContentType = servletRequest.getHeader("Accept");
        	
        	javax.servlet.http.Cookie[] cookies = servletRequest.getCookies();
        	
        	@SuppressWarnings("unchecked")
			Enumeration<String> headerNames = servletRequest.getHeaderNames();
        	for (int i = 0; headerNames.hasMoreElements(); i++) {
        		String name = headerNames.nextElement();
        		@SuppressWarnings("unchecked")
				Enumeration<String> headerValues = servletRequest.getHeaders(name);
        		// do something interesting with headers
        	}
        	
        	@SuppressWarnings("unchecked")
			Map<String, String> parameters = servletRequest.getParameterMap();
        	
        	boolean isMultipartRequest = (body != null);
        	if (!isMultipartRequest) {
        		response = new JSONObject();
        		
                response.put("Service", service);
                response.put("Method", method);

                // read js
                // TODO - handle file not found
                InputStream in = this.getClass().getResourceAsStream("/" + service + ".js");
                InputStreamReader reader = new InputStreamReader(in);

                // setup engine
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");

                // inject bindings
                //Bindings bindings = jsEngine.createBindings();
                //bindings.put("_returnValue", null);

                try {

                    // evaluate the script
                    engine.eval (reader);

                    // TODO inject bfg

                    logger.error("Running JS...");

                    Object ret = ((Invocable) engine).invokeFunction(method, "3", "1");

                    logger.error("retVal is " + ret);

                    response.put("ret", ret);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return Response.ok(response.toString()).build();
        	} else {
        		// TODO - implement the multipart handling prior to javascript invoke
        		return null;
        	}
        	
        } catch (JSONException e) {
            logger.error("Error creating json response.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }	
    
	// non-mime convenience overlaod for the previous method
    protected Object dynamicPost(
    		String service,
    		String method,
			@Context HttpServletRequest servletRequest, 
			@Context HttpServletResponse servletResponse) {
    	return dynamicPost(service, method, servletRequest, servletResponse, null);
    }
    
    
}