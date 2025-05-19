#!/bin/bash

LOCALHOST=http://172.25.16.1:8082/api/horses

echo $'CONTROL 1: CREATE A HORSE\n'

# Paard 1
curl -d '{"name": "Bucephalus", "birthDate": "2023/05/11", "height": 1.8, "isPregnant": false, "studHorse": "Alexander"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

# Paard 2
curl -d '{"name": "Shadowfax", "birthDate": "2022/10/16", "height": 1.7, "isPregnant": true, "studHorse": "Gandalf"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

# Paard 3
curl -d '{"name": "Sleipnir", "birthDate": "2023/01/25", "height": 2.0, "isPregnant": false, "studHorse": "Odin"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

# Paard 4
curl -d '{"name": "Arion", "birthDate": "2021/06/27", "height": 1.6, "isPregnant": false, "studHorse": "Adrastus"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

# Paard 5
curl -d '{"name": "Sleek", "birthDate": "2009/03/14", "height": 1.9, "isPregnant": true, "studHorse": "Apollo"}' -H "Content-Type: application/json" -X POST $LOCALHOST/create-new

echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'

echo $'CONTROL 2: UPDATE A HORSE\n'
# Paard 1
curl -d '{"name": "Bucephalus Jr.", "birthDate": "2010/03/12", "height": 1.6, "isPregnant": true, "studHorse": "Daisy"}' -H "Content-Type: application/json" -X PUT $LOCALHOST/2/update
echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'

echo $'CONTROL 3: DELETE A HORSE\n'
curl -X DELETE $LOCALHOST/1/delete
echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'

echo $'CONTROL 4: GET LIST OF HORSES\n'
curl -H "Content-Type: application/json" $LOCALHOST/api/horses
echo $'\n+++++++++++++++++++++++++++++++++++++++++++++++++++\n'

echo $'CONTROL 5: GET A FOAL BY ID\n'
curl -H "Content-Type: application/json" $LOCALHOST/2
echo $'\n'

echo 'Druk op een toets om door te gaan...'
read key