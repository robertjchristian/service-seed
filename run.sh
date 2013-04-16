#!/bin/bash



echo ./karyon-examples/hello-world/build/libs/ to ./apache-tomcat-7.0.39/webapps


export CATALINA_HOME="$PWD/apache-tomcat-7.0.39"
export TOMCAT_CMD="$CATALINA_HOME/bin/catalina.sh run"

# deploy latest
export WAR_SOURCE="$PWD/karyon-examples/hello-world/build/libs/"
export WAR_DEST="$CATALINA_HOME/webapps"
echo "Deploying wars in $WAR_SOURCE to $WAR_DEST"
rm -r -f $WAR_DEST/*.war
cp $WAR_SRC/*.war $WAR_DEST/*.war

export JAVA_OPTS="-Darchaius.deployment.applicationId=hello-world"
export JAVA_OPTS="$JAVA_OPTS -Darchaius.deployment.environment=dev"
echo "Initial JAVA_OPTS=$JAVA_OPTS"

echo .
echo "Executing $TOMCAT_CMD"
echo .

$TOMCAT_CMD
