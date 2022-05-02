FROM amazoncorretto:11
ARG JAR_FILE=./build/libs/application-0.0.1-SMAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "/application.jar"]