FROM openjdk:8-jdk-alpine
ARG APP_NAME
ENV APP_NAME=${APP_NAME}
ADD ./flow-eda-${APP_NAME}-0.0.1-SNAPSHOT.jar ./flow-eda-${APP_NAME}.jar
ARG APP_PORT
EXPOSE ${APP_PORT}
ENTRYPOINT java -jar flow-eda-${APP_NAME}.jar
