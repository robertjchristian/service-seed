#!/bin/bash

export CATALINA_HOME="$PWD/apache-tomcat-7.0.39"
export TOMCAT_CMD="$CATALINA_HOME/bin/catalina.sh run"

export JAVA_OPTS="$JAVA_OPTS -Darchaius.deployment.applicationId=hello-netflix-oss"
export JAVA_OPTS="$JAVA_OPTS -Darchaius.deployment.environment=dev"
echo "Initial JAVA_OPTS=$JAVA_OPTS"

echo .
echo "Executing $TOMCAT_CMD"
echo .

$TOMCAT_CMD
