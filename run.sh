#!/bin/bash

export CATALINA_HOME="$PWD/apache-tomcat-7.0.39"
export TOMCAT_CMD="$CATALINA_HOME/bin/catalina.sh run"

# deploy latest
export RM_LOGS_CMD="rm $CATALINA_HOME/logs/*"
echo "Executing $RM_LOGS_CMD"
$RM_LOGS_CMD

export WAR_SOURCE="$PWD/karyon-examples/hello-world/build/libs/*.war"
export WAR_DEST="$CATALINA_HOME/webapps"
export RM_WAR_CMD="rm $WAR_DEST/*.war"
echo "Executing $RM_WAR_CMD"
$RM_WAR_CMD
export RM_FLATTENED_WAR_CMD = "$rm -r -f $WAR_DEST/*SNAPSHOT*"
$RM_FLATTENED_WAR_CMD

export DEPLOY_CMD="cp $WAR_SOURCE $WAR_DEST"
echo "Executing $DEPLOY_CMD"
$DEPLOY_CMD

export JAVA_OPTS="-Darchaius.deployment.applicationId=hello-world"
export JAVA_OPTS="$JAVA_OPTS -Darchaius.deployment.environment=dev"
echo "Initial JAVA_OPTS=$JAVA_OPTS"

echo "Executing $TOMCAT_CMD"
echo

$TOMCAT_CMD
