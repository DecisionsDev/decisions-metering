#!/bin/bash
set -x
echo "start package helm material..."
echo "current build directory:"
pwd
cd charts/stable && helm package ../ibm-odm-metering

helm repo index --url 'https://odmdev.github.io/decisions-metering/charts/stable' ./
cd ../..