# java-sprint2-hw


GET /tasks/task/?id= - возвращает таск, id которого будет указаоно в параметре

DELETE /tasks/task/?id= - удаляет таск, id которого было указано в параметре



GET /tasks/task/ - возвращает все таски

DELETE /tasks/task/ - удаляет все таски

POST /tasks/task/ - создание нового эпика или обновление данных о старом эпике.
Параметры эпика передаются в теле запроса:

{
"status": "NEW",
"Name": "task1",
"Description": "task1Test",
"taskId": 1,
"duration": {
"seconds": 0,
"nanos": 0
},
"localDate": {
"date": {
"year": 999999999,
"month": 12,
"day": 31
},
"time": {
"hour": 23,
"minute": 59,
"second": 59,
"nano": 999999999
}
},
"type": "TASK"
}



GET /tasks/subtask/epic/?id= - возвращает сабтаски эпика, чье id указывается в параметре



GET /tasks/subtasks/?id= - возвращает сабтаск, id которого будет указаоно в параметре

DELETE /tasks/subtasks/?id= - удаляет сабтаск, id которого было указано в параметре



GET /tasks/subtasks/ - возвращает все сабтаски

DELETE /tasks/subtasks/ - удаляет все сабтаски

POST /tasks/subtasks/ - создание нового сабтаска или обновление данных о старом сабтаске.
Параметры эпика передаются в теле запроса:

{
"status": "DONE",
"Name": "subTask3Test",
"Description": "subtask3Test",
"taskId": 3,
"duration": {
"seconds": 10,
"nanos": 0
},
"localDate": {
"date": {
"year": 2019,
"month": 11,
"day": 11
},
"time": {
"hour": 1,
"minute": 1,
"second": 0,
"nano": 0
}
},
"type": "SUBTASK"
}



GET /tasks/epic - вернет все эпики

POST /tasks/epic - создание нового эпика или обновление данных о старом эпике.
Параметры эпика передаются в теле запроса:

{
"status": "NEW",
"Name": "epic0",
"Description": "epic0",
"taskId": 0,
"duration": {
"seconds": 0,
"nanos": 0
},
"localDate": {
"date": {
"year": 1,
"month": 1,
"day": 1
},
"time": {
"hour": 1,
"minute": 1,
"second": 0,
"nano": 0
}
},
"type": "EPIC"
}



GET /tasks/history - возвращает историю



GET /tasks - - возвращает все таски
