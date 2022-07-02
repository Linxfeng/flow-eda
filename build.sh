#!/usr/bin/env bash

docker build -t flow-eda-oauth2 --build-arg APP_NAME='oauth2' --build-arg APP_PORT=8086 .
docker build -t flow-eda-logger --build-arg APP_NAME='logger' --build-arg APP_PORT=8082 .
docker build -t flow-eda-runner --build-arg APP_NAME='runner' --build-arg APP_PORT=8088 .
docker build -t flow-eda-web --build-arg APP_NAME='web' --build-arg APP_PORT=8081 .
