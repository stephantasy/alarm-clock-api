#!/bin/bash
java -jar -Dspring.profiles.active=domoticz,prod alarm-clock-1.7.1-SNAPSHOT.jar --alarmclock.debug=false
