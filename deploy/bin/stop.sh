#!/bin/bash

# bootstrap class
MAIN_CLASS=com.weng.gateway.GatewayApplication

# basename of the logfile.
LOG_BASE_NAME=demonstrate

PACKAGE_HOME=$(cd "$(dirname "$0")";cd ..;pwd)

echo "-------------------------------------------"
echo "stopping server"

# find all the PIDs
PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
if [ -z "$PIDs" ]; then
    echo "service not running"
    exit 1
fi

if [ -n "$PIDs" ]; then
    for PID in $PIDs; do
        kill $PID
        echo "killing $PID"
    done
fi

# wait for 50s till all the process terminate
for i in 1 10; do
    PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
    if [ ! -n "$PIDs" ]; then
        echo "stop server success"
        echo -------------------------------------------
        break
    fi
    echo "stopping..."
    sleep 2
done

# if there still exists some processes, kill them forcely
PIDs=`jps -l | grep $MAIN_CLASS | awk '{print $1}'`
if [ -n "$PIDs" ]; then
    for PID in $PIDs; do
        kill -9 $PID
        echo "kill -9 $PID"
    done
fi

