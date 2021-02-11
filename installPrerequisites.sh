#!/bin/bash
set -e
echo "preping the environment for building the metering service..."

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu
  $(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get -y -o Dpkg::Options::="--force-confnew" install docker-ce


UTILDIR="$HOME/.cache/util"
function downloadTool {
    # $1 : The tool executable name
    # $2 : The url of thetools
    if [ ! -f "$UTILDIR/$1" ]; then 
      echo "Downloading : curl $2 --output $1 "
      curl $2 --output $1 && chmod +x $1 && sudo mv $1 /usr/local/bin 
    else 
      echo "$1 is already in the cache."
    fi 
    chmod +x $UTILDIR/$1 && sudo mv $UTILDIR/$1 /usr/local/bin 
}
# Linter
downloadTool "cv" "https://github.ibm.com/IBMPrivateCloud/content-verification/releases/download/v2.4.0/cv-linux-amd64.tar.gz"
cv version
# hey
downloadTool "hey" "https://hey-release.s3.us-east-2.amazonaws.com/hey_linux_amd64"
hey version
# docker-compose
DCCOMPOSE=https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-`uname
  -s`-`uname -m`
downloadTool "docker-compose" $DCCOMPOSE


docker --version
docker-compose version