com.liaison.service-seed
======

Fork of Netflix/karyon...

Karyon can be thought of as the nucleus of a blueprint that contains the following main ingredients.

Bootstrapping , Libraries and Lifecycle Management (via NetflixOSS's Governator)
Runtime Insights and Diagnostics (via built in Admin Console)
Pluggable Web Resources (via JSR-311 and Jersey)
Cloud Ready

history
=====

* Added apache-tomcat
 * Added a manager-gui role (user/pass=admin/admin)
* Added a deploy task to build.gradle which copies the war to apache's webapps root
* Added ./run.sh and ./run.cmd scripts to launch tomcat
* Modified gradle.properties to use daemon
* Changed namespace of hello-world

to use
====

* From server-seed...
 * Start daemon for speed:  gradle --daemon
 * Create and deploy war:  gradle war deploy
 * Launch server:  ./run.sh

links
====

* Once tomcat is started...
 * Static REST com.liaison.service endpoint:  http://localhost:8080/hello-world-1.0.6-SNAPSHOT/rest/v1/hello/to/newbee
 * Dynamic REST com.liaison.service endpoint:  http://localhost:8080/hello-world-1.0.6-SNAPSHOT/rest/v1/hello/
 * Web admin console:  http://localhost:8077/adminres/webadmin/index.html
