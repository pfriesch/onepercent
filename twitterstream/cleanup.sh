#!/bin/bash
### https://stackoverflow.com/questions/12613848/finding-directories-older-than-n-days-in-hdfs
usage="Usage: dir_diff.sh [days]"
path="hdfs://localhost:9000/user/hadoop/tweets/"

if [ ! "$1" ]
then
  echo $usage
  exit 1
fi

now=$(date +%s)
hdfs dfs -ls -R $path | grep "^d" | while read f; do 
	dir_path=`echo $f | awk '{print $8}'`
	dir_date=`echo ${dir_path#$path} | cut -d '/' -f 1-3`

	if [ `echo $dir_date | awk -F '/' '{print NF}'` -eq 3 ]; then
		hdfs dfs -test -d $path$dir_date		
		if [ $? -eq 0 ]; then
			difference=$(( ( $now - $(date -d "$dir_date" +%s) ) / (24 * 60 * 60 ) ))
			if [ $difference -gt $1 ]; then
				hdfs dfs -rm -r -skipTrash $path$dir_date
			fi
		fi
	fi
done
