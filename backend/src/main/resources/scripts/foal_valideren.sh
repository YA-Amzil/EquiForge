#!/bin/bash

LOCALHOST=http://172.25.16.1:8082/api/foals

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo $'CONTROL 1: CREATE A FOAL\n'
#Foal1
curl -d '{"name": "Daisy", "age": 1, "dewormingDate": "2023/01/01"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new
echo $'\n'

#Foal2
curl -d '{"name": "Sarah", "age": 3, "dewormingDate": "2023/03/03"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new
echo $'\n'

#Foal3
curl -d '{"name": "Tim", "age": 4, "dewormingDate": "2023/04/04"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

echo $'\n'

#Foal4
curl -d '{"name": "Tom", "age": 5, "dewormingDate": "2023/05/05"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new
echo $'\n'

#Foal5
curl -d '{"name": "Cedric", "age": 6, "dewormingDate": "2023/06/06"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new
echo $'\n'

#Foal6
curl -d '{"name": "Eva", "age": 7, "dewormingDate": "2023/07/07"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo $'CONTROL 2: UPDATE A FOAL\n'
curl -d '{"name": "Sofie", "age": 2, "dewormingDate": "2023/02/02"}' -H "Content-Type: application/json" -X PUT $LOCALHOST/3/update
echo $'\n'

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo $'CONTROL 3: DELETE A FOAL\n'
curl -X DELETE $LOCALHOST/1/delete
echo $'\n'

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo $'CONTROL 4: GET LIST OF FOALS\n'
curl -H "Content-Type: application/json"  $LOCALHOST
echo $'\n'

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo $'CONTROL 3: GET A FOAL BY ID\n'
curl -H "Content-Type: application/json" $LOCALHOST/1
echo $'\n'

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'
echo 'Press any key to continue...'
read key