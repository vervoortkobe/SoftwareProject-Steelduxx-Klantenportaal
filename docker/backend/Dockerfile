ARG DOCKER_DEFAULT_PLATFORM

FROM maven:3.8.4-openjdk-17-slim AS build

ENV SENTRY_ENABLED=false
ENV API_SECRET_GROUP=TEST
ENV API_SECRET_KEY=TEST
#ENV MAIL_ADDRESS=${MAIL_ADDRESS}
#ENV MAIL_PWD=${MAIL_PWD}
# Not actually used in application. Just for testing purposes. (Replaced by ENV SECRET_KEY)
ENV SECRET_KEY=356D311FC77379682CF555B1944A777B3475719CC7C48F34CE4DE2AF98 

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean compile package

FROM --platform=${DOCKER_DEFAULT_PLATFORM} openjdk:17-oraclelinux8 AS runtime

WORKDIR /app

ARG JAR_FILE=/app/target/*.jar

COPY --from=build ${JAR_FILE} app.jar

COPY entrypoint.sh .

ENTRYPOINT [ "/bin/bash", "/app/entrypoint.sh" ]
EXPOSE 8080