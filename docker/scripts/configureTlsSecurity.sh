#!/bin/bash

sed -i 's|METERING_KEYSTORE_PASSWORD|'$METERING_KEYSTORE_PASSWORD'|g' /config/tlsSecurity.xml

if [ -f "/config/resources/certificate/keystore.jks" ]
then
	echo "use /config/resources/certificate/keystore.jks"
	cp /config/resources/certificate/keystore.jks /config/resources/security/keystore.jks
else
	echo "generate keystore.jks using certificate server.crt and private key server.key"
	openssl pkcs12 -export -in /config/resources/certificate/server.crt -inkey /config/resources/certificate/server.key -out /config/resources/security/server.p12 -name ${METERING_KEYSTORE_ALIAS} -passout pass:${METERING_KEYSTORE_PASSWORD}
	keytool -v -importkeystore -srckeystore /config/resources/security/server.p12 -srcstoretype pkcs12 -srcalias ${METERING_KEYSTORE_ALIAS} -destkeystore /config/resources/security/keystore.jks -deststoretype jks -deststorepass ${METERING_KEYSTORE_PASSWORD} -destalias ${METERING_KEYSTORE_ALIAS} -srcstorepass ${METERING_KEYSTORE_PASSWORD}
fi
