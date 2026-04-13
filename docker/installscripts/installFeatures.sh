#!/bin/bash
set -x
echo "Install the feature list for ODM on Liberty"
PACKAGELIST="servlet-4.0 springBoot-2.0 transportSecurity-1.0"
featureUtility installFeature $PACKAGELIST --verbose --acceptLicense --noCache --verify=skip
