# Project Documentation Context (Non-Obvious Only)

- The root `src/` directory contains only Spring Boot `application.properties` files — all Java source is under `service/src/` and `model/src/`.
- `service/src/main/config/` holds profile-specific `application_ODM.properties` and `application_ADS.properties`; neither is named `application.properties`. The build plugin copies the correct one to `target/classes/application.properties` at `generate-sources` phase.
- `metering_service_default.properties` (in `service/src/main/resources/`) is NOT a Spring Boot properties file — it is loaded programmatically by `Environment.getDefaultProperties()` to supply mandatory defaults and remapping rules before Spring starts.
- `model/src/main/i18n/` (not `resources/`) is the resource directory for the model module's message bundle — it is registered explicitly in `model/pom.xml` `<build><resources>`.
- The `metricsConfiguration` property value is an **entire JSON document** stored as a single property string in `application.properties`. The schema is `MetricConfiguration` → list of `MetricDefinition` each with a `formatter` sub-object. The `formatter.className` field is a relative class name (`.ThresholdMetricFormatter`) resolved against the `reporting` package.
- Integration tests in `service/src/test/` have a `model/` and `util/` sub-package — these are test-only model classes (e.g., `LicenseFile`, `Metric`) for parsing ILMT XML output, distinct from the production model in the `model` module.
- `README_ADS.md` and `README_ODM.md` document the two deployment modes separately; `README.md` is the general entry point.
