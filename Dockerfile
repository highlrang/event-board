FROM amazoncorretto:11
WORKDIR /usr/src/app
#COPY Dockerrun.aws.json .
#RUN chmod +x ./gradlew
#RUN ./gradlew bootJar
ARG JAR_FILE=build/libs/application-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]