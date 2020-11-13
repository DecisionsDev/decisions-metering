ARG FROMLIBERTY
ARG FROMLIBERTYBUILD
FROM maven:3.5.2-jdk-8-alpine AS builder
ARG ODMDOCKERDIR
ENV ODMDOCKERDIR $ODMDOCKERDIR
ENV SCRIPT /script
ENV APPS /config/apps

RUN mkdir -p $SCRIPT && \
		mkdir -p /config/resources



# Install missing require package in the alpine builder image
RUN apk add --no-cache bash ca-certificates wget &&  mkdir -p $APPS
# Build welcome page

#RUN cd /welcomepage; mvn -B clean install | grep -v 'Download.*' && mkdir -p $APPS



# Decision Server Runtime
COPY ./target/metering-service.jar $APPS

RUN chmod -R 777 $APPS && ls $APPS/

FROM ${FROMLIBERTYBUILD} AS oidc-liberty-builder
ARG ODMDOCKERDIR
ENV ODMDOCKERDIR $ODMDOCKERDIR
USER root
ENV SCRIPT /script
COPY  $ODMDOCKERDIR/scripts $ODMDOCKERDIR/wlp* /opt/wlppackage/
# TODO
RUN mkdir $SCRIPT && mv /opt/wlppackage/*.sh $SCRIPT && $SCRIPT/installFeatures.sh
COPY --chown=1001:0 $ODMDOCKERDIR/config /config
#COPY --chown=1001:0 $ODMDOCKERDIR/decisionserver/decisionserverruntime/config /config
#COPY --chown=1001:0 $ODMDOCKERDIR/common/config /config
#COPY --chown=1001:0 $ODMDOCKERDIR/common/security/ltpa.keys /config/resources/security/ltpa.keys
#COPY --chown=1001:0 $ODMDOCKERDIR/common/security/keystore.jks /config/security/keystore.jks
#COPY --chown=1001:0 $ODMDOCKERDIR/common/security/truststore.jks /config/security/truststore.jks
#COPY --chown=1001:0 $ODMDOCKERDIR/common/drivers /config/resources
#COPY --chown=1001:0 $ODMDOCKERDIR/common/config/jvm/jvm.options /config/configDropins/overrides/jvm.options
RUN chmod -R 777 /config

FROM ${FROMLIBERTY}
ARG ODMDOCKERDIR
ARG ODMVERSION
ARG DECISIONSERVERCONSOLE_NAME
LABEL maintainer="ODMDev odmdev_open_source_user@wwpdl.vnet.ibm.com, Laurent GRATEAU <laurent.grateau@fr.ibm.com>"
MAINTAINER ODMDev odmdev_open_source_user@wwpdl.vnet.ibm.com, Laurent GRATEAU <laurent.grateau@fr.ibm.com>

LABEL name="Decision Metering Service"
LABEL io.k8s.display-name="Decision Metering Service"
LABEL description="The Decision Metering Service exposes a metering service as \
 a web service with REST/JSON endpoints."


LABEL summary="The Decision Metering REST API."
LABEL io.k8s.description="The Decision Metering REST API."

LABEL vendor="IBM"
LABEL version=${ODMVERSION}
LABEL ProductVersion=${ODMVERSION}
LABEL ProductID="IBM Operational Decision Manager for production"
LABEL io.openshift.tags="odm,dba,dbamc"



ENV APP_NAME DecisionMetering
ENV ODMDOCKERDIR $ODMDOCKERDIR
ENV SCRIPT /scripts
ENV APPS /config/apps

COPY --from=oidc-liberty-builder /opt/ibm/wlp /opt/ibm/wlp

# Welcome page
COPY --from=builder $APPS $APPS


# License
# TODO ?
# COPY --from=builder  --chown=1001:0 /licenses /licenses
USER 1001
EXPOSE 8888


