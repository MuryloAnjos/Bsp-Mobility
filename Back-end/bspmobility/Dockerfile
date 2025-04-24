FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/bspmobility-0.0.1-SNAPSHOT.jar app.jar
RUN ls -l /app  # Adiciona um comando para listar os arquivos no diretório /app
ENTRYPOINT ["java", "-jar", "app.jar"]