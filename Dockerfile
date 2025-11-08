# 1. Use uma imagem base do Java 17
FROM eclipse-temurin:17-jdk-jammy

# 2. Defina o diretório de trabalho dentro do container
WORKDIR /app

# 3. Copie o arquivo .jar compilado para dentro do container
# O "mvnw package" gera o .jar dentro da pasta 'target'
COPY target/*.jar app.jar

# 4. Exponha a porta que o Spring Boot usa
EXPOSE 8080

# 5. O comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]