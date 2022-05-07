FROM amazoncorretto:11
WORKDIR /usr/src/app

COPY src src
COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

ARG JAR_FILE=build/libs/application-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]