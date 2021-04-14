# [DRAFT]

# ODM Metering Service Helm chart (ibm-odm-metering)

The [IBM Operational Decision Manager metering service](https://github.com/ODMDev/decisions-metering) Helm chart `ibm-odm-metering` is used to deploy the consumption metering service in a Kubernetes environment.

## Introduction

The IBM License Service provides information about the usage of decision artifacts and executed decisions. Users of subscription services can obtain details about these billable artifacts. The license covers consumption in the form of traffic between RuleApps and client applications.

For more information, see the [Operational Decision Manager documentation](https://www.ibm.com/support/knowledgecenter/SSQP76_8.10.x/com.ibm.odm.kube/topics/con_k8s_licensing_metering.html).

## Chart Details

The `ibm-odm-metering` Helm chart is a package of preconfigured Kubernetes resources that bootstrap the deployment of an ODM consumption metering service on a Kubernetes cluster. Configuration parameters are available to customize some aspects of the deployment. However, the chart is designed to get you up and running as quickly as possible with appropriate default values. If you accept the default values, you can begin sending metering data to ODM immediately.

The `ibm-odm-metering` chart deploys a single container with the ODM consumption metering service.

When an  `ibm-odm-metering` instance is running, the endpoint URL can be referenced in an ODM deployment through the parameter `customization.meteringServerUrl`

## Prerequisites

- Kubernetes 1.11+ 
- Helm 3.2 and later versions
- One PersistentVolume needs to be created prior to installing the chart if the parameters `persistence.enabled=true` and `persistence.dynamicProvisioning=false`. By default, dynamic provisionning is enabled.
- Review  and accept the product license:
  - Set license=view to print the license agreement
  - Set license=accept to accept the license

Ensure you have a good understanding of the underlying concepts and technologies:
- Helm chart, Docker, containers
- Kubernetes
- Helm commands
- Kubernetes command line tool

Before you install the ODM consumption metering service, you must gather all the configuration settings that you want to use for your release. For more details, refer to the [values section](#values) at the end of this page.

## Resources Required

### Minimum Configuration

|   | CPU Minimum (m) | Memory Minimum (Mi) |
| ---------- | ----------- | ------------------- |
| ODM services | 0.25           | 128Mi                  |


## Installing the Chart

The following instructions must be executed by a namespace administrator.

Add the odm-metering chart repository:

```console
$ helm repo add odm-metering  https://odmdev.github.io/decisions-metering/charts/stable/
$ helm repo update
```

To install a release named `my-odm-metering-release` and with the default configuration, run the following command:

```console
$ helm install my-odm-metering-release --set license=accept odm-metering/ibm-odm-metering
```

> **Tip**: List all existing releases with the `helm list` command.

### OpenShift console

You can add the ODM metering Helm chart repository in the OpenShift catalog.

Then, you are able to use the OpenShift console to instanciate the Helm chart.

There are two ways to declare the Helm chart repository in the OpenShift catalog:
1. Using the command line 

```console
$ oc apply -f https://odmdev.github.io/decisions-metering/charts/openshift/HelmRepository.yaml
```

2. Using the OpenShift Web console

- Click the + button (import resource) at the top of the page.
- Copy / Paste the following YAML excerpt.
```yaml
apiVersion: helm.openshift.io/v1beta1
kind: HelmChartRepository
metadata:
  name: odm-metering-repo
spec:
  connectionConfig:
    url: 'https://odmdev.github.io/decisions-metering/charts/stable/'
```

This is to be done only once in your cluster.

Then, you can use the Helm chart in the OpenShift console.
Here is the procedure:
1. Go to the Developer view.
2. Create a project: odm-metering
3. Click the Topology menu button. 
4. Click the `From Catalog` item in the right side.
5. Select the `Helm Charts` toggle button.
6. Search `odm`
7. Click the `Ibm Odm Metering` item and install the IBM ODM Metering.

## Configuration

To see all configurable options with detailed comments, visit the  [values section](#values) at the end of this page, or run the following configuration command:
```console
$ helm show values odm-metering/ibm-odm-metering
```

Using Helm, you specify each parameter with a `--set key=value` argument in the `helm install` command.
For example:

```console
$ helm install my-odm-metering-release \
  --set license=accept \
  odm-metering/ibm-odm-metering
```

It is also possible to use a custom-made .yaml file to specify the values of the parameters when you install the chart.
For example:

```console
$ helm install --set license=accept my-odm-metering-release -f values.yaml odm-metering/ibm-odm-metering
```

> **Tip**: The default values are in the `values.yaml` file of the `ibm-odm-metering` chart.

The release is an instance of the `ibm-odm-metering` chart: The ODM consumption Metering service is now running in a  Kubernetes cluster.

### Verifying the Chart

When your pods are up and running, you can access the metering service.
Follow the instructions displayed by the helm install. They instruct you how to retrieve the ODM metering service. 
 
```console
helm get notes my-odm-metering-release
```

Yo can then:
  * Open a browser with the odm metering URL. 

You should get the message: 
  
  ```Operational Decision Manager usage reporting service```
  
  * Set this URL as value for the Helm chart parameter `customization.meteringServerUrl`.

When the service is available, you can obtain a zip of the License Service files by using the /backup rest-api endpoint. 
In a browser, access it using meteringServerUrl/backup
or using the curl command line :

```console
curl -k meteringServerUrl/backup -o backup.zip
```

### Uninstalling the chart

To uninstall and delete a release named `my-odm-metering-release`, use the following command:

```console
$ helm delete my-odm-metering-release
```

The command removes all the Kubernetes components associated with the chart, except any Persistent Volume Claims (PVCs).  This is the default behavior of Kubernetes, and ensures that valuable data is not deleted.  In order to delete the ODM data, you can delete the PVC using the following command:

```console
$ kubectl delete pvc <release_name>-odm-pvclaim -n <namespace>
```

### Service Account Requirements

By default, the chart creates and uses the serviceAccount named `<releasename>-ibm-odm-metering-service-account`.

The serviceAccount should be granted the appropriate PodSecurityPolicy or SecurityContextConstraints depending on your cluster. Refer to the [PodSecurityPolicy Requirements](#podsecuritypolicy-requirements) or [Red Hat OpenShift SecurityContextConstraints Requirements](#red-hat-openshift-securitycontextconstraints-requirements) documentation.

You can also configure the chart to use a custom serviceAccount. In this case, a cluster administrator can create a custom serviceAccount and the namespace administrator is then able to configure the chart to use the created serviceAccount by setting the parameter `serviceAccountName`:

```console
$ helm install my-odm-metering-release \
  --set license=accept \
  --set serviceAccountName=my-sa \
  ibm-charts/ibm-odm-metering-service
```

### PodSecurityPolicy Requirements

This chart requires a PodSecurityPolicy to be bound to the target namespace prior to installation. To meet this requirement, a specific cluster and namespace might have to be scoped by a cluster administrator.

The predefined PodSecurityPolicy name [`ibm-restricted-psp`](https://ibm.biz/cpkspec-psp) has been verifed for this chart. If your target namespace is bound to this PodSecurityPolicy, you can proceed to install the chart.


This chart also defines a custom PodSecurityPolicy which can be used to finely control the permissions/capabilities needed to deploy this chart.

A cluster administrator can create the custom PodSecurityPolicy and the ClusterRole by applying the following descriptor files in the appropriate namespace:
* Custom PodSecurityPolicy definition:

```yaml
apiVersion: extensions/v1beta1
kind: PodSecurityPolicy
metadata:
  name: ibm-odm-psp
spec:
  allowPrivilegeEscalation: false
  forbiddenSysctls:
  - '*'
  fsGroup:
    ranges:
    - max: 65535
      min: 1
    rule: MustRunAs
  requiredDropCapabilities:
  - ALL
  runAsUser:
    rule: MustRunAsNonRoot
  seLinux:
    rule: RunAsAny
  supplementalGroups:
    ranges:
    - max: 65535
      min: 1
    rule: MustRunAs
  volumes:
  - configMap
  - emptyDir
  - projected
  - secret
  - downwardAPI
  - persistentVolumeClaim
```

* Custom ClusterRole for the custom PodSecurityPolicy:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: ibm-odm-clusterrole
rules:
- apiGroups:
  - extensions
  resourceNames:
  - ibm-odm-psp
  resources:
  - podsecuritypolicies
  verbs:
  - use
```

### Red Hat OpenShift SecurityContextConstraints Requirements

This chart requires SecurityContextConstraints (scc) to be granted to the serviceAccount prior to installation.
A cluster administrator can either bind the SecurityContextConstraints to the target namespace or add the scc specifically to the serviceAccount.

The predefined SecurityContextConstraints name [`restricted`](https://ibm.biz/cpkspec-scc) has been verified for this chart. In OpenShift, `restricted` is used by default for authenticated users.

To use the `restricted` scc, you must define the `customization.runAsUser` parameter as empty because the restricted scc requires to use an arbitrary UID. As it is the default value, you can omit it.

```console
$ helm install my-odm-metering-release \
  odm-metering/ibm-odm-metering
```

This chart also defines custom SecurityContextConstraints which can be used to finely control the permissions/capabilities needed to deploy this chart.

You can apply the following YAML file to create the custom SecurityContextConstraints.
* Custom SecurityContextConstraints definition:

```yaml
apiVersion: security.openshift.io/v1
kind: SecurityContextConstraints
metadata:
  annotations:
  name: ibm-odm-scc
allowHostDirVolumePlugin: false
allowHostIPC: false
allowHostNetwork: false
allowHostPID: false
allowHostPorts: false
allowPrivilegedContainer: false
allowPrivilegeEscalation: false
allowedCapabilities: []
allowedFlexVolumes: []
allowedUnsafeSysctls: []
defaultAddCapabilities: []
defaultPrivilegeEscalation: false
forbiddenSysctls:
  - "*"
fsGroup:
  type: MustRunAs
  ranges:
  - max: 65535
    min: 1
readOnlyRootFilesystem: false
requiredDropCapabilities:
- ALL
runAsUser:
  type: MustRunAsNonRoot
seccompProfiles:
- docker/default
seLinuxContext:
  type: RunAsAny
supplementalGroups:
  type: MustRunAs
  ranges:
  - max: 65535
    min: 1
volumes:
- configMap
- downwardAPI
- emptyDir
- persistentVolumeClaim
- projected
- secret
priority: 0
```



## Architecture

- Only the AMD64 / x86_64 architecture is supported.


## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| license | string | `` | Read and accept the license agreement. Possible values are : 'accept' / 'view' / 'not accepted'. |
| networkPolicy.enabled | bool | true | Specify whether to enable the network policy. |
| customization.runAsUser | number | null | Specify whether to enable the network policy. If left empty, Kubernetes allocates a random UID (OpenShift).   |
| customization.processingInitialDelay | number | 6000 | The rate in milliseconds at which usage is processed and written to the license files. |
| customization.processingRate | number | 60000 | The rate in milliseconds at which usage is processed and written to the license files. |
| customization.securitySecretRef | string | | Specify the name of the secret that contains the TLS certificate you want to use. If the parameter is left empty, a default certificate is used. |  
| livenessProbe.initialDelaySeconds | number | 200 | Specify the number of seconds after the container has started before liveness probe is initiated. |
| livenessProbe.periodSeconds | number | 10 | Specify how often (in seconds) to perform the probe. |
| livenessProbe.failureThreshold | number | 25 | Specify how many times Kubernetes tries before giving up when a pod starts and the probe fails. Giving up means restarting the pod. |
| readinessProbe.initialDelaySeconds | number | 200 | Specify the number of seconds after the container has started before liveness probe is initiated. |
| readinessProbe.periodSeconds | number | 5 | Specify how often (in seconds) to perform the probe. |
| readinessProbe.failureThreshold | number | 40 | Specify how many times Kubernetes tries before giving up when a pod starts and the probe fails. Giving up means restarting the pod. |
| persistence.enabled | bool | true | Specify whether to enable persistence for the files in a persistent volume. |
| persistence.useDynamicProvisioning | bool | true | When this parameter is false, the binding process selects an existing volume. Ensure that an unbound volume exists before you install the chart. |
| persistence.storageClassName | string | "" | Persistent Volume Claim to store License Service metering and database files. |
| persistence.storagePvc | string | "" | Specify the name of the persistent volume claim that stores the metering files. |
| persistence.resources.requests.storage | string | 2Gi | Specify the storage size for persistent volume. |
| image.pullPolicy | string | IfNotPresent | Specify the pull policy for the Docker image.  'Always'/'IfNotPresent'/'Never'. |
| serviceAccountName | string | "" | Specify the ServiceAccount to use in Kubernetes. |
| service.enableRoute | bool | true | Specify whether to create OpenShift routes automatically. If true, the routes are created for all ODM components. |
| service.hostname | string | "" | Specify the hostname used by the created routes. |
| service.type | string | "NodePort" | Specify the service type. |
| resources.requests.cpu | string | 0.25 | Specify the requested CPU |
| resources.requests.memory | string | 128Mi | Specify the requested memory. |
| resources.limits.cpu | string | 0.5 | Specify the CPU limit. |
| resources.limits.memory | string | 512Mi | Specify the memory limit. |

## Custom Certificate

The metering service is provided with an HTTPS secured protocol. The default certificate is compliant with the ODM Docker images https://github.com/ODMDev/odm-ondocker If you want to provide your own certificate, create a secret that encapsulates the server.crt certificate and the server.key private key files, and then set the `customization.securitySecretRef` parameter with this secret. 

For example :

```console
openssl req -x509 -nodes -days 1000 -newkey rsa:2048 -keyout mycompany.key -out mycompany.crt -subj "/CN=*.mycompany.com/OU=it/O=mycompany/L=Paris/C=FR"
```

Create a generic Kubernetes secret:

```console
kubectl create secret generic mysecuritysecret --from-file=server.crt=mycompany.crt --from-file=server.key=mycompany.key
```

Install the ibm-odm-metering release by providing this security secret:

```console
$ helm install my-odm-metering-release --set license=accept --set customization.securitySecretRef=mysecuritysecret odm-metering/ibm-odm-metering
```


## Storage

- Persistent storage using Kubernetes dynamic provisioning. Uses the default storageclass defined by the Kubernetes admin or by using a custom storageclass which will override the default.
  - Set global values to:
    - persistence.enabled: true (default)
    - persistence.useDynamicProvisioning: true (default)
  - Specify a custom storageClassName per volume or leave the value empty to use the default storageClass.


- Persistent storage using a predefined PersistentVolumeClaim or PersistentVolume that is set up prior to the deployment of this chart.
  - Set global values to:
    - persistence.enabled: true (default)
    - persistence.useDynamicProvisioning: false 
    - persistence.storagePvc: "YourExistingPVC"
  - The Kubernetes binding process selects a pre-existing volume based on the accessMode and size.

## Rest-api endpoint to get a zip archive of the License Service files

When the metering service is deployed, you can get a zip archive of the License Service files by using the rest-api endpoint /backup. 

## Limitations

Only one pod can be instanciated for the metering service.

## Documentation

For more information, see [ODM documentation](https://www.ibm.com/support/knowledgecenter/SSQP76_8.10.x/com.ibm.odm.kube/topics/con_k8s_licensing_metering.html).
