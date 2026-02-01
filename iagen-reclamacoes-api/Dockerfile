# ===== build stage =====
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /project

# 1. Copia os arquivos do wrapper primeiro para aproveitar o cache do Docker
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Torna o script executável e baixa as dependências
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw && ./mvnw -B -e -DskipTests dependency:go-offline

# 2. Copia o código fonte e gera o pacote
COPY src ./src
RUN ./mvnw -B -e clean package

# ===== runtime stage =====
FROM registry.access.redhat.com/ubi8/openjdk-17:1.23
ENV LANGUAGE='en_US:en'

# Copia os artefatos gerados pelo Quarkus
COPY --from=build --chown=185 /project/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /project/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /project/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /project/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
ENTRYPOINT ["/opt/jboss/container/java/run/run-java.sh"]