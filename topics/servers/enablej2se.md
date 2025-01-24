# Enabling metering in custom Java SE rule applications

You use the res-setup Ant task to configure each artifact for which you want to enable metering.

## About this task
You enable the metering feature in a Java SE environment as described below. The metering parameters are described in [Setting up Decision Server to integrate with the metering services](../dssetup.md).

**Procedure**
1. Run the following command to generate a ra.xml file with the metering feature enabled:

          cd <odm_install_dir>/executionserver/bin
          <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dxu.config.in=ra.xml -Dxu.config.out=<my_output_dir>/ra-out.xml -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=<meteringclientID> -Dmetering.send.usages=true setup-metering

   The ra-out.xml file is generated, and the metering feature is enabled, for example:

          <config-property>
          <config-property-name>plugins</config-property-name>
          <config-property-type>java.lang.String</config-property-type>
          <config-property-value>{pluginClass=Metering,server.url=http://host.company.com:8888,api.key=ABC,enable=true,send.usages=true,instance.identifier=MyApplication1}"</config-property-value>
          </config-property>

If you are embedding or packaging Java™ rule sessions in a web container, you must package the generated ra.xml file as described in [Packaging Java rule sessions for Java SE](https://www.ibm.com/docs/en/odm/9.0.0?topic=factories-packaging-java-rule-sessions-java-se).

2. Optional: When more than one Decision Server instance connects to the metering service, in particular in the case of a cluster, each instance must be uniquely identified by using the attributes metering.install.directory and metering.instance.identifier.
3. Restart the application server.

## Results

The Decision Server data is now sent to your metering service.

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2025
