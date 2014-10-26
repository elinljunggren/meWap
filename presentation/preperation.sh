#!/bin/sh

access_token="ya29.qgCfDAiWi8RFFRMrZ-tUiQHc1z66HSkKK7pFRSU9-Vpwe-XCMrxQ2skKWgnefNEfpVsoe1myjmn1Zw"

for i in "Födelsedagskalas" "Filmkväll" "Lunchmöte" "Äta glass" "Äta mer glass"
do
	curl 'http://mewap.se/webresources/events' -H 'Content-Type: application/json;charset=UTF-8' -H 'Accept: application/json, text/plain, */*' -H "Cookie: access_token=$access_token" -d '{"name":"'"$i"'","description":"a","duration":0,"deadline":"1425164400000","notification":"NO_NOTIFICATION","dates":["1445883480000"],"participators":["elin.l.ljunggren@gmail.com"],"deadlineReminder":false,"allDayEvent":false}'
done

for j in "Möte" "Presentera meWap" "Programmera" "Middag" "Träna"
do
	curl 'http://mewap.se/webresources/events' -H 'Content-Type: application/json;charset=UTF-8' -H 'Accept: application/json, text/plain, */*' -H "Cookie: access_token=$access_token" -d '{"name":"'"$j"'","description":"a","duration":0,"deadline":"1413583200000","notification":"NO_NOTIFICATION","dates":["1445883480000"],"participators":["elin.l.ljunggren@gmail.com"],"deadlineReminder":false,"allDayEvent":false}'
done
