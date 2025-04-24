# Usa uma imagem base com Maven para compilar o projeto
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
# Copia o código-fonte do projeto
COPY . .
# Executa o Maven para gerar o JAR
RUN mvn clean install -DskipTests

# Usa uma imagem leve para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/bspmobility-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]