#!/bin/bash

set -e

echo "Current build directory: $(pwd)"
cd ..

ODM_FILE_NAME=odm-distrib-${ODM_VERSION}.zip
echo "ODM distribution: Starting download..."
if [ ! -f "$HOME/.cache/$ODM_FILE_NAME" ]; then
    ODM_ZIP_URL=${ODM_URL}/${ODM_VERSION}/icp-docker-compose-build-images-${ODM_VERSION}.zip
    curl ${ODM_ZIP_URL} -u "${ARTIFACTORY_USER}:${ARTIFACTORY_PWD}" -o ${ODM_FILE_NAME}
    mv ${ODM_FILE_NAME} ${HOME}/.cache/
    echo "ODM distribution: Download finished."
else
    echo "ODM distribution: Load from cache."
fi

echo "Unzip odm distribution..."
unzip -q ${HOME}/.cache/${ODM_FILE_NAME}

export ODM_HOME=$(pwd)/install
echo "ODM home: ${ODM_HOME}"
