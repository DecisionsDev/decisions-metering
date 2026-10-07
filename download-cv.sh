#!/bin/bash

set -ex

GH_API=https://github.ibm.com/api/v3
GH_REPO=${GH_API}/repos/IBMPrivateCloud/content-verification
GH_LATEST=${GH_REPO}/releases/releases/latest
AUTH="Authorization: token ${GHE_TOKEN}"

response=$(curl --header "${AUTH}" --header "Accept:application/vnd.github+json" --header "X-GitHub-Api-Version:2022-11-28" --silent ${GH_LATEST})
id=$(echo "${response}" | jq '.assets[2].id' | tr -d '"')
name=$(echo "${response}" | jq '.assets[2].name' | tr -d '"')
GH_ASSET=${GH_REPO}/releases/assets/${id}

curl --location --header "${AUTH}" --header "Accept: application/octet-stream" --output ${name} --silent ${GH_ASSET}

tar xvzf ${name}
