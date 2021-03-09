#!/bin/bash

# 服务名称
PROJECT_NAME="IBCronServer"
PROJECT_NAME_JAR="${PROJECT_NAME}.jar"
CUR_TIME=$(date "+%Y-%m-%d %H:%M:%S");

# 日志目录
PROJECT_LOG_PATH="/opt/weblogs/${PROJECT_NAME}"
PROJECT_CONSOLE_LOG_PATH="${PROJECT_LOG_PATH}/console"
if [ ! -d "${PROJECT_CONSOLE_LOG_PATH}" ]; then
  mkdir -p ${PROJECT_CONSOLE_LOG_PATH}
fi

# 启动环境，默认dev
ACTIVE="dev"
if [ "$1" == "-prod" ]; then
  ACTIVE="prod"
elif [ "$1" == "-test" ]; then
  ACTIVE="test"
fi
echo "ACTIVE: ${ACTIVE}"

# 客户端ID
CLIENT_ID=0
if [ x"$2" != x ]; then
  CLIENT_ID=$2
fi
echo "CLIENT_ID: ${CLIENT_ID}"
