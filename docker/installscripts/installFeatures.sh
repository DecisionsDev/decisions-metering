#!/bin/bash
set -x
echo "Install the feature list for ODM on Liberty"
PACKAGELIST="springBoot-4.0 servlet-6.1 transportSecurity-1.0"
featureUtility installFeature $PACKAGELIST --verbose --acceptLicense --noCache --verify=skip
