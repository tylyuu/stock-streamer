FROM adoptopenjdk:11-jre-hotspot

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/stockstreamer-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
