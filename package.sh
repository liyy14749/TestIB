#!/bin/bash

echo "------------------------------ mvn install [start] ------------------------------"
mvn clean install -Dmaven.test.skip=true
echo "------------------------------ mvn install [end] ------------------------------"

echo "------------------------------ package distribute [start] ------------------------------"
project_root=/opt/webserver/IBCronServer
if [ ! -d "${project_root}" ]; then
    mkdir -p ${project_root}
fi

cp -f target/IBCronServer.jar ${project_root}
cp -f script/*.sh ${project_root} && chmod +x ${project_root}/*.sh

ls -al ${project_root}
echo "------------------------------ package distribute [end] ------------------------------"
echo "distribute success."
