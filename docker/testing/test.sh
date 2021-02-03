#!/bin/bash

# Perfom et first execution
set -e
RESULT=$(curl -X POST -u "odmAdmin:odmAdmin" http://localhost:9080/DecisionService/rest/test_deployment/1.0/loan_validation_with_score_and_grade/1.0 -b "loanvalidation.json" -T 'loanvalidation.json' -H "Content-Type: application/json; charset=UTF-8" -v)

echo $RESULT | grep "The borrower's SSN" 
if [ $? -ne 0 ]; then
    echo "Cannot verify payload"
    exit 1;
fi

hey -D "loanvalidation.json" -n 3000 -m "POST" -H "Authorization: Basic b2RtQWRtaW46b2RtQWRtaW4=" -T "application/json; charset=UTF-8" http://localhost:9080/DecisionService/rest/test_deployment/1.0/loan_validation_with_score_and_grade/1.0
if [ $? -ne 0 ]; then
    echo "Load seems KO"
    exit 1;
fi

curl http://localhost:8888/backup --output ilmt.zip
unzip ilmt.zip -d ilmt
# Assert the metering files contains expected metrics
grep "MILLION_MONTHLY_DECISIONS" ilmt/*.slmtag
grep '0.0.3' ilmt/*.slmtag
