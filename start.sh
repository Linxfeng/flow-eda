#!/usr/bin/env bash

docker run -d -p 8082:8082 flow-eda-logger
docker run -d -p 8088:8088 flow-eda-runner
docker run -d -p 8081:8081 flow-eda-web
