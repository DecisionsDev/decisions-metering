#!/bin/bash

# This script downloads the first asset from the latest Github release of a
# private repo. 
#
# PREREQUISITES
#
# curl, jq
#
# USAGE
#
# Set owner and repo variables inside the script, make sure you chmod +x it.
#
#     ./download.sh "--GITHUB TOKEN HERE--"
#

# Define variables
echo "---------------------------------------------------------------------"
echo "Define variables"
echo "---------------------------------------------------------------------"


GITHUB_API_TOKEN=$1
REPO=$2
GH_API="https://github.ibm.com/api/v3"
GH_REPO="$GH_API/repos/$REPO"
GH_LATEST="$GH_REPO/releases/tags/v2.4.0"
AUTH="Authorization: token $GITHUB_API_TOKEN"

# Read asset name and id
echo "---------------------------------------------------------------------"
echo "Read asset name and id"
echo "---------------------------------------------------------------------"

response=$(curl -sH "$AUTH" $GH_LATEST)
id=`echo "$response" | jq '.assets[2] .id' |  tr -d '"'`
name=`echo "$response" | jq '.assets[2] .name' |  tr -d '"'`
GH_ASSET="$GH_REPO/releases/assets/$id"

# Print Details
echo "---------------------------------------------------------------------"
echo "Print Details"
echo "Assets Id: $id"
echo "Name: $name"
echo "Assets URL: $GH_ASSET"
echo "---------------------------------------------------------------------"

# Downloading asset file
echo "---------------------------------------------------------------------"
echo "Downloading asset file"
echo "---------------------------------------------------------------------"
curl -q -L -o "$name" -H "$AUTH" -H 'Accept: application/octet-stream' "$GH_ASSET" 
