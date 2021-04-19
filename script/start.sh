#!/bin/bash

source ./config.sh $1

for item in ${CONFIG[@]}; do
  active=$(echo ${item} | awk -F ':' '{print $1}')
  server_host=$(echo ${item} | awk -F ':' '{print $2}')
  server_port=$(echo ${item} | awk -F ':' '{print $3}')
  client_id=$(echo ${item} | awk -F ':' '{print $4}')
  redis_key=$(echo ${item} | awk -F ':' '{print $5}')

  time_day=$(date "+%Y%m%d")
  log_path="${PROJECT_CONSOLE_LOG_PATH}/${time_day}/${server_host}/${client_id}"
  if [ ! -d "${log_path}" ]; then
    mkdir -p ${log_path}
  fi

  # 启动服务
  num=$(ps -ef | grep "${PROJECT_NAME_JAR}" | grep "${active}" | grep "${client_id}" | grep -v "grep" | wc -l)
  if [ $num -gt 0 ]; then
    echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: is running." >>"${log_path}/${PROJECT_NAME}.log"
  else
    echo "[${CUR_TIME}]-${PROJECT_NAME_JAR}: start to running." >>"${log_path}/${PROJECT_NAME}.log"

    time_hour=$(date "+%H%M")
    log_path="${log_path}/${time_hour}.log"

    nohup java -jar ${PROJECT_NAME_JAR} \
      --spring.profiles.active=${active} \
      --my.ib.server.clientId=${client_id} \
      --my.ib.server.host=${server_host} \
      --my.ib.server.port=${server_port} \
      --my.stock.redisKey=${redis_key} \
      >>${log_path} 2>&1 &
  fi
done
