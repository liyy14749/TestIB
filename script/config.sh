#!/bin/bash

# 载入java等环境变量
if [ -f "/root/.bash_profile" ]; then
    source "/root/.bash_profile";
fi

# 服务名称
PROJECT_NAME="IBCronServer"
PROJECT_NAME_JAR="${PROJECT_NAME}.jar"
CUR_TIME=$(date "+%Y-%m-%d %H:%M:%S");

# 项目目录
PROJECT_PATH="/opt/webserver/${PROJECT_NAME}"
if [ ! -d "${PROJECT_PATH}" ]; then
    mkdir -p ${PROJECT_PATH}
fi

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
elif [ "$1" == "-pre" ]; then
  ACTIVE="pre"
elif [ "$1" == "-test" ]; then
  ACTIVE="test"
fi

echo "ACTIVE: ${ACTIVE}"

