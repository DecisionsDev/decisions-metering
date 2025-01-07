#!/bin/bash
set -x
# Install the driver for Derby
echo "Install the feature list for ODM on Liberty"
ROOTFEATUREDIR=/opt/wlppackage
PACKAGELIST="servlet-6.0 springBoot-3.0 transportSecurity-1.0"
ls /opt/wlppackage/
rm /opt/wlppackage/.donotremoved
if [ ! -d $ROOTFEATUREDIR/features ]; then
  mkdir -p $ROOTFEATUREDIR
  echo "Downloading features list : $PACKAGELIST"
  installUtility download $PACKAGELIST --location=$ROOTFEATUREDIR;
fi
installUtility install $PACKAGELIST --from=$ROOTFEATUREDIR
