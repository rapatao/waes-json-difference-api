FROM java:8-jre-alpine

COPY target/json-difference-api.jar json-difference-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=/dev/./urandom", "-Dspring.profiles.active=docker", "-jar", "json-difference-api.jar"]