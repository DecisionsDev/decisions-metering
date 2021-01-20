#!/bin/bash

sed -i 's|METERING_KEYSTORE_PASSWORD|'$METERING_KEYSTORE_PASSWORD'|g' /config/tlsSecurity.xml
openssl pkcs12 -export -in /config/resources/certificate/server.crt -inkey /config/resources/certificate/server.key -out /config/resources/security/server.p12 -name ${METERING_KEYSTORE_ALIAS} -passout pass:${METERING_KEYSTORE_PASSWORD}
keytool -importkeystore -srckeystore /config/resources/security/server.p12 -srcstoretype pkcs12 -srcalias ${METERING_KEYSTORE_ALIAS} -destkeystore /config/resources/security/keystore.jks -deststoretype jks -deststorepass ${METERING_KEYSTORE_PASSWORD} -destalias ${METERING_KEYSTORE_ALIAS} -srcstorepass ${METERING_KEYSTORE_PASSWORD}
