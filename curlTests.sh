#!/bin/sh

JSON_1='{"name":"haaallååå", "description":"desc", "dates":["1412005022000", "1414005022000"],"allDayEvent":false, "duration":3600000, "deadline":"1411005022000", "deadlineReminder":true, "notification":"NO_NOTIFICATION", "participators":["a@a.a"]}'

echo "Creating event..."
curl -X POST -H "Content-Type: application/json" -d "$JSON_1" http://localhost:8080/meWap/webresources/events

echo "\nNumber of events:"
curl http://localhost:8080/meWap/webresources/events/count  2>/dev/null | jsonpp

echo "\n\nEvent with id=1:"
curl http://localhost:8080/meWap/webresources/events/1  2>/dev/null | jsonpp

echo "\n\nDeleting event with id=1..."
curl -X DELETE http://localhost:8080/meWap/webresources/events/1

echo "\n"
read -p "Print all events? [y/N]: " yn
case $yn in
[Yy]* ) curl http://localhost:8080/meWap/webresources/events/ 2>/dev/null | jsonpp; 
	break;;
* ) break;;
esac
