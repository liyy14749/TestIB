#!/bin/bash

source ./config.sh $1

# 关闭服务
for item in ${CONFIG[@]}; do
  active=`echo ${item} | awk -F ':' '{print $1}'`
  server_host=`echo ${item} | awk -F ':' '{print $2}'`
  client_id=`echo ${item} | awk -F ':' '{print $4}'`

  time_day=$(date "+%Y%m%d")
  log_path="${PROJECT_CONSOLE_LOG_PATH}/${time_day}/${server_host}/${client_id}"
  if [ ! -d "${log_path}" ]; then
    mkdir -p ${log_path}
  fi

  num=$(ps -ef | grep "${PROJECT_NAME_JAR}" | grep "${active}" | grep "${client_id}" | grep -v "grep" | wc -l)
  if [ $num -gt 0 ]; then
    ps -ef | grep "${PROJECT_NAME_JAR}" | grep "${client_id}" | grep "${client_id}" | grep -v "grep" | awk '{print $2}' | xargs kill -9
    echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: to stop." >>"${log_path}/${PROJECT_NAME}.log"
  else
    echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: not running." >>"${log_path}/${PROJECT_NAME}.log"
  fi
done
