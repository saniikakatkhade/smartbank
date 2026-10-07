FROM eclipse-temurin:26-jdk

WORKDIR /app

COPY target/smartbook-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]