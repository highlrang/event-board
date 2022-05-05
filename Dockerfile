FROM amazoncorretto:11
WORKDIR /usr/src/app
ARG JAR_FILE=build/libs/application-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]