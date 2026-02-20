# =========================
# 1) Build stage
# =========================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o projeto inteiro
COPY . .

# Roda testes (garante que o build do Docker só passa se estiver ok)
RUN mvn -q test

# Gera o jar
RUN mvn -q package -DskipTests=false

# =========================
# 2) Runtime stage
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]