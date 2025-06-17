# Enabling metering on Tomcat

You enable metering for Decision Center through the decisioncenter-configuration.properties file.

**Procedure**
1. Update the Decision Center configuration.

    a. Create a file named decisioncenter-configuration.properties on your disk with the following content.

        com.ibm.rules.metering.enable=true
        com.ibm.rules.metering.send.usages=true
        com.ibm.rules.metering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport>
        com.ibm.rules.metering.api.key=<irrelevant,putanything>
        com.ibm.rules.metering.instance.identifier=DecisionCenter
        com.ibm.rules.metering.install.directory=/opt/IBM/MyServer/ODMRuntime
    
    b. Restart the application server with the additional JVM parameter, as follows:

        <tomcat_home>/bin/startup.sh -Dcom.ibm.rules.decisioncenter.setup.configuration-file=/<path_to_the_decisioncenter-configuration.properties_file>   

2. Optional: When more than one Decision Center instance connects to the metering service, in particular in the case of a cluster, each instance must be uniquely identified by using the attributes metering.install.directory and metering.instance.identifier. 

Here is the list of metering attributes and their descriptions:

|Attribute| Description |
|--|--|
| com.ibm.rules.metering.api.key | If specified, it is used as a reference to identify a metering client instance within Operational Decision Manager. This information appears in the log of the metering service, so it is recommended to be customized. |
| com.ibm.rules.metering.enable | Set this parameter to true to insert or to false to remove the metering plugin in the Decision Center configuration. Optional. |
| com.ibm.rules.metering.application.level.id | Uniquely identifies the application level for a specific server instance. For example: com.ibm.rules.metering.application.level.id=pricing. Mandatory for grouping and aggregation of data. |
| com.ibm.rules.metering.environment.level.id | Uniquely identifies the environment level for a specific server instance. For example: com.ibm.rules.metering.environment.level.id=development. Mandatory for grouping and aggregation of data. |
| com.ibm.rules.metering.host.name | The name of the Operational Decision Manager server that sends the metrics. Allows you to differentiate the servers. Optional. |
| com.ibm.rules.metering.install.directory | If specified, this information appears as InstanceId information inside the ILMT tag files. You don't necessarily need to customize it. An actual unique software location is used if you do not do so. It needs to start with a "/" character. |
| com.ibm.rules.metering.instance.identifier | Identifier to distinguish emitting servers that have the same host name. Optional. For example: MyInstanceID. |
| com.ibm.rules.metering.send.usages | When this parameter is enabled, the usage reporting of the runtime is sent to the IBM Cloud Private server. Value: true or false. Mandatory when com.ibm.rules.metering.enable=true. You must set this parameter to true in order to send the runtime usage reports to your metering service. If this parameter is set to false, the metering feature is enabled but no usage reporting is sent. |
| com.ibm.rules.metering.server.url | Endpoint where the usage metrics are sent. Mandatory when com.ibm.rules.metering.enable=true and com.ibm.rules.metering.send.usages=true. |

## Results

The Decision Center data is now sent to your metering service.

Back to [Setting up Decision Center to integrate with the metering services](../dcsetup.md)

© Copyright IBM Corporation 2025

