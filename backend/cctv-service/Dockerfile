FROM gradle:jdk21-alpine AS build

WORKDIR /project

COPY gradlew /project/
COPY gradle /project/gradle
COPY build.gradle settings.gradle /project/

RUN chmod +x gradlew && ./gradlew build --no-daemon -x test || return 0

COPY src /project/src

RUN ./gradlew clean build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /project

COPY --from=build /project/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/project/app.jar"]