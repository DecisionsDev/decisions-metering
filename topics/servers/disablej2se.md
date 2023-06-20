# Disabling metering in custom Java SE rule applications

You use the res-setup Ant task to disable metering in the Decision Server runtime.  For information on the res-setup Ant task, see [Setting up Decision Server to integrate with the metering services](../dssetup.md).

**Procedure**

Disable the metering feature in J2SE by setting the metering.enable attribute to false as follows:

    cd <odm_install_dir>/executionserver/bin
    <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dxu.config.in=ra.xml -Dxu.config.out=ra-out.xml -Dmetering.enable=false setup-metering 

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2023

