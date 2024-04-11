FROM maven:3.9-amazoncorretto-21 as builder
WORKDIR /app
COPY pom.xml pom.xml 
COPY src/ src/
RUN mvn package

FROM amazoncorretto:21-alpine3.19
COPY --from=builder /app/target/*.jar /app/application.jar
CMD ["java","-jar", "-Dspring.profiles.active=docker", "/app/application.jar"]