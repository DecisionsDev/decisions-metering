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

