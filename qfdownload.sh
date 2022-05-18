#!/bin/sh
dt=`date +'%Y-%m-%d'`
cd /qfdj/

logdir=/qfdj/logs
if [[ ! -d $logdir ]]; then
	mkdir -p $logdir
fi

source /etc/profile
#获取文件列表
node qf.js  > $logdir/downloadList_${dt}.log 2>&1

#下载并合成文件
sleep 2m
javac M3U8Download.java
java M3U8Download > $logdir/m3u8dl_${dt}.log 2>&1





sleep 1m
#删除10天前的文件
deldir="/qfdj/Music/"
fdt=`date +%Y-%m-%d -d "10 day ago"`
echo "current date $dt"

for filename in `ls ${deldir}`;
do 
  #echo $filename
  if [[ $filename =~ ^2022.* ]]; then
      echo $filename
      filedate=${filename//-/}
      echo "-----------------"
      #echo $filedate
      if [[ $filedate < $fdt ]]; then
	 filepath=$deldir$filename
         #echo "delete dir ${filepath}"
         rm -rf $filepath
       
      fi
  fi
done


#生成完整的html页面
sleep 2m
javac createHtml.java
java createHtml > $logdir/createHtml_${dt}.log 2>&1
