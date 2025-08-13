# ===== build stage =====
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests package -Pskip --fail-never || true
COPY src ./src
RUN mvn -q -DskipTests package

# ===== run stage =====
FROM eclipse-temurin:21-jre-alpine
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Duser.timezone=UTC"
WORKDIR /opt/app
COPY --from=build /app/target/eureka-server-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]