#!/bin/bash

set -e

echo "Build metering docker image service..."

mvn install -DskipTests=true -Dodm.home=$PWD/../install

docker-compose build
