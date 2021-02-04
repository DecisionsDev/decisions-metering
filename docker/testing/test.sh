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
    echo "Result of load test seems not good."
fi

curl http://localhost:8888/backup --output ilmt.zip
unzip ilmt.zip -d ilmt
# Assert the metering files contains expected metrics
grep "MILLION_MONTHLY_DECISIONS" ilmt/*.slmtag
if [ $? -ne 0 ]; then
    echo "Cannot find the expected value MILLION_MONTHLY_DECISIONS in the ILMT File. Metrics for runtime"
fi
grep "<Value>0.003</Value>" ilmt/4ee9e1b0d7aba2e118b0be1649928b14.slmtag
if [ $? -ne 0 ]; then
    echo "Cannot find the expected value 0.003 in the ILMT File. (3000 executions"
fi

