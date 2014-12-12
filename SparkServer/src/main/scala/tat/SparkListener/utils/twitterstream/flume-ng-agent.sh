#!/bin/bash
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

# Name of the agnet
FLUME_AGENT_NAME=TwitterAgent

# Setting up a few defaults that can be later overrideen in /etc/default/flume-ng-agent
FLUME_DIR=/home/05/40031/apache-flume
FLUME_LOG_DIR=${FLUME_DIR}/logs
FLUME_CONF_DIR=${FLUME_DIR}/conf
FLUME_RUN_DIR=${FLUME_DIR}/run
#FLUME_HOME=/usr/lib/flume-ng
FLUME_USER=s0540031

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

FLUME_LOCK_DIR="${FLUME_DIR}/lock"
LOCKFILE="${FLUME_LOCK_DIR}/flume-ng-agent"
desc="Flume agent daemon"

#FLUME_CONF_FILE=${FLUME_CONF_FILE:-${FLUME_CONF_DIR}/flume.conf}
FLUME_CONF_FILE=${FLUME_CONF_DIR}/twitteragent.conf
EXEC_PATH=${FLUME_DIR}/bin/flume-ng
FLUME_PID_FILE=${FLUME_RUN_DIR}/flume-ng-agent-${FLUME_AGENT_NAME}.pid

# These directories may be tmpfs and may or may not exist
# depending on the OS (ex: /var/lock/subsys does not exist on debian/ubuntu)
for dir in "$FLUME_RUN_DIR" "$FLUME_LOCK_DIR"; do
  [ -d "${dir}" ] || install -d -m 0755 -o $FLUME_USER -g $FLUME_USER ${dir}
done

FLUME_SHUTDOWN_TIMEOUT=${FLUME_SHUTDOWN_TIMEOUT:-60}

start() {
  [ -x $exec ] || exit $ERROR_PROGRAM_NOT_INSTALLED

  checkstatus
  status=$?
  if [ "$status" -eq "$STATUS_RUNNING" ]; then
    exit 0
  fi

  log_success_msg "Starting $desc (flume-ng-agent): "
  #/bin/su -s /bin/bash -c "/bin/bash -c 'echo \$\$ > ${FLUME_PID_FILE} && exec ${EXEC_PATH} agent --conf $FLUME_CONF_DIR --conf-file $FLUME_CONF_FILE --name $FLUME_AGENT_NAME >>${FLUME_LOG_DIR}/flume.${FLUME_AGENT_NAME}.init.log 2>&1 ' &" $FLUME_USER
  /bin/bash -c "/bin/bash -c 'echo \$\$ > ${FLUME_PID_FILE} && exec ${EXEC_PATH} agent --conf $FLUME_CONF_DIR --conf-file $FLUME_CONF_FILE --name $FLUME_AGENT_NAME >>${FLUME_LOG_DIR}/flume.${FLUME_AGENT_NAME}.init.log 2>&1 ' &"
  RETVAL=$?
  [ $RETVAL -eq 0 ] && touch $LOCKFILE
  return $RETVAL
}

stop() {
  if [ ! -e $FLUME_PID_FILE ]; then
    log_failure_msg "Flume agent is not running"
    exit 0
  fi

  log_success_msg "Stopping $desc (flume-ng-agent): "

  FLUME_PID=`cat $FLUME_PID_FILE`
  if [ -n $FLUME_PID ]; then
    kill -TERM ${FLUME_PID} &>/dev/null
    for i in `seq 1 ${FLUME_SHUTDOWN_TIMEOUT}` ; do
      kill -0 ${FLUME_PID} &>/dev/null || break
      sleep 1
    done
    kill -KILL ${FLUME_PID} &>/dev/null
  fi
  rm -f $LOCKFILE $FLUME_PID_FILE
  return 0
}

restart() {
  stop
  start
}

checkstatus(){
  pidofproc -p $FLUME_PID_FILE java > /dev/null
  status=$?

  case "$status" in
    $STATUS_RUNNING)
      log_success_msg "Flume agent is running"
      ;;
    $STATUS_DEAD)
      log_failure_msg "Flume agent is dead and pid file exists"
      ;;
    $STATUS_DEAD_AND_LOCK)
      log_failure_msg "Flume agent is dead and lock file exists"
      ;;
    $STATUS_NOT_RUNNING)
      log_failure_msg "Flume agent is not running"
      ;;
    *)
      log_failure_msg "Flume agent status is unknown"
      ;;
  esac
  return $status
}

condrestart(){
  [ -e ${LOCKFILE} ] && restart || :
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
  *)
    echo $"Usage: $0 {start|stop|status|restart|try-restart|condrestart}"
    exit 1
esac

exit $RETVAL
