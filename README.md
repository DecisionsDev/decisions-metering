# IBM Decisions usage metering service

[IBM License Metric Tool](https://www.ibm.com/support/knowledgecenter/SS8JFY_9.2.0/com.ibm.lmt.doc/welcome/LMT_welcome.html) inventories IBM software, and monitors its usage and compliance. The Decisions usage metering service produces license files that are compliant with the tool and based on the observed usage of IBM Decisions software.

## IBM Operational Decision Manager
To configure metering for use with IBM Operation Decision Manager, see the following documentation:[ODM Readme](README_ODM.md)

## IBM Automation Decision Services
To configure metering for use with IBM Automation Decision Services, see the following documentation: [ADS Readme](README_ADS.md)

## Troubleshooting

To troubleshoot faulty behavior:

- Ensure there is no HTTP port conflict that can prevent the service from starting.
- Check that the service is responding to HTTP requests, for example, load the root server URL (by default ```http://localhost:8888/```) with a browser.
- Check that the service is accessible from the server environment where the clients are configured.
- Review the client configuration and make sure that the settings are correct.
- Enable low-level logging by using ```----com.ibm.decision.metering.ilmt.service.loggingLevel=TRACE```, and perform a few decision services executions. The corresponding usage appears in the log after some time, depending on the configuration of the metering service.
- Monitor and check the content of the output directory of the license files to determine whether files are being created.