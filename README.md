service-seed
======

Fork of Netflix/karyon

history
=====

* Added apache-tomcat
 * Modified catalina.sh to include app and env properties
* Added a deploy task to build.gradle which copies the war to apache's webapps root
* Added a ./run.sh script to launch tomcat

to use
====

* From server-seed...
 * Start daemon for speed:  gradle --daemon
 * Create and deploy war:  gradle war deploy
 * Launch server:  ./run.sh

links
====

* Once tomcat is started...
 * Static REST service endpoint:  http://localhost:8080/hello-netflix-oss-1.0.6-SNAPSHOT/rest/v1/hello/to/newbee
 * Dynamic REST service endpoint:  http://localhost:8080/hello-netflix-oss-1.0.6-SNAPSHOT/rest/v1/hello/
 * Web admin console:  http://localhost:8077/adminres/webadmin/index.html
