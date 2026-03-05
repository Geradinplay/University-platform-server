# 1 BUILD STAGE (Java 21)
FROM gradle:8.8-jdk21 AS build
WORKDIR /app

# Copy entire project
COPY . /app

# Make gradlew executable
RUN chmod +x gradlew

# Build project (skip tests inside Docker)
RUN ./gradlew build --no-daemon -x test


# 2 RUNTIME STAGE (Java 21)
FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /opt/eureka

# Copy jar from APP module
COPY --from=build /app/app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["-jar", "app.jar"]
