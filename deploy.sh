app_process_id=`/bin/ps -fu $USER|grep "MyWeddi-0.0.1-SNAPSHOT.jar" |awk '{print $2}'`
echo $app_process_id