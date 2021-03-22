#!/bin/bash
set -e
echo "start package helm material..."
echo "current build directory:"
pwd
mkdir index && cd index && helm package ../charts/ibm-odm-metering 

helm repo index --url 'https://github.com/lgrateau/decisions-metering/releases/download/'$TRAVIS_TAG ./
