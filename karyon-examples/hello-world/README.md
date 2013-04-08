hello-world
======

This is the simplest example of karyon using guice and jersey. This showcases, the following components:

* _karyon-extensions_: Uses guice extension.
* _eureka_: This is _disabled_ by default but can be enabled by setting property "com.netflix.karyon.eureka.disable" to "false"
 in [hello-world.properties](https://github.com/Netflix/karyon/blob/master/karyon-examples/hello-world/src/main/resources/hello-world.properties) or commenting out that property. This will mean populating the property file
 [eureka-client.properties](https://github.com/Netflix/karyon/blob/master/karyon-examples/hello-world/src/main/resources/eureka-client.properties), available in this example, with proper eureka endpoints in your environment.
* _karyon-admin-web_: Starts an embedded jetty server having an admin console available at http://localhost:8077/
* _karyon health check_: Provides a "always healthy" handler where the implementor can add any logic to signify health of
the application. The handler class is: [com.liaison.helloworld.server.health.HealthCheck](https://github.com/Netflix/karyon/blob/master/karyon-examples/hello-world/src/main/java/com/netflix/hellonoss/server/health/HealthCheck.java)

Running the example
===================

There are two ways of using this example, as is,

* Using gradle's jetty plugin: Run the command: _./gradlew :karyon-examples:hello-world:jettyRun_ from the base
directory of karyon (not from the hello-world module)

* Create a war file and deploy in the container of your choice: Run the command: _./gradlew :karyon-examples:hello-world:war_ from the base
  directory of karyon (not from the hello-world module). This will create the war file in
  the directory: karyon-examples/hello-world/build/libs/


What to see
===========

When running with gradle's jetty plugin, you should be able to hit the endpoints:

* http://localhost:8989/hello-world/rest/v1/hello/to/newbee: This will give you a JSON response:

{"Message":"Hello newbee from Netflix OSS"}

* http://localhost:8989/hello-world/rest/v1/hello:  This will give you a JSON response:

{"Message":"Hello Netflix OSS component!"}

* http://localhost:8077/

This will take you to the karyon admin user interface.
