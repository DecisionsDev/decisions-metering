#!/bin/bash
set -e
# Perfom et first execution
RSPATH="/production_deployment/1.0/loan_validation_production/1.0"
function loadRuntime() {
    ServerURL=$1
    NbRequest=$2
    echo "---------------------------------------------"
    echo "- Executing on $NbRequest on Server URL : $1 "
    echo "---------------------------------------------"
    RESULT=$(curl -X POST -u "odmAdmin:odmAdmin" $ServerURL/DecisionService/rest$RSPATH -b "loanvalidation.json" -T 'loanvalidation.json' -H "Content-Type: application/json; charset=UTF-8" -v)

    echo $RESULT | grep "validData"
    if [ $? -ne 0 ]; then
        echo "Cannot verify payload"
        exit 1;
    fi

    # Execution for the
    hey -D "loanvalidation.json" -n $NbRequest -m "POST" -H "Authorization: Basic b2RtQWRtaW46b2RtQWRtaW4=" -T "application/json; charset=UTF-8" $ServerURL/DecisionService/rest$RSPATH
    if [ $? -ne 0 ]; then
        echo "Result of load test seems not good."
        exit 1;
    fi
    echo "---------------------------------------------"
    echo "- End of execution "
    echo "---------------------------------------------"
}

function assertResult() {
    Metric=$1
    Ratio=$2
    ComputeExecution=$Ration*1000000
    # Assert the metering files contains expected metrics
    echo "Checking Metrics name"
    grep "$Metric" ilmt/*.slmtag
    if [ $? -ne 0 ]; then
        echo "Cannot find the expected value $Metric in the ILMT File. Metrics for runtime"
        exit 1;
    else 
        sudo --preserve-env docker-compose -f docker/testing/docker-compose-metering.yml logs
        sudo --preserve-env docker-compose logs
    fi
    echo "Checking Metrics Value"
    grep "<Value>$Ratio</Value>" ilmt/*.slmtag
    if [ $? -ne 0 ]; then
        echo "Cannot find the expected value $Metric in the ILMT File - $ComputeExecution executions"
        exit 1;
    else
        sudo --preserve-env docker-compose -f docker/testing/docker-compose-metering.yml logs
        sudo --preserve-env docker-compose logs
    fi
}
sleep 60
# Assert Runtime of the RES Console
loadRuntime "http://localhost:9080" "2000"

# Assert Runtime of the Decision Service.
loadRuntime "http://localhost:9090" "3000"
sleep 100
$(rm -R ilmt ilmt.zip ; true)
curl -k -v https://localhost:9999/backup --output ilmt.zip
unzip -n ilmt.zip -d ilmt
echo $(cat ilmt/*.* ; true)
assertResult "MILLION_MONTHLY_DECISIONS" "0.005"
echo "RESULT : $?"

assertResult "THOUSAND_MONTHLY_ARTIFACTS" "0.079"
echo "RESULT : $?"
