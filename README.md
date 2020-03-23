Alarm Clock
=====

Display the swagger:

http://localhost:8082/swagger-ui.html

Creation of the service:

```sh
sudo vim /etc/systemd/system/alarmclock.service
```

copy this inside:
```sh
Unit]
Description=Alarm Clock

[Service]
User=pi

#Java Program
WorkingDirectory=/home/pi/AlarmClock

#Path of the bash script running the Java program
ExecStart=/bin/bash /home/pi/AlarmClock/AlarmClockLauncher.sh
SuccessExitStatus=143
TimeoutStopSec=7
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Give sh Script execute permission
```sh
sudo chmod u+x /home/pi/AlarmClock/AlarmClockLauncher.sh
```

Enable and start service
```sh
sudo systemctl daemon-reload
sudo systemctl enable alarmclock
sudo systemctl start alarmclock
```

Access DB

mysql -u sby -p
use db_alarmclock;
select * from alarm;
delete from alarm where id = 3;
