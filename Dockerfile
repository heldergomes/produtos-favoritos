FROM openjdk:11

EXPOSE 8080

WORKDIR /app

COPY target/produtos-favoritos-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]