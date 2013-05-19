<h2>Service Seed</h2>

<h3>About</h3>
Service seed is a starting point for service implementations.  It is a fork of the Netflix/karyon project found on github, with additional features.  The purpose of the seed is to enable homogenous, SOA-focused webservice development.  Out of the box, the seed offers support for:

* Java webservice development (Jersey stack and working examples)
* Dynamic webservice development (runtime-deployment of Javascript-defined webservices)
* Database/ORM integration (via JPA)
* Asynchronous logging
* Properties management (via Netflix Archaeus)
* JMX monitoring (made easy) and metrics reporting (via Netflix Servo)
* Framework-level monitoring (bytes in/out, CPU high water mark, number of requests handled per endpoint, etc)
* Auditing
 * PCI and Hippa requirements are modeled within the seed framework
 * Custom auditing appender
* Administrative console
 * Every seed exposes an administrative console on port 8077, regardless of deployment/container configuration.

<h3>Pre-requisites</h3>
* Gradle >= 1.4
* JDK >= 6 (note JDK currently breaks Cobertura)

<h3>To run</h3>

* From the project root, "gradle jettyRun"
** Checkout the admin console at http://localhost:8077
** Checkout the example REST services at http://localhost:8989/hello-world/v1/
*** /hello - example of static service (no parameters)
*** /hello/to/world - example of dynamic query parameter
** Checkout the dynamic services landing page
*** localhost:8989/dyn

<h3>To deploy</h3>
* From the project root, "gradle war"
* Then copy (deploy) to your container of choice

<h3>Developing a concrete service from the seed</h3>
There are currently four modules within the seed:
* karyon-admin-web
* karyon-admin
* service-framework
* service-implementation

The first two, for admin, are only coupled to the project as source as an artifact of the karyon project, and have not been moved out because they will likely be modified by this project within the near to mid term.

The service-framework module, like the admin modules, contain the homogenous functionality like auditing, servlet filter exception processing, system/generic metrics, and dynamic services.  Likely, for a given implementation, there will be no need to modify this module.

The service-implementation module is the module everyone will be concerned with.  Out of the box, it defines a hello-world project with two endpoints (one static and one that takes a template parameter), and a simple health check servlet.  This module is meant to be refactored into a useful service implementation.

<h3>Example</h3>

Let's say you wanted to develop a service called "math" that multiplies two template parameters and returns the result.  

<h5>First step, barebones implementation</h5>
* Get the project, (ie) git clone github:robertjchristian/service-seed
 * As a sanity check, perform the steps in "to run" outlined above`
* nano service-implementation/src/main/java/com/liaison/service/HelloworldResource
 * Copy/paste the helloTo service, including annotations
 * Change path from "to/{name}" to "multiply/{a}/{b}"
 * Change method name to multiply, and the single "name" path parameter interface to take a and b "int" parameters instead
 * Change the response.put call to return the value of a*b instead.

That's it!

<h5>Second step, productize</h5>

Realistically you will want to productize your service, which basically means fixing up the namespace from hello-world to something more specific to your particular service.  These steps outline that process.  Note that scaffolding is on the near-term roadmap, and will make most or all of these steps obsolete:

* Edit ./service-implementation/build.gradle
 * Change project.war.basename from "hello-world" to "math"
 * Change System.setProperty("archaius.deployment.applicationId", "hello-world") to System.setProperty("archaius.deployment.applicationId", "math")
* Rename ./service-implementation/src/main/resources/hello-world* to use prefix "math" instead.  
* Refactor your package namespace as desired 
 * Make sure to update math.properties to reflect any high-level namespace change, ie com.netflix.karyon.server.base.packages=com.liaison
to com.netflix.karyon.server.base.packages=com.acme





