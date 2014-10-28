#!/bin/bash
FLUME_VERSION="1.5.0.1"
SCRIPT_PATH=$(readlink -f "$0")
BASENAME="/"`basename $0`
SCRIPT_DIR=${SCRIPT_PATH%$BASENAME}

if ! type "java" > /dev/null; then
	echo "Please install Java first";
	exit 1;
fi


if ! type "mvn" > /dev/null; then
	echo "Please install Maven first";
	exit 2;
fi

JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
JAVA_HOME=${JAVA_HOME%/}

### Change to Homedirectory
cd ~/

### Get Apache Flume
wget http://apache.lauf-forum.at/flume/$FLUME_VERSION/apache-flume-$FLUME_VERSION-bin.tar.gz

### Unpack Apache Flume
tar -xzf apache-flume-$FLUME_VERSION-bin.tar.gz

### 
#cd apache-flume-$FLUME_VERSION-bin

### Copy Flume Config
cp -R $SCRIPT_DIR/conf/* ~/apache-flume-$FLUME_VERSION-bin/conf

### conform JAVA_HOME
sed -i 's#^JAVA_HOME.*$#JAVA_HOME='"$JAVA_HOME"'#' ~/apache-flume-$FLUME_VERSION-bin/conf/flume-env.sh

### Compile TwitterAgent
cd $SCRIPT_DIR/agent
mvn package

### Copy TwitterAgent into Flume directory
cp $SCRIPT_DIR/agent/target/flume-sources-1.0-SNAPSHOT.jar ~/apache-flume-$FLUME_VERSION-bin/lib/

### Show command to run Flume
echo "To start Flume use the following command";
echo "~/apache-flume-$FLUME_VERSION-bin/bin/flume-ng agent --conf ~/apache-flume-$FLUME_VERSION-bin/conf --conf-file ~/apache-flume-1.5.0.1-bin/conf/flume.conf --name TwitterAgent";
echo "Remember to Change Hadoop destination in conf/flume.conf";
exit 0;
