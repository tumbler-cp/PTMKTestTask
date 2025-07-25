### Ходжаев Абдужалол Абдужаборович
Отчёт доступен по [ссылке](https://docs.google.com/document/d/1tctLwuisqVdTx8B2mNHH1U4aoMDJif7-VzmrOITAh34/edit?usp=sharing)

## Интрукция

Сборка jar
```
mvn package
```

Запуск контейнера базы данных
```
docker compose up -d
```


Запуск приложения
```
java -jar target/*.jar <arg1> <etc> ...
```

`arg1` - номер режима

`etc` - остальные параметры запуска


*!Внимание*. Конфигурационные файлы (`app.properties` и файл со списком ФИО как указано в `app.properties`)
должны находится на той же директории с которой вы запускаете приложение