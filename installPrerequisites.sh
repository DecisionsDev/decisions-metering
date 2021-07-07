#!/bin/bash

set -e

echo "--- Verifying Helm charts compliance --"
echo "- Installing cv linter "
chmod a+x downloadGhelRelease.sh && ./downloadGhelRelease.sh "$GHELTOKEN" "$CVLINTERREPO" && tar xvzf "cv-linux-amd64.tar.gz"
chmod a+x cv && sudo mv cv /usr/local/bin && cv version
# Verifying Helm Charts
echo "- Linting Helm Charts "
cv lint helm charts/ibm-odm-metering -o $PWD/charts/ibm-odm-metering/tests/lintOverride.yaml
echo "- Helm charts format is OK "
