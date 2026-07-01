#!/bin/bash
set -x
echo "Install the feature list for ODM on Liberty"
PACKAGELIST="servlet-6.0 springBoot-4.0 transportSecurity-1.0"
featureUtility installFeature $PACKAGELIST --verbose --acceptLicense --noCache --verify=skip
