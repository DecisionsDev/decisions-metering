#!/bin/bash
set -e
echo "start building metering service..."
echo "current build directory:"
pwd
cd ../
ODM_FILE_NAME=odm-distrib-${ODM_VERSION}.zip
#if [ ! -f $HOME/.cache/$ODM_FILE_NAME ]; then
echo "ODM distribution: Starting download..."
if [ ! -f "$HOME/.cache/$ODM_FILE_NAME" ]; then
    ODM_ZIP_URL=${ODM_URL}/${ODM_VERSION}/icp-docker-compose-build-images-${ODM_VERSION}.zip
    curl $ODM_ZIP_URL -u "${ARTIFACTORY_USER}:${ARTIFACTORY_PWD}" -o $ODM_FILE_NAME
    mv $ODM_FILE_NAME $HOME/.cache/
    echo "ODM distribution: download finished..."
else
    echo "ODM distribution: Loading from cache..."
    echo "ODM distribution: Loading finished..."
fi

echo "unzip odm distribution..."
unzip -q $HOME/.cache/$ODM_FILE_NAME

export ODM_HOME=$PWD/install
echo "ODM Home : $ODM_HOME"
