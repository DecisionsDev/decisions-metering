
# New: IBM Container Registry

IBM® is now hosting product images on the IBM Container Registry, *icr.io*. You can obtain the IBM Operational Decision Manager for Developers image without authenticating by using this IBM-controlled source: *icr.io/cpopen/odm-k8s*.

```console
docker pull icr.io/cpopen/odm-k8s/odm-metering-service:9.0-amd64
```

# Quick Reference

-	**Where to get help**:

    [ODM developer community](https://developer.ibm.com/odm/)

    [ODM Licensing and metering documentation](https://www.ibm.com/docs/en/odm/9.0.0?topic=kubernetes-licensing-metering)


-	**Where to file issues**:  
  https://github.com/ODMDev/decisions-metering/issues

-	**Maintained by**:  IBM ODM Team

-	**Supported architectures**:  
 `amd64`
-	**Source of this description**:
        https://github.com/ODMDev/decisions-metering/tree/master/docker

-	**Supported Docker versions**:  
	[latest release](https://github.com/docker/docker-ce/releases/latest) (up to version 19, on a best-effort basis)


-	**Kubernetes Helm chart**:  
	[ODM Metering Helm Charts](https://github.com/ODMDev/decisions-metering/blob/master/charts/ibm-odm-metering/README.md)

# Overview

The Operational Decision Manager (ODM) usage metering service image allows you to generate license files that are compliant with the [IBM License Metric Tool](https://www.ibm.com/support/knowledgecenter/SS8JFY_9.2.0/com.ibm.lmt.doc/welcome/LMT_welcome.html). These license files are based on the observed usage of ODM software.

See the License section at the end of this page for restrictions on the use of this image.

# Usage

The image contains the metering service which exposes HTTP on port 8888 and HTTPS on port 9999.

You must accept the license before you launch the image. The license is available at the end of this page.

```console
docker run -e LICENSE=accept  -p 8888:8888 -p 9999:9999 icr.io/cpopen/odm-k8s/odm-metering-service:9.0-amd64
```

When the server is started, use the URL http://localhost:8888 or https://localhost:9999 to display a welcome page. <!-- markdown-link-check-disable-line -->

The metering service receives usage information from Operational Decision Manager and aggregates it.

When the service is available, you can get a zip archive of the ILMT files by using the /backup REST API endpoint.
In a browser, access the zip archive by using http://localhost:8888/backup or https://localhost:9999/backup <!-- markdown-link-check-disable-line -->
or use the following curl command:

<!-- markdown-link-check-disable -->
```console
curl http://localhost:8888/backup -o backup.zip
curl -k https://localhost:9999/backup -o backup.zip
```
<!-- markdown-link-check-enable -->

## Storage

To avoid loosing data when you delete the Docker image container, store the database outside of the ODM Metering Docker image container, in a locally mounted host volume (-v $PWD/DB:/config/storage/DB).

You can also store the license files by creating a volume (-v $PWD/ILMT:/config/storage/ILMT).

```console
docker run -e LICENSE=accept  -p 8888:8888 -p 9999:9999 -v $PWD/DB:/config/storage/DB -v $PWD/ILMT:/config/storage/ILMT  -e com.ibm.decision.metering.ilmt.service.ILMToutputDirectory=/config/storage/ILMT -e com.ibm.decision.metering.ilmt.service.databaseFilePath=/config/storage/DB  icr.io/cpopen/odm-k8s/odm-metering-service:9.0-amd64
```

When you first run this command, it creates the metering files in your local folder. When restarting the metering service, it reads and updates these files.

## Configuration

 You can modify the default metering properties by providing your own `mybootstrap.properties` file (-v $PWD/mybootstrap.properties:/config/bootstrap.properties).

 The default `bootstrap.properties` file contains the following properties:

```console
# The log level that is used by the application. Possible values include ERROR, WARN, INFO, DEBUG, and TRACE.
METERING_LOGGINGLEVEL=INFO
# The rate in milliseconds at which usage is processed and written to the license files.
METERING_PROCESSINGRATE=60000
# The delay in milliseconds before the first processing occurs after the service is started.
METERING_PROCESSING_INITIAL_DELAY=6000
```

To configure the metering service with the configuration property file you can use:
 ```console
docker run -e LICENSE=accept -p 8888:8888 -p 9999:9999 -v $PWD/mybootstrap.properties:/config/bootstrap.properties icr.io/cpopen/odm-k8s/odm-metering-service:9.0-amd64
```

## Security

The metering service is provided with an HTTPS secured protocol.

The default certificate is compliant with the ODM Docker images https://github.com/ODMDev/odm-ondocker

If you want to provide your own certificate, set two volumes, one for the `server.crt` certificate (-v $PWD/mycompany.crt:/config/resources/certificate/server.crt) file, and one for the  `server.key` private key  (-v $PWD/mycompany.key:/config/resources/certificate/server.key) file.

For example, generate a certificate with this command:

 ```console
openssl req -x509 -nodes -days 1000 -newkey rsa:2048 -keyout mycompany.key -out mycompany.crt -subj "/CN=*.mycompany.com/OU=it/O=mycompany/L=Paris/C=FR"
```

To use it, run the following docker command:

 ```console
docker run -e LICENSE=accept -p 8888:8888 -p 9999:9999 -v $PWD/mycompany.crt:/config/resources/certificate/server.crt -v $PWD/mycompany.key:/config/resources/certificate/server.key icr.io/cpopen/odm-k8s/odm-metering-service:9.0-amd64
```

  # License

  The Docker files and associated scripts are licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
