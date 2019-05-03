#!/bin/sh

log_file=~/boot/server.log

gc_params="-XX:+UseConcMarkSweepGC -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses \
           -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=50 -XX:+CMSScavengeBeforeRemark \
           -XX:ParallelGCThreads=4 -XX:StringTableSize=1000003 -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m \
           -XX:+PrintFlagsFinal -XX:+PrintGCDetails -XX:+PrintGCDateStamps  \
           -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+PrintStringTableStatistics -XX:+DisableExplicitGC "

server_name_array=(
    'ft-0.0.1-SNAPSHOT.jar'
)

server_test_array=(
    'http://127.0.0.1:9001/'
)

server_start_array=(
    '-Xms256m -Xmx256m -Xmn128m -Xss256k -Xloggc:/data/gc/gc_ft_%t-log ft-0.0.1-SNAPSHOT.jar'
)

server_log_array=(
    '/root/boot/ft_log.out'
)

server(){
    length=${#server_name_array[@]}

    echo $(date '+%Y-%m-%d %H:%M:%S')

    for ((i = 0; i < ${length}; i++))
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

                stop ${server_name}

				sleep 3

				nohup java -jar ${gc_params} ${server_start} > ${server_log_file} 2>&1 &

				echo -e "\e[1;31m ${server_name}启动完毕... \e[0m"

				sleep 90

			fi

	    else

            echo -e "\e[1;31m ${server_name_array[$i]}进程不存在, 开始自动重启... \e[0m"

		    echo -e "\e[1;32m ${server_start}, 请稍候... \e[0m"

		    nohup java -jar ${gc_params} ${server_start} > ${server_log_file} 2>&1 &

            sleep 60

	    fi

    done
}

stop() {
    server_name=${1}

    count=0
    endTime=90
    forceKillTime=60
    while (( "${count} < ${endTime}" ))
    do
        process=$(getProcessId ${server_name})
        if [[ "${process}" == "" ]]
        then
            echo "${server_name} already stopped"
            return 0
        fi
        echo "stop ${process} ,time use ${count} seconds"
        if (( ${count} < ${forceKillTime} ))
        then
            echo "kill ${process}"
            kill ${process}
        else
            echo "force kill ${process}"
            kill -9 ${process}
        fi
        let "count+=5"
        sleep 5
    done
}

getProcessId(){
    process=`ps aux | grep ${1} | grep -v grep | awk '{print $2}'`
    echo ${process}
}

while true
do
	server>>${log_file}
	sleep 10
done

