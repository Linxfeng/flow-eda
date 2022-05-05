#!/usr/bin/env bash

docker build -t flow-eda-logger --build-arg APP_NAME='logger' --build-arg APP_PORT=8082 .
docker build -t flow-eda-runner --build-arg APP_NAME='runner' --build-arg APP_PORT=8088 .
docker build -t flow-eda-web --build-arg APP_NAME='web' --build-arg APP_PORT=8081 .

docker run -d -p 8082:8082 flow-eda-logger
docker run -d -p 8088:8088 flow-eda-runner
docker run -d -p 8081:8081 flow-eda-web
