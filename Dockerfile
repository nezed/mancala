FROM gradle:8.2.1-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/mancala-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "/app/mancala-0.0.1-SNAPSHOT.jar"]