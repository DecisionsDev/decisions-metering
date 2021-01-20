#!/bin/bash
set -e

. ${SCRIPT}/initVariables.sh 8888 9999

#${SCRIPT}/checkLicense.sh

if [ ! -f /config/initializeddb.flag ] ; then
	# DO STUFF
	touch /config/initializeddb.flag
fi;


#if [ -n "$RELEASE_NAME" ]
#then
#  echo "Prefix cookie names with $RELEASE_NAME"
#        sed -i 's|RELEASE_NAME|'$RELEASE_NAME'|g' /config/httpSession.xml
#else
#  echo "Prefix cookie names with $HOSTNAME"
#        sed -i 's|RELEASE_NAME|'$HOSTNAME'|g' /config/httpSession.xml
#fi

$SCRIPT/configureTlsSecurity.sh

/opt/ibm/wlp/bin/server run defaultServer
