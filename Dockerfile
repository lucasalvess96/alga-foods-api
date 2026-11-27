# ETAPA 1 - BUILD
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Dependencia privada preparada por docker/prepare-security-lib.ps1.
COPY docker/security-lib/ /tmp/security-lib/
RUN mvn -B org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
    -Dfile=/tmp/security-lib/security-lib-0.0.1-SNAPSHOT.jar \
    -DpomFile=/tmp/security-lib/security-lib-0.0.1-SNAPSHOT.pom

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ETAPA 2 - RUNTIME
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/alga-foods-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
