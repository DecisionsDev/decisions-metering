{{/*
Expand the name of the chart.
*/}}
{{- define "name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "fullname" -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.secret.fullname" -}}
{{- $name := default "odm-secret" .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.network-policy.fullname" -}}
{{- $name := default "odm-network-policy" .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm-metering-security-secret-volume.fullname" -}}
{{- printf "%s-%s" .Release.Name "odm-metering-security-secret-volume" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.test.fullname" -}}
{{- $name := default "odm-test" .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.test-configmap.fullname" -}}
{{- $name := default "odm-test-configmap" .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.persistenceclaim.fullname" -}}
{{- $name := default "odm-pvclaim" .Values.nameOverride -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm.repository.name" -}}
{{- $reponame := default "ibmcom" .Values.image.repository -}}
{{- printf "%s"  $reponame |  trimSuffix "/" -}}
{{- end -}}

{{- define "odm.metering-options-configmap.fullname" -}}
{{- printf "%s-%s" .Release.Name "odm-metering-options-configmap" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm-metering-options-volume.fullname" -}}
{{- printf "%s-%s" .Release.Name "odm-metering-options-volume" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "odm-metering-options-dir" -}}
"/config/bootstrap.properties"
{{- end -}}

{{- define "odm-security-dir" -}}
"/config/resources/certificate"
{{- end -}}

{{- define "odm-keystore-password-key" -}}
"keystore_password"
{{- end -}}

{{- define "odm-keystore-alias-key" -}}
"keystore-alias"
{{- end -}}

{{/*
Check if tag contains specific platform suffix and if not set based on kube platform
*/}}
{{- define "platform" -}}
{{- if not .Values.image.arch }}
  {{- if (eq "linux/amd64" .Capabilities.KubeVersion.Platform) }}
    {{- printf "-%s" "amd64" }}
  {{- end -}}
  {{- if (eq "linux/ppc64le" .Capabilities.KubeVersion.Platform) }}
    {{- printf "-%s" "ppc64le" }}
  {{- end -}}
  {{- if (eq "linux/s390x" .Capabilities.KubeVersion.Platform) }}
    {{- printf "-%s" "s390x" }}
  {{- end -}}
{{- else -}}
  {{- if eq .Values.image.arch "amd64" }}
    {{- printf "-%s" "amd64" }}
  {{- else -}}
    {{- printf "-%s" .Values.image.arch }}
  {{- end -}}
{{- end -}}
{{- end -}}

{{/*
Return arch based on kube platform
*/}}
{{- define "ibm-odm-metering.arch" -}}
  {{- if (eq "linux/amd64" .Capabilities.KubeVersion.Platform) }}
    {{- printf "%s" "amd64" }}
  {{- end -}}
  {{- if (eq "linux/ppc64le" .Capabilities.KubeVersion.Platform) }}
    {{- printf "%s" "ppc64le" }}
  {{- end -}}
  {{- if (eq "linux/s390x" .Capabilities.KubeVersion.Platform) }}
    {{- printf "%s" "s390x" }}
  {{- end -}}
{{- end -}}

{{- define "odm-spec-security-context" -}}
hostNetwork: false
hostPID: false
hostIPC: false
securityContext:
  runAsNonRoot: true
  {{- if .Values.customization.runAsUser }}
  runAsUser: {{ .Values.customization.runAsUser }}
  {{- end }}
{{- end -}}

{{- define "odm-security-context" -}}
securityContext:
  {{- if .Values.customization.runAsUser }}
  runAsUser: {{ .Values.customization.runAsUser }}
  {{- end }}
  runAsNonRoot: true
  privileged: false
  readOnlyRootFilesystem: false
  allowPrivilegeEscalation: false
  capabilities:
    drop:
    - ALL
{{- end -}}

{{- define "odm-additional-labels" -}}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/name: {{ template "name" . }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
{{- end -}}


{{- define "odm-serviceAccountName" -}}
{{- if .Values.serviceAccountName -}}
serviceAccountName: {{ .Values.serviceAccountName }}
{{- else -}}
serviceAccountName: {{ template "fullname" . }}-service-account
{{- end }}
{{- end -}}

{{- define "odm-route.fullname" -}}
{{- printf "%s-%s" .Release.Name "route" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Image tag or digest.
*/}}{{- define "image.tagOrDigest" -}}
{{- if hasPrefix "sha256" .root.Values.image.tag -}}
image: {{ template "odm.repository.name" .root }}/{{ .containerName }}@{{ .root.Values.image.tag }}
{{- else -}}
image: {{ template "odm.repository.name" .root }}/{{ .containerName }}:{{ .root.Values.image.tag }}_{{ .root.Chart.Version }}{{ template "platform" .root }}
{{- end -}}
{{- end }}

{{- define "metering-service-type" -}}
{{- if .Values.service.enableRoute -}}
type: ClusterIP
{{- else -}}
type: {{ .Values.service.type }}
{{- end }}
{{- end -}}
