# Enabling metering on Tomcat

You use the res-setup Ant task to configure each artifact for which you want to enable metering. 

## About this task

You can enable the metering feature for the following Decision Server runtime features:

- Hosted Transparent Decision Service
- Testing and simulation

The metering parameters are described in [Setting up Decision Server to integrate with the metering services](../dssetup.md).

**Procedure**
1. Run the res-setup Ant task as described below for the following Decision Server runtime features.

    *Hosted Transparent Decision Service*
    
    The following command generates a DecisionService.war file with the metering feature enabled.

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/tomcat8/DecisionService.war -Dmetering.war.out=<my_output_dir>/DecisionService.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering
        
    *Testing and simulation*
    
    The following command generates a DecisionRunner.war file with the metering feature enabled.

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/tomcat8/DecisionRunner.war -Dmetering.war.out=<my_output_dir>/DecisionRunner.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering

    The following command generates a testing.war file with the metering feature enabled.

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/tomcat8/testing.war -Dmetering.war.out=<my_output_dir>/testing.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering

2. Deploy the generated output files to your application server by copying the files to the Tomcat webapps directory.
3. Optional: When more than one Decision Server instance connects to the metering service, in particular in the case of a cluster, each instance must be uniquely identified by using the attributes metering.install.directory and metering.instance.identifier.
4. Restart the application server.

## Results

The Decision Server data is now sent to your metering service.

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2020

