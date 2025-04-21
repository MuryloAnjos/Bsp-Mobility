FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN mvn clean package -X
RUN ls -la .
RUN ls -la target/
COPY target/bspmobility-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]