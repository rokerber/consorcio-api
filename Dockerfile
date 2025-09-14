# --- ESTÁGIO ÚNICO: Ambiente de Produção ---
# Este Dockerfile é mais simples porque assume que o pipeline de CI (Drone)
# já compilou e testou o código, gerando o arquivo .jar na pasta 'target'.

# Começamos com uma imagem de Java Runtime (JRE) pequena e segura para produção.
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia APENAS o .jar que foi gerado pelo passo 'build-and-test' do Drone CI.
# O Drone disponibiliza a pasta 'target' para este passo.
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]