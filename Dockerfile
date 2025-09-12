# --- ESTÁGIO 1: O Ambiente de Build (A "Cozinha") ---
FROM maven:3.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia todo o projeto para dentro do container
COPY . .

# Compila e empacota a aplicação usando o 'mvn' nativo da imagem, não o wrapper.
# Esta é a linha que corrigimos.
RUN mvn package -DskipTests -B


# --- ESTÁGIO 2: O Ambiente de Produção (O "Prato Final") ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia APENAS o .jar final que foi gerado no estágio anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]