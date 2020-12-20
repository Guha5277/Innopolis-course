#Определение с какого образа будет происходить создание контейнера (обязательная инструкция)
FROM openjdk:8
ADD httpserver.jar server.jar
ENTRYPOINT java -jar server.jar