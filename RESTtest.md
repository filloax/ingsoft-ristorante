## DisabilitaPrenotazioni



curl -X POST -H "Content-Type: application/json" -d "{\"inizio\":\"2021-06-14T10:34:40.655999\",\"fine\":\"2021-07-05T13:33:00\",\"tipo\":\"PRENOTAZIONE\"}"  http://localhost:8080/disa-prenotazioni -v
curl -X POST -H "Content-Type: application/json" -d "{\"inizio\":\"2022-06-14T10:34:40.655999\",\"fine\":\"2023-08-05T13:33:00\",\"tipo\":\"PRENOTAZIONE\"}"  http://localhost:8080/disa-prenotazioni -v
curl -X POST http://localhost:8080/disa-prenotazioni/test -v
curl -X DELETE http://localhost:8080/disa-prenotazioni/1 -v
