#!/bin/bash
set -e
echo "start package helm material..."
echo "current build directory:"
pwd
mkdir index && cd index && helm package ../charts/ibm-odm-metering-service 
#- helm repo index --url https://github.com/lgrateau/decisions-metering/releases/download/$TRAVIS_TAG/ibm-odm-metering-service-20.0.0.tgz
echo "TRAIS TAG :" $TRAVIS_TAG
helm repo index --url 'https://github.com/lgrateau/decisions-metering/releases/download/'$TRAVIS_TAG'/ibm-odm-metering-service-20.0.0.tgz' ./
