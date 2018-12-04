#!/bin/sh

log_file=~/server.log

gc_params="-XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses \
           -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=50 -XX:+CMSScavengeBeforeRemark \
           -XX:ParallelGCThreads=4 \
           -XX:+PrintFlagsFinal -XX:+PrintGCDetails -XX:+PrintGCDateStamps  \
           -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+PrintStringTableStatistics -XX:+DisableExplicitGC "

server_name_array=(
    'ft-0.0.1-SNAPSHOT.jar'
)

server_test_array=(
    'http://127.0.0.1:80/'
)

server_start_array=(
    'java -jar -Xms128m -Xmx128m -Xss256k ${gc_params} -Xloggc:/data/gc/gc_ft_%t-log ft-0.0.1-SNAPSHOT.jar'
)

server_log_array=(
    '/root/ft_log.out'
)

server(){
    length=${#server_name_array[@]}

    echo $(date '+%Y-%m-%d %H:%M:%S')

    for ((i=0; i<"$length"; i=i+1))
    do

        serverProcess=$(ps aux | grep "${server_name_array[$i]}" | grep -v grep)
	    echo ${serverProcess}

	    processArray=(${serverProcess// / })
	    serverId=${processArray[1]}

	    server_test_url=${server_test_array[$i]}
	    server_start=${server_start_array[$i]}
	    server_log_file=${server_log_array[$i]}
	    server_name=${server_name_array[$i]}

	    if [[ ${serverId} ]]; then

		    echo "serverId==>${serverId}进程已存在" >> ${log_file}

		    echo "开始检测${server_name}, ${server_test_url}"

		    http_code=$(curl -s -o /dev/null -m 10 --connect-timeout 10 ${server_test_url} -w "%{http_code}")
            echo "${server_test_url}, http code==>${http_code}"

            if [ ${http_code} -eq 200 ]; then
			    echo -e "\e[1;32m ${server_name}, 运行正常 \e[0m"
			    echo ''
			else

				echo -e "\e[1;31m ${server_name}宕机, 开始自动重启... \e[0m"

                kill -9 ${serverId}

				sleep 3

				nohup ${server_start} > ${server_log_file} 2>&1 &

				sleep 90

			fi

	    else

            echo -e "\e[1;31m ${server_name_array[$i]}进程不存在, 开始自动重启... \e[0m"

		    echo -e "\e[1;32m ${server_start}, 请稍候... \e[0m"

		    nohup ${server_start} > ${server_log_file} 2>&1 &

            sleep 60

	    fi

    done
}

while true
do
	server>>${log_file}
	sleep 5
done

