set CATALINA_HOME="%cd%/apache-tomcat-7.0.39"
set TOMCAT_CMD="%CATALINA_HOME%/bin/catalina.sh run"

set JAVA_OPTS="%JAVA_OPTS% -Darchaius.deployment.applicationId=hello-world"
set JAVA_OPTS="%JAVA_OPTS% -Darchaius.deployment.environment=dev"
echo "Initial JAVA_OPTS=%JAVA_OPTS%"

echo .
echo "Executing %TOMCAT_CMD%"
echo .

%TOMCAT_CMD%
