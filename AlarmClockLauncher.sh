#!/bin/bash
java -jar -Dspring.profiles.active=domoticz,prod alarm-clock-1.0.0-SNAPSHOT.jar --alarmclock.debug=false
