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
CLIENT_ID="1000"
CLIENT_HOST="127.0.0.1"
CLIENT_PORT="7496"
if [ "$1" == "-prod" ]; then
  ACTIVE="prod"
  CLIENT_HOST="10.0.2.25"
  CLIENT_PORT="40001"
elif [ "$1" == "-test" ]; then
  ACTIVE="test"
fi
echo "ACTIVE: ${ACTIVE}"

# 客户端ID
if [ x"$2" != x ]; then
  CLIENT_ID=$2
fi
echo "CLIENT_ID: ${CLIENT_ID}"

# 盈透客户端 HOST
if [ x"$3" != x ]; then
  CLIENT_HOST=$3
fi
echo "CLIENT_HOST: ${CLIENT_HOST}"

# 盈透客户端 PORT
if [ x"$4" != x ]; then
  CLIENT_PORT=$4
fi
echo "CLIENT_PORT: ${CLIENT_PORT}"
