FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y ffmpeg
COPY ./build/libs/MemorAIze-0.0.1-SNAPSHOT.jar memoraize.jar
ENTRYPOINT ["java", "-jar", "memoraize.jar"]
