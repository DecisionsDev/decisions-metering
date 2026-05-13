ARG FROMLIBERTY
ARG FROMLIBERTYBUILD
ARG ODMDOCKERDIR

FROM ${FROMLIBERTYBUILD} AS oidc-liberty-builder
ARG ODMDOCKERDIR
ENV ODMDOCKERDIR $ODMDOCKERDIR
USER root
ENV INSTALLSCRIPT /installscripts
ENV SCRIPT /scripts
ENV APPS /config/apps

RUN mkdir $INSTALLSCRIPT && mkdir $SCRIPT
RUN mkdir /config/ILMT /config/storage
COPY $ODMDOCKERDIR/scripts $SCRIPT
COPY $ODMDOCKERDIR/installscripts $INSTALLSCRIPT
COPY $ODMDOCKERDIR/wlp* /opt/wlppackage/
# TODO
RUN --mount=type=secret,id=artifactory_user \
	--mount=type=secret,id=artifactory_token \
    if [[ -n $(cat /run/secrets/artifactory_user) ]] ; then \
    mkdir -p /opt/ibm/wlp/etc \
    && echo "mavenCentralMirror.url=https://na.artifactory.swg-devops.com/artifactory/hyc-decisions-team-proxy-maven-virtual" > /opt/ibm/wlp/etc/featureUtility.properties \
    && echo "mavenCentralMirror.user=$(cat /run/secrets/artifactory_user)" >> /opt/ibm/wlp/etc/featureUtility.properties \
    && echo "mavenCentralMirror.password=$(cat /run/secrets/artifactory_token)" >> /opt/ibm/wlp/etc/featureUtility.properties; \
    fi
RUN $INSTALLSCRIPT/installFeatures.sh; \
    rm -f /opt/ibm/wlp/etc/featureUtility.properties
COPY --chown=1001:0 $ODMDOCKERDIR/config /config
COPY --chown=1001:0 $ODMDOCKERDIR/config/jvm.options /config/configDropins/overrides/jvm.options
COPY --chown=1001:0 $ODMDOCKERDIR/security/ltpa.keys /config/resources/security/ltpa.keys
COPY --chown=1001:0 $ODMDOCKERDIR/certificate/server.crt /config/resources/certificate/server.crt
COPY --chown=1001:0 $ODMDOCKERDIR/certificate/server.key /config/resources/certificate/server.key
COPY ./service/target/metering-service-ODM.jar $APPS/metering-service.jar

RUN chmod -R 777 /config $SCRIPT

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
COPY --from=oidc-liberty-builder /scripts /scripts
# License
# TODO ?
# COPY --from=builder  --chown=1001:0 /licenses /licenses
USER 1001
EXPOSE 8888 9999

ENTRYPOINT ["/scripts/runserver.sh"]
CMD ["/scripts/runserver.sh"]
