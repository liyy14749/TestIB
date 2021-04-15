#!/bin/bash

# 载入java等环境变量
if [ -f "/root/.bash_profile" ]; then
  source "/root/.bash_profile"
fi

# 服务名称
PROJECT_NAME="IBCronServer"
PROJECT_NAME_JAR="${PROJECT_NAME}.jar"
CUR_TIME=$(date "+%Y-%m-%d %H:%M:%S")

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
active="dev"
client_id="7777"
server_config=("127.0.0.1:7496")
if [ "$1" == "-prod" ]; then
  active="prod"
  client_id="70001"
  server_config=("10.0.2.27:7496")
elif [ "$1" == "-pre" ]; then
  active="pre"
  client_id="10001"
  server_config=("10.0.2.27:7496")
elif [ "$1" == "-test" ]; then
  client_id="70001"
  active="test"
fi

echo "active: ${active}"
echo "client_id: ${client_id}"
echo "server_config: ${#server_config[@]}"

# 启动分组
group_num=3 # 当前股票分组数量
tmp_client_id=client_id
for ((i = 0; i < group_num; i++)); do
  tmp_client_id=$((client_id + i))
  CONFIG[$i]="${tmp_client_id}:stock_static_symbol_us_${i},stock_static_symbol_hk_${i}"
done
tmp_client_id=$((client_id + i))
CONFIG[$i]="${tmp_client_id}:stock_static_symbol_ind"


server_config_len=${#server_config[@]}
for i in "${!CONFIG[@]}"; do
  mod=`expr $i % $server_config_len`
  CONFIG[$i]="${active}:${server_config[$mod]}:${CONFIG[$i]}"
  echo "CONFIG: ${CONFIG[$i]}"
done

