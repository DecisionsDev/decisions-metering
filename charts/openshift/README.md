# ODM Metering Helm Chart (ibm-odm-metering) on OpenShift

## Installing the metering service with the OpenShift console

You can add the ODM metering Helm chart repository in the OpenShift catalog to be able to instanciate the Helm chart through the OpenShift console.

There are two ways to declare the Helm chart repository in the OpenShift catalog:
1. Using the command line

```console
$ oc apply -f https://decisionsdev.github.io/decisions-metering/charts/openshift/HelmRepository.yaml
```

2. Using the OpenShift web console

- Click the + button (import resource) at the top of the page.
- Copy and paste the following YAML excerpt.
```yaml
apiVersion: helm.openshift.io/v1beta1
kind: HelmChartRepository
metadata:
  name: odm-metering-repo
spec:
  connectionConfig:
    url: 'https://decisionsdev.github.io/decisions-metering/charts/stable/'
```

You do this only once in your cluster.

Then, you are ready to use the Helm chart in the OpenShift console:
1. Go to the Developer view.
2. Create a project and name it `odm-metering`.
3. Click the Topology menu item.
4. Click `From Catalog` on the right side.
5. Click the `Helm Charts` toggle button.
6. Search `odm`.
7. Click `Ibm Odm Metering`, and then install the ODM metering service.

After the metering service is installed, you can apply it to ODM. For details, see [Using the metering service with ODM on Kubernetes](../ibm-odm-metering/README.md#using-the-metering-service-with-odm-on-kubernetes)
