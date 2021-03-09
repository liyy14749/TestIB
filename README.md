# IBCronServer
IBCronServer

. 安装 IB 的TWS API包【只需初始化】
    mvn install:install-file -Dfile=TwsApi_debug.jar -DgroupId=com.stock -DartifactId=twsapi -Dversion=1.0-SNAPSHOT -Dpackaging=jar
. 更新代码并打包发布
    cd ~/git/IBCronServer，如：cd /opt/webserver/git/IBCronServer
    git checkout master
    git pull --rebase
    sh ./package.sh
. tws启动命令
    cd ~/IBCronServer，如：cd /opt/webserver/IBCronServer
    sh start.sh -{active} {client_id}，如：sh start.sh -test 999
    sh stop.sh -{active} {client_id}
    sh restart.sh -{active} {client_id}
    参数释义
        {active}为：dev-开发环境|test-测试环境|prod-线上环境，默认dev
        {client_id}为客户端ID，默认0