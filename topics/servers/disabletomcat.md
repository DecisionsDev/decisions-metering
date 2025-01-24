# Disabling metering on Tomcat

You use the res-setup Ant task to disable metering in the Decision Server runtime. For information on the res-setup Ant task, see [Setting up Decision Server to integrate with the metering services](../dssetup.md). 

**Procedure**

Disable the metering feature on a Tomcat application server by setting the metering.enable attribute to false as described for the following Decision Server runtime features.

*Hosted Transparent Decision Service*

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/tomcat8/DecisionService.war -Dmetering.war.out=<my_output_dir>/DecisionService.war -Dmetering.enable=false setup-metering
    
*Testing and simulation*

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=DecisionRunner.war -Dmetering.war.out=<my_output_dir>/DecisionRunner.war -Dmetering.enable=false setup-metering
    
        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/tomcat8/testing.war -Dmetering.war.out=<my_output_dir>/testing.war -Dmetering.enable=false setup-metering

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2025

