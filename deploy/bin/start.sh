#!/bin/bash

#启动类
MAIN_CLASS=com.weng.gateway.GatewayApplication

# root of the package
PACKAGE_HOME=$(cd "$(dirname "$0")";cd ..;pwd)


# check whether the program has been started
#PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
#if [ -n "${PIDs}" ]; then
#    echo "failed to start. The program is already running. PID:${PIDs}"
#    exit 1
#fi

# classpath
CLASSPATH="${PACKAGE_HOME}/lib/*:${PACKAGE_HOME}/conf"

# JVM startup parameters
JAVA_OPTS="-server -Xms10M -Xmx2048M -Duser.timezone=Asia/Shanghai -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${PACKAGE_HOME}"

# basename of the logfile.
LOG_BASE_NAME=demonstrate


# use the *java* residing in JAVA_HOME
export _EXECJAVA="$JAVA_HOME/bin/java"

LOG_DIR=${PACKAGE_HOME}/log
LOG_PATH=${LOG_DIR}/all.log
mkdir -p $LOG_DIR

echo "-------------------------------------------"
echo "starting server"
echo

echo "MAIN_CLASS:"
echo "      [${MAIN_CLASS}]"
echo

echo "JVM parameter:"
echo "      [${JAVA_OPTS}]"
echo

echo "LOG_PATH:"
echo "      [${LOG_PATH}]"
echo
echo "-------------------------------------------"

# start with *nohup* to prevent the OS from killing our program after logout
nohup $_EXECJAVA $JAVA_OPTS -classpath $CLASSPATH $MAIN_CLASS  >${LOG_PATH} 2>&1 &

tail -f ${LOG_PATH}
