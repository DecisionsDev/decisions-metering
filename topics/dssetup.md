# Setting up Decision Server to integrate with the metering service

After building and customizing your metering service, you are ready to use the res-setup Ant task to configure your Operational Decision Manager Rule Execution Server.

## About this task

You can use the ressetup.xml Ant file or write your own to set up the metering feature in Operational Decision Manager. If you use the res-setup.xml template file, some values are predefined such as metering.server.url, but you must customize most of them.

The res-setup task is described at [res-setup](https://www.ibm.com/docs/en/odm/8.12.0?topic=setup-res). To setup metering, use the following guide.

## Summary of res-setup usage for metering configuration

Among the parameters listed in the res-setup task document, the following parameters are used to configure a client of the Operational Decision Manager metering service:

Parameter | Description
:-: | :-:
metering.api.key | This parameter is not used, but it is mandatory. Use an arbitrary value.
metering.server.url | To be set to the URL where the metering service is run, for example, http://host.company.com:8888/odm-metering-service.
metering.enable | To be set to true so that the metering components can run.
metering.send.usages | To be set to true so that actual usages are sent to the metering service.
metering.instance.identifier | If specified, it is used as a reference to identify a metering client instance within Operational Decision Manager. This information appears in the log of the metering service, so it is recommended to be customized.
metering.install.directory | If specified, this information appears as InstanceId information inside the ILMT tag files. You don't necessarily need to customize it. An actual unique software location is used if you do not do so. It needs to start with a "/" character.

Depending on the application server that is running your Operational Decision Manager, pass the following additional parameters:

Server | Description
:-: | :-:
Liberty or Tomcat | Use the parameters *metering.war.in* and *metering.war.out* to set up the metering for an HTDS, Decision Runner or SSP instance, and use the parameters *xu.config.in* and *xu.config.out* to set up the metering in a custom Java SE application using rules.
Other servers | Use the parameters *xu.in* and *xu.out* to set up the metering for an eXecution Unit (XU) instance.

## Table of contents

-   [Enabling metering in J2SE](servers/enablej2se.md)
-   [Disabling metering in J2SE](servers/disablej2se.md)
-   [Enabling metering on Liberty](servers/enableliberty.md)
-   [Disabling metering on Liberty](servers/disableliberty.md)
-   [Enabling metering on Tomcat](servers/enabletomcat.md)
-   [Disabling metering on Tomcat](servers/disabletomcat.md)
-   [Enabling metering on other supported application servers](servers/enablewas.md)
-   [Disabling metering on other supported application servers](servers/disablewas.md)

Back to [IBM Operational Decision Manager usage metering service](../README.md)
