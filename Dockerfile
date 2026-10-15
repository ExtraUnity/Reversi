FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY src/Server/Server.java ./Server/Server.java

RUN javac Server/Server.java

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/Server ./Server

CMD ["java", "Server.Server"]