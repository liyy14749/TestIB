#!/bin/bash

source ./config.sh $1 $2

# 启动服务
num=$(ps -ef | grep "${PROJECT_NAME_JAR}" | grep "${ACTIVE}" | grep "${CLIENT_ID}" | grep -v "grep" | wc -l)
if [ $num -gt 0 ]; then
  ./spring-boot.sh stop ${PROJECT_NAME_JAR}
  echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: to stop." >> "${PROJECT_CONSOLE_LOG_PATH}/${PROJECT_NAME}.log"
else
  echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: not running." >> "${PROJECT_CONSOLE_LOG_PATH}/${PROJECT_NAME}.log"
fi