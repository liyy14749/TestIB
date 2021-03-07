#!/bin/bash
git pull
mvn install -Dmaven.test.skip=true
dirname=$1
if [ x"$1" = x ]; then
  dirname='prod'
fi
echo ${dirname}
mkdir -p /opt/webserver/IBCronServer/
cp target/IBCronServer.jar /opt/webserver/IBCronServer/
cp script/restart.sh /opt/webserver/IBCronServer/ && chmod +x /opt/webserver/IBCronServer/restart.sh
cp script/spring-boot.sh /opt/webserver/IBCronServer/ && chmod +x /opt/webserver/IBCronServer/spring-boot.sh
cp script/${dirname}/start.sh /opt/webserver/IBCronServer/ && chmod +x /opt/webserver/IBCronServer/start.sh
