FROM maven:3.8.4-openjdk-17-slim

WORKDIR /app

COPY mvnw .
#COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/springboot-thymeleaf-crud-web-app-0.0.1-SNAPSHOT.jar"]
