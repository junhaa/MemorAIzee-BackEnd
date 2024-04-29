FROM openjdk:17
COPY ./build/libs/MemorAIze-0.0.1-SNAPSHOT.jar memoraize.jar
COPY /home/ubuntu/secret/memoraize-419313-0045e89d04b4.json /app
ENTRYPOINT ["java", "-jar", "memoraize.jar"]
