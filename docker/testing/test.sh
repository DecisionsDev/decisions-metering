#!/bin/bash

# Perfom et first execution

RESULT=$(curl -X POST -u "odmAdmin:odmAdmin" http://localhost:9080/DecisionService/rest/test_deployment/1.0/loan_validation_with_score_and_grade/1.0 -b "loanvalidation.json" -T 'loanvalidation.json' -v -H "Content-Type: application/json; charset=UTF-8")

echo $RESULT | grep "The borrower's SSA" 
if [ $? = 0 ]; then
    echo "Seems KO"
fi