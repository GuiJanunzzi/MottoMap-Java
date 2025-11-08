# --- ESTÁGIO 1: A "CONSTRUÇÃO" (Build) ---
# Usamos uma imagem que já tem o Maven e o Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia todo o código-fonte do seu projeto para dentro da imagem
COPY . .

# Executa o comando do Maven para compilar o projeto e gerar o .jar
# Isso vai criar a pasta /app/target/
RUN ./mvnw package -DskipTests


# --- ESTÁGIO 2: A IMAGEM FINAL (Run) ---
# Começamos de uma imagem limpa, apenas com o Java 17 para rodar (JRE)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia APENAS o arquivo .jar que foi gerado no estágio "builder"
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# O comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]