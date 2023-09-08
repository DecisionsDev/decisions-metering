# IBM Decisons usage metering service with IBM Automation Decision Services

## Overview

The usage metering service receives metering information from Automation Decision Services, and aggregates it to produce license files.
The service application runs as an embedded HTTP server that receives and processes HTTP requests. These HTTP requests are emitted by metering components that can be configured in Automation Decision Services.

The usage metering service aggregates reported usage periodically in the license files that are stored in a directory.
The files show *IBM Automation Decision Services* as a software name, refer to a specific decision client application instance, and contain the MONTHLY_API_CALL metric that corresponds to the number of successful decision service executions.

The usage metering service uses a single-file, disk-based database engine to ensure that the service can be stopped and restarted without losing data.

## Prerequisites

Make sure that you have everything you need:

#### For compiling

- IBM License Metric Tool (ILMT) jar file
- Java SDK 11
- Apache Maven 3.6.3 or later version

#### For using
- JRE 11
- IBM Automation Decision Services 22.0.2 or later version
(with configuration set for metering - see the [Configuring the clients](#configuring-clients) section)

***Note:*** *You can retrieve the ILMT jar file from the download service for Automation Decision Services (<DECISION_DESIGNER_SERVER_URL>/download/)*


## Compiling the usage metering service

To compile the usage metering service:

1. Define an environment variable named **ILMT_JAR_PATH** that contains the path to the ILMT jar file (*license_metric_logger_2.1.1.201507131115.jar*) in the file system.

2.	In the directory that contains the *pom.xml* file, run one of the following commands:

```mvn clean install -PADS```

or 

```mvn clean install -PADS -DskipTests``` to avoid running the unit tests
 
> Note: You can dynamically pass the location of the ILMT jar file in the compilation command line:

> ```mvn clean install -Dilmt.jar.file.path=<*ILMT jar file path*>```

The stand-alone service *metering-service-ADS.jar* is produced in the service/target directory.

## Running the usage metering service

Use the following command to start the usage metering service without any customization:

```java -jar metering-service-ADS.jar```

To customize settings when you start the usage metering service, set the parameters by using a **--** prefix. 

For example:

```java -jar metering-service-ADS.jar --parameterName=parameterValue```

To start the usage metering service on port 9090 and set the log level to DEBUG, for example, use the following command:

```java -jar metering-service-ADS.jar --server.port=9090 --com.ibm.decision.metering.ilmt.service.loggingLevel=DEBUG```

The following parameters are available:

Name | Default value | Description
--- | :-: | ---
server.port | 8888 | The HTTP port on which the usage metering service operates. You might have to change it to avoid a conflict.
server.servlet.context-path | / | The context root at which the usage metering service operates.
com.ibm.decision.metering.ilmt.service.loggingLevel | INFO | The log level that is used by the application. Possible values include ERROR, WARN, INFO, DEBUG, and TRACE.
com.ibm.decision.metering.ilmt.service.ILMToutputDirectory | ./ILMT_files | The directory where the license files are stored. It must be either a relative path (starting with "./" or "../") or an absolute path (starting with "/"). It must correspond to a directory that is declared in ILMT so that the directory is scanned. Do not use a temporary location.
com.ibm.decision.metering.ilmt.service.processingRate | 60000 | The rate in milliseconds when the usage is processed and written to the license files.
com.ibm.decision.metering.ilmt.service.processingInitialDelay | 60000 | The delay in milliseconds before the first processing occurs after the usage metering service is started.
com.ibm.decision.metering.ilmt.service.databaseFilePath | ./decision_usage_metering.data | The file path of the database used by the usage metering service internally. Do not use a temporary location.
com.ibm.decision.metering.ilmt.service.databaseUser | decision | The username of the database that is used by the usage metering service internally.
com.ibm.decision.metering.ilmt.service.databasePassword | decision_metering | The password of the database that is used by the usage metering service internally.

## Enabling HTTPS in the usage metering service

Add the following parameters when you run the usage metering service:

|Name  | Example value |Description  |
|--|--|--|
| server.ssl.key-store | classpath:keystore.p12 | Store the keystore that contains the SSL certificate in the class path. |
| server.ssl.key-store-password | password | The password that is used to access the keystore. |
| server.ssl.key-store-type | pkcs12 | The type of the keystore (JKS or PKCS12). |
| server.ssl.key-alias | tomcat | The alias that identifies the key in the keystore. |
| server.ssl.key-password | password | The password that is used to access the key in the keystore. |

To start the usage metering service on HTTPS, for example, use the following command:

```java -jar metering-service-ADS.jar --server.port=8443 --server.ssl.key.store=classpath:keystore.p12 --server.ssl.key-store-password=password --server.ssl.key-store-type=pkcs12 --server.ssl.key-alias=tomcat --server.ssl.key-password=password```

## Configuring clients

A *swidtag* file needs to be placed at the root of the classpath of the client application. To download this file, use the download service for Automation Decision Services (<DECISION_DESIGNER_SERVER_URL>/download/).

For more information about the client application setup, see the IBM Automation Decision Services product documentation.
