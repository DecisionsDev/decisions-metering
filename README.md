[![Build Status](https://travis-ci.org/ODMDev/decisions-metering.svg?branch=master)](https://travis-ci.org/ODMDev/decisions-metering)

# IBM Operational Decision Manager usage metering service

[IBM License Metric Tool](https://www.ibm.com/support/knowledgecenter/SS8JFY_9.2.0/com.ibm.lmt.doc/welcome/LMT_welcome.html) inventories IBM software, and monitors its usage and compliance. The Operational Decision Manager usage metering service produces license files that are compliant with the tool and based on the observed usage of Operational Decision Manager software.

## Overview

The service receives metering information from Operational Decision Manager, and aggregates it to produce the license files.
The service application runs as an embedded HTTP server that receives and processes HTTP requests. These HTTP requests are emitted by metering components that can be configured in Operational Decision Manager.
The metering service aggregates reported usage periodically into the license files, which are stored in a directory.
The files show *IBM Operational Decision Manager Server* as the software name, refer to a specific eXecution Unit (XU) or Decision Center Business Console instance, and contain the following metrics:

Name | Product area | Description | Unit
:-: | :-: | :-: | :-:
MILLION_MONTHLY_DECISIONS | Decision Server | Number of ruleset executions | 10^6
THOUSAND_MONTHLY_ARTIFACTS | Decision Center | Number of Decision Center artifacts | 10^3

The service uses a single-file, disk-based database engine to ensure that the service can be stopped and restarted without losing data.

## Prerequisites

<pre><code>
<b>For compilation</b>
Operational Decision Manager 8.10.2 or later version
JDK 1.8
Maven 3.6.3 or later version

<b>For use</b>
Operational Decision Manager 8.10.2 or later version
JRE 1.8
With configuration set for metering.
Please see section the following <i>Configuring the clients</i> section.
</code>
</pre>

## Compiling the service

Follow these steps to compile the service:

1.	Go to *InstallDir*/executionserver/lib and look for the *license_metric_logger_2.1.1.201507131115.jar* file.

> Note: *InstallDir* is the installation directory that contains Operational Decision Manager.

2.	Skip this step if the file is in the directory. If not, do the following steps:
  
    a.	Open *InstallDir*/shared/ilmt.
  
    b.	Locate the JAR file in the *license_metric_logger.zip* file.
  
    c.	Extract the JAR file to the *InstallDir*/executionserver/lib directory.

4.	In the directory that contains the *pom.xml* file, run the one of the following commands:

```mvn clean install```

or 

```mvn clean install -DskipTests``` to avoid running the unit tests
 
> Note: You can dynamically pass the directory to where Operational Decision Manager is installed in the compilation command line:

> ```mvn clean install -Dodm.home=<*InstallDir*>```

The standalone service *metering-service.jar* JAR file is produced in the target directory.

## Running the service

Use the following command to start the service without any customization:

```java -jar metering-service.jar```

To customize settings when you start the service, set the parameters by using a **--** prefix, for example:

```java -jar metering-service.jar --parameterName=parameterValue```

To start the service on port 9090 and set the log level to DEBUG, for example, use the following command:

```java -jar metering-service.jar --server.port=9090 --com.ibm.rules.metering.loggingLevel=DEBUG```

The following parameters are available:

Name | Default value | Description
--- | :-: | ---
server.port | 8888 | The HTTP port on which the service operates. You might have to change it to avoid a conflict.
server.servlet.context-path | / | The context root at which the service operates.
com.ibm.rules.metering.loggingLevel | INFO | The log level that is used by the application. Possible values include ERROR, WARN, INFO, DEBUG, and TRACE.
com.ibm.rules.metering.ILMToutputDirectory | ./ILMT_files | The directory where license files are stored. It must be either a relative path (starting with "./" or "../") or an absolute path (starting with "/"). It should correspond to a directory that is declared in the licence metric tool so that the directory is scanned. Do not use a temporary location.
com.ibm.rules.metering.processingRate | 60000 | The rate in milliseconds at which usage is processed and written to the license files.
com.ibm.rules.metering.processingInitialDelay | 60000 | The delay in milliseconds before the first processing occurs after the service is started.
com.ibm.rules.metering.databaseFilePath | ./odm_usage_metering.data | The file path of the database used by the service internally. Do not use a temporary location.
com.ibm.rules.metering.databaseUser | odm | The user name of the database that is used by the service internally.
com.ibm.rules.metering.databasePassword | odm_metering | The password of the database that is used by the service internally.

## Enable HTTPS in ODM usage metering service

Add following parameters when running the service:

|Name  | Example value |Description  |
|--|--|--|
| server.ssl.key-store | classpath:keystore.p12 | Store the key store that contains the SSL certificate in the classpath. |
| server.ssl.key-store-password | password | The password used to access the key store. |
| server.ssl.key-store-type | pkcs12 | The type of the key store (JKS or PKCS12). |
| server.ssl.key-alias | tomcat | The alias that identifies the key in the key store. |
| server.ssl.key-password | password | The password used to access the key in the key store. |

To start the service on HTTPS, for example, use the following command:

```java -jar metering-service.jar --server.port=8443 --server.ssl.key.store=classpath:keystore.p12 --server.ssl.key-store-password=password --server.ssl.key-store-type=pkcs12 --server.ssl.key-alias=tomcat --server.ssl.key-password=password```

## Configuring the clients

### Decision Server

On WebSphere, JBoss, and Weblogic application servers, each eXecution Unit (XU) deployed instance must be configured for metering.
On Liberty and Tomcat application servers, each HTDS and DecisionRunner deployed instances must be configured for metering, as well as custom-made applications that use J2SE Rule Sessions.
See [Setting up Decision Server to integrate the metering service](topics/dssetup.md).

### Decision Center

Each Decision Center Business Console deployed instance must be configured for metering.
See [Setting up Decision Center to integrate the metering service](topics/dcsetup.md).

## Troubleshooting

To troubleshoot faulty behavior:

- Ensure there is no HTTP port conflict that can prevent the service from starting.
- Check that the service is responding to HTTP requests, for example, load the root server URL (by default ```http://localhost:8888/```) with a browser.
- Check that the service is accessible from the server environment where the clients are configured.
- Review the client configuration and make sure that the settings are correct.
- Enable low-level logging by using ```--com.ibm.rules.metering.loggingLevel=TRACE```, and perform a few ruleset executions. The corresponding usage appears in the log after some time, depending on the configuration of the metering service.
- Monitor and check the content of the output directory of the license files to determine whether files are being created.
