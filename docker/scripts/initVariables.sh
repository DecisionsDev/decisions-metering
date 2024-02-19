#!/bin/bash

if [ ! "$HTTP_PORT" ]
then
  echo "HTTP_PORT unset : set $1"
  export HTTP_PORT=$1
fi

if [ ! "$HTTPS_PORT" ]
then
  echo "HTTPS_PORT unset : set $2"
  export HTTPS_PORT=$2
fi

echo "HTTPS_PORT : $HTTPS_PORT"
echo "HTTP_PORT : $HTTP_PORT"

if [ ! "$CONTEXT_ROOT" ]
then
  echo "CONTEXT_ROOT unset : set blank"
  export CONTEXT_ROOT=""
fi

if [ ! "$METERING_KEYSTORE_PASSWORD" ]
then
  export METERING_KEYSTORE_PASSWORD=changeme
fi

if [ ! "$METERING_KEYSTORE_ALIAS" ]
then
  export METERING_KEYSTORE_ALIAS=odmicpserver
fi

if [ ! "$METERING_ILMT_OUTPUT_DIRECTORY" ]
then
  export METERING_ILMT_OUTPUT_DIRECTORY=/config/storage/ILMT

if [ ! "$METERING_DB_DIRECTORY" ]
then
  export METERING_DB_DIRECTORY=/config/storage/DB
fi
