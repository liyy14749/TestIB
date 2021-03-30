#!/bin/bash

source ./script/config.sh

echo "------------------------------ mvn install [start] ------------------------------"
mvn clean install -Dmaven.test.skip=true
echo "------------------------------ mvn install [end] ------------------------------"

echo "------------------------------ package distribute [start] ------------------------------"
\cp -f target/IBCronServer.jar ${PROJECT_PATH}
\cp -f script/*.sh ${PROJECT_PATH} && chmod +x ${PROJECT_PATH}/*.sh

ls -al ${PROJECT_PATH}
echo "------------------------------ package distribute [end] ------------------------------"
echo "distribute success."
