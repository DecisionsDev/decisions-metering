# Disabling metering on other supported application servers

You use the res-setup Ant task to disable metering in the Decision Server runtime. For information on the res-setup Ant task, see [Setting up Decision Server to integrate with the metering services](../dssetup.md).

**Procedure**

Disable the metering feature on application servers by setting the metering.enable attribute to false.

For example, for WebSphere Application Server, the execution unit (XU) is shared by all the Decision Server runtime features (HTDS, testing and simulation) installed on the same node as the XU. Therefore, you disable it once for all the features:

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dxu.in=../applicationservers/WebSphere/jrules-res-xu-WAS.rar -Dxu.out=<my_output_dir>/jrules-res-xu-WAS.rar -Dmetering.enable=false setup-metering

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2024

