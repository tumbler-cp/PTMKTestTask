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

arg1 - номер режима
etc - остальные параметры запуска
