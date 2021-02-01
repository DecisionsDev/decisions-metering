#!/bin/bash
set -e
echo "start building metering docker image service..."

mvn install -DskipTests=true -Dodm.home=$PWD/../install
docker-compose build
