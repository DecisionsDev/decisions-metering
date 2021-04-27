# ODM metering service Helm chart (ibm-odm-metering) on Openshift

## Installing the Metering service with the OpenShift console

You can add the ODM metering Helm chart repository in the OpenShift catalog.

Then, you are able to use the OpenShift console to instanciate the Helm chart.

There are two ways to declare the Helm chart repository in the OpenShift catalog:
1. Using the command line

```console
$ oc apply -f https://odmdev.github.io/decisions-metering/charts/openshift/HelmRepository.yaml
```

2. Using the OpenShift Web console

- Click the + button (import resource) at the top of the page.
- Copy and paste the following YAML excerpt.
```yaml
apiVersion: helm.openshift.io/v1beta1
kind: HelmChartRepository
metadata:
  name: odm-metering-repo
spec:
  connectionConfig:
    url: 'https://odmdev.github.io/decisions-metering/charts/stable/'
```

You do this only once in your cluster.

Then, you can use the Helm chart in the OpenShift console:
1. Go to the Developer view.
2. Create a project: odm-metering
3. Click the Topology menu button.
4. Click the `From Catalog` item in the right side.
5. Select the `Helm Charts` toggle button.
6. Search `odm`
7. Click the `Ibm Odm Metering` item and install the IBM ODM metering service.

Once the metering service is installed, you can use it in ODM. See  Using the metering service with ODM on Kubernetes offering](../
/ibm-odm-metering/README.md#using-the-metering-service-with-odm-on-kubernetes-offering)
