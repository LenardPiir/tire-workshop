FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY . .

RUN ./gradlew bootJar

ENTRYPOINT ["java","-jar","build/libs/workshop-0.0.1-SNAPSHOT.jar"]