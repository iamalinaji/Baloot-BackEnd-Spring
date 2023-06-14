FROM eclipse-temurin:17

WORKDIR /app

COPY target/Baloot-0.0.1-SNAPSHOT.jar /app/Baloot.jar

ENTRYPOINT ["java", "-jar", "Baloot.jar"]
