app_process_id=`/bin/ps -fu $USER|grep "MyWeddi-0.0.1-SNAPSHOT.jar" |awk '{print $2}'`
echo $app_process_id

pwd
ls -l

ls -l target

kill -9 app_process_id
java -jar target/MyWeddi-0.0.1-SNAPSHOT.jar &


