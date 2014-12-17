#!/bin/bash
#
# Apache Spark needs to be installed in ~/spark
# The Application to run needs to be installed in ~/Twitter-Analytics-Tool/SparkServer/target/twitter-analytics-tool-0.0.2.jar
#
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Starts a Flume agent
#
# chkconfig: 345 90 10
# description: Flume agent
#
### BEGIN INIT INFO
# Provides:          flume-ng-agent
# Required-Start:    $remote_fs
# Should-Start:
# Required-Stop:     $remote_fs
# Should-Stop:
# Default-Start:     3 4 5
# Default-Stop:      0 1 2 6
# Short-Description: Flume agent
### END INIT INFO

. /lib/lsb/init-functions

# Setting up a few defaults that can be later overrideen in /etc/default/flume-ng-agent
GIT_PATH="${HOME}/Twitter-Analytics-Tool"
POM_PATH="${GIT_PATH}/SparkServer/pom.xml"
PACKAGE_VERSION=$(grep -oPm1 "(?<=<version>)[^<]+" ${POM_PATH})
JOB_CLASS="htwb.onepercent.SparkListener.App"
JOB_JARS="${HOME}/.m2/repository/org/json4s/json4s-native_2.10/3.2.10/json4s-native_2.10-3.2.10.jar"
JOB_PACKAGE="${GIT_PATH}/SparkServer/target/twitter-analytics-tool-${PACKAGE_VERSION}.jar"

SPARK_DIR="${HOME}/spark"
SPARK_LOG_DIR="${SPARK_DIR}/logs"
#SPARK_USER=s0540031

SPARK_MASTER="spark://hadoop03.f4.htw-berlin.de:60001"
SPARK_MASTER_OPTS='--executor-memory=32G --driver-memory 4G --driver-java-options "-Dspark.storage.memoryFraction=0.1"'

SPARK_LOCKFILE="${SPARK_DIR}/${JOB_CLASS}.lock"
SPARK_PIDFILE="${SPARK_DIR}/${JOB_CLASS}.pid"
EXEC_PATH="${SPARK_DIR}/bin/spark-submit"
desc="Spark JOB daemon"

# Autodetect JAVA_HOME if not defined
if [ -e /usr/libexec/bigtop-detect-javahome ]; then
  . /usr/libexec/bigtop-detect-javahome
elif [ -e /usr/lib/bigtop-utils/bigtop-detect-javahome ]; then
  . /usr/lib/bigtop-utils/bigtop-detect-javahome
fi

STATUS_RUNNING=0
STATUS_DEAD=1
STATUS_DEAD_AND_LOCK=2
STATUS_NOT_RUNNING=3

ERROR_PROGRAM_NOT_INSTALLED=5


# These directories may be tmpfs and may or may not exist
# depending on the OS (ex: /var/lock/subsys does not exist on debian/ubuntu)

SPARK_SHUTDOWN_TIMEOUT=${SPARK_SHUTDOWN_TIMEOUT:-60}

start() {
  [ -x $exec ] || exit $ERROR_PROGRAM_NOT_INSTALLED

  checkstatus
  status=$?
  if [ "$status" -eq "$STATUS_RUNNING" ]; then
    exit 0
  fi

  log_success_msg "Starting $desc: "
  /bin/bash -c "/bin/bash -c 'echo \$\$ > ${SPARK_PIDFILE} && exec ${EXEC_PATH} --class $JOB_CLASS --master $SPARK_MASTER $SPARK_MASTER_OPTS --jars $JOB_JARS $JOB_PACKAGE >>${SPARK_LOG_DIR}/${JOB_CLASS}.log 2>&1 ' &"
  RETVAL=$?
  [ $RETVAL -eq 0 ] && touch $SPARK_LOCKFILE
  return $RETVAL
}

stop() {
  if [ ! -e $SPARK_PIDFILE ]; then
    log_failure_msg "Spark Agent is not running"
    exit 0
  fi

  log_success_msg "Stopping $desc: "

  SPARK_PID=`cat $SPARK_PIDFILE`
  if [ -n $SPARK_PID ]; then
    kill -TERM ${SPARK_PID} &>/dev/null
    for i in `seq 1 ${SPARK_SHUTDOWN_TIMEOUT}` ; do
      kill -0 ${SPARK_PID} &>/dev/null || break
      sleep 1
    done
    kill -KILL ${SPARK_PID} &>/dev/null
  fi
  rm -f $SPARK_LOCKFILE $SPARK_PIDFILE
  return 0
}

restart() {
  stop
  start
}

checkstatus(){
  pidofproc -p $SPARK_PIDFILE java > /dev/null
  status=$?

  case "$status" in
    $STATUS_RUNNING)
      log_success_msg "Spark Agent is running"
      ;;
    $STATUS_DEAD)
      log_failure_msg "Spark Agent is dead and pid file exists"
      ;;
    $STATUS_DEAD_AND_LOCK)
      log_failure_msg "Spark Agent is dead and lock file exists"
      ;;
    $STATUS_NOT_RUNNING)
      log_failure_msg "Spark Agent is not running"
      ;;
    *)
      log_failure_msg "Spark Agent status is unknown"
      ;;
  esac
  return $status
}

debug(){
  cat ${SPARK_LOG_DIR}/${JOB_CLASS}.log | grep ^'### DEBUG ###' | tail -n 15
  return 0
}

update(){
  git --git-dir ${GIT_PATH}/.git pull
  mvn -q -f ${POM_PATH} clean package -DskipTests
  PACKAGE_VERSION=$(grep -oPm1 "(?<=<version>)[^<]+" ${POM_PATH})
  JOB_PACKAGE="${GIT_PATH}/SparkServer/target/twitter-analytics-tool-${PACKAGE_VERSION}.jar"
  restart
}

}condrestart(){
  [ -e ${SPARK_LOCKFILE} ] && restart || :
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  status)
    checkstatus
    ;;
  restart)
    restart
    ;;
  condrestart|try-restart)
    condrestart
    ;;
  debug)
    debug
    ;;
  update)
    update
    ;;
  *)
    echo $"Usage: $0 {start|stop|status|restart|try-restart|condrestart|debug|update}"
    exit 1
esac

exit $RETVAL
