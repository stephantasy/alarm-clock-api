#!/bin/bash
java -jar -Dspring.profiles.active=domoticz,prod alarm-clock-1.2.0-SNAPSHOT.jar --alarmclock.debug=false
