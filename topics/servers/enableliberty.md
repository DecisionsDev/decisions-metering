# Enabling metering on Liberty

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
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/WLP/DecisionService.war -Dmetering.war.out=<my_output_dir>/DecisionService.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering
    
    *Testing and simulation*
    
   The following command generates a DecisionRunner.war file with the metering feature enabled.

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/WLP/DecisionRunner.war -Dmetering.war.out=<my_output_dir>/DecisionRunner.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering
        
   The following command generates a testing.war file with the metering feature enabled.

        cd <odm_install_dir>/executionserver/bin
        <odm_install_dir>/shared/tools/ant/bin/ant -f ressetup.xml -Dmetering.war.in=../applicationservers/WLP/testing.war -Dmetering.war.out=<my_output_dir>/testing.war -Dmetering.enable=true -Dmetering.server.url=http://<odmmeteringservicehost>:<odmmeteringserviceport> -Dmetering.api.key=<irrelevant,putanything> -Dmetering.instance.identifier=setup-metering

2. Deploy the generated output files to your application server by copying the files to the Liberty apps directory.
3. Optional: When more than one Decision Server instance connects to the metering service, in particular in the case of a cluster, each instance must be uniquely identified by using the attributes metering.install.directory and metering.instance.identifier.
4. Restart the application server.

## Distribute an SSL certificate to clients

As part of the metering setup, you must distribute an SSL certificate to the clients.

**Procedure**

1. Extract an SSL certificate from a keystore:

   ```keytool -export -keystore keystore.p12 -alias client -file myCertificate.crt```

2. Import the SSL certificate into the default truststore of the Liberty server: 

   ```keytool -importcert -file myCertificate.crt -alias client -keystore $WLP_HOME/usr/servers/odm<version>/resources/security/truststore.jks```

3. Enable SSL communication in the Liberty server by adding SSL entries in the $WLP_HOME/usr/servers/odm<version>/server.xml file:

   ```
   <sslDefault sslRef="tls12" />
   <ssl id="tls12" keyStoreRef="defaultKeyStore" trustStoreRef="defaultTrustStore" serverKeyAlias="localhost" sslProtocol="TLSv1.2" />
   <ssl id="tlsall" keyStoreRef="defaultKeyStore" sslProtocol="SSL_TLSv2" />
   <keyStore id="defaultKeyStore" password="changeit" location="keystore.jks" />
   <keyStore id="defaultTrustStore" password="changeit" location="truststore.jks" /> 
   ```

4. Start the Liberty server:

   ```$WLP_HOME/bin/start odm<version>```

## Results

The Decision Server data is now sent to your metering service.

Back to [Setting up Decision Server to integrate with the metering services](../dssetup.md)

© Copyright IBM Corporation 2020

