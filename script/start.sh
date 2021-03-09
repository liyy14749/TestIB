#!/bin/bash

source ./config.sh $1 $2

# 启动服务
num=$(ps -ef | grep "${PROJECT_NAME_JAR}" | grep "${ACTIVE}" | grep "${CLIENT_ID}" | grep -v "grep" | wc -l)
if [ $num -gt 0 ]; then
  echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: is running." >> "${PROJECT_CONSOLE_LOG_PATH}/${PROJECT_NAME}.log"
else
  echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: start to running." >> "${PROJECT_CONSOLE_LOG_PATH}/${PROJECT_NAME}.log"
  nohup java -jar ${PROJECT_NAME_JAR} --spring.profiles.active=${ACTIVE} --spring.profiles.clientId=${CLIENT_ID} >> ${PROJECT_CONSOLE_LOG_PATH}/console.log 2>&1 &
  sleep 1
  tail -fn 50 ${PROJECT_CONSOLE_LOG_PATH}/console.log
fi

