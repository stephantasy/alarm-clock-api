#!/bin/bash
java -jar -Dspring.profiles.active=domoticz,prod alarm-clock-1.7.0-SNAPSHOT.jar --alarmclock.debug=false
