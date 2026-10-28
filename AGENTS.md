# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## Stack

Java 17 (compiled), Java 21 (CI runtime), Maven multi-module: `model` (POJO/Jackson) + `service` (Spring Boot 4.x). Two build profiles: **ODM** (default) and **ADS** — profile determines which `application.properties` is packaged and which output JAR name is used.

## Commands

```bash
# Full build (skips tests, requires ODMHOME env var pointing to ODM install)
mvn install --no-transfer-progress -DskipTests=true -Dodm.home=$PWD/../install

# Run unit/integration tests (from repo root)
mvn test -pl service -Dodm.home=$PWD/../install

# Run a single test class
mvn test -pl service -Dtest=EndpointsTest -Dodm.home=$PWD/../install

# Run a single test method
mvn test -pl service -Dtest=ReportingTest#testDecisionsReportingSingleUsageLowValue -Dodm.home=$PWD/../install

# Build with ADS profile
mvn install -DskipTests=true -PADS -Dilmt.jar.file.path=/path/to/license_metric_logger.jar

# Build + docker image (used by CI — requires docker-compose)
bash build.sh
```

## Critical Build Requirements

- The `license_metric_logger` JAR (`com.ibm.license.metric`) is a **system-scope** dependency sourced from `$ODMHOME/executionserver/lib/` for ODM profile or `$ILMT_JAR_PATH` for ADS profile. Maven **will fail** without this JAR on the filesystem.
- `maven-surefire-plugin` is pinned to `2.22.0` in `service/pom.xml` — do not upgrade without verifying JUnit 5 compatibility.
- `snakeyaml` is explicitly excluded from `spring-boot-starter` to avoid a known vulnerability.
- `junit-vintage-engine` is excluded from `spring-boot-starter-test` — only JUnit 5 (`@Test` from `org.junit.jupiter`) should be used in new tests, even though legacy `@RunWith(SpringRunner.class)` still appears in existing test classes.

## Dependency Version Pins (security maintenance)

Versions are **explicitly overriding** Spring Boot's BOM. Track these for CVEs:
- `spring-boot.version=4.1.0` / `spring-framework.version=7.0.8` / `tomcat-embed.version=11.0.25`
- `jackson.version=2.22.1` (also pinned in `model/pom.xml` as `2.22` — keep in sync)
- `logback.version=1.5.34`
- `h2` at `2.2.224` (runtime, test only)
- `org.apache.httpcomponents:fluent-hc` at `4.5.2` (test only) — **flagged outdated**, upgrade to HttpClient5
- `junit:junit:4.13` (test only) — **flagged outdated**, migrate to JUnit 5 only

## Architecture

```
POST /api/startup   → RegistrationController → RegistrationService (H2 REGISTRATION table)
POST /api/usage     → UsageController        → UsageService (H2 USAGE table)
@Scheduled          → ReportHandler          → writes ILMT XML tag files to ILMToutputDirectory
```

- `ReportHandler.doReporting()` is `@Scheduled` at `processingRate` ms (60s prod, **10s in tests**). Tests `Thread.sleep(15000)` to wait for a processing cycle — do not make processing faster without adjusting test sleeps.
- `Environment.METRIC_CONFIGURATION` is a **static mutable global** populated at `@PostConstruct` from `metricsConfiguration` JSON property. Altering metric definitions requires modifying the JSON in `application.properties`.
- `metering_service_default.properties` uses custom `target_` and `prefix_` key prefixes to map app-specific property names to Spring property names — this is handled in `MeteringServiceApplication.retrieveProperties()`. Standard Spring property injection does NOT resolve these custom keys directly.
- All logging must go through `Environment.getLogger()` (returns a single named SLF4J logger `com.ibm.decision.metering.ilmt.service`), not `LoggerFactory.getLogger(MyClass.class)`.
- All messages must be retrieved via `Environment.getMessage(Messages.KEY)` — never hardcode log/response strings. Message keys are defined in `service/src/main/java/.../util/Messages.java` and resolved from `metering_service_messages.properties`.

## Code Style

- All entity IDs are `UUID.randomUUID().toString()` (String, not numeric auto-increment).
- JPA entities use `jakarta.persistence.*` (not `javax.persistence.*` — Spring Boot 4 / Jakarta EE 10).
- Spring Boot imports: use `org.springframework.boot.webmvc.autoconfigure.*` and `org.springframework.boot.persistence.autoconfigure.*` (Boot 4.x package paths differ from Boot 3.x).
- Serialization/deserialization always via `Environment.deserializeObjectFromJson()` / `Environment.serializeObjectToJson()` — never instantiate `ObjectMapper` directly in business code.
- Each file must carry the IBM Apache 2.0 copyright header (see any existing source file).

## Testing

- Tests are Spring Boot integration tests (`@SpringBootTest(webEnvironment = DEFINED_PORT)`) that start the full app on port `8888` and make real HTTP calls using Apache `fluent-hc`.
- **No mock layer** — tests require the full Spring context + H2 in-memory DB.
- Test order is enforced with `@FixMethodOrder(MethodSorters.NAME_ASCENDING)` — prefix test methods with a numeric string (`"0"`, `"1"`, ...) when order matters.
- ILMT output files are written to `./target/ILMT_files_test` during tests (configured in `service/src/test/resources/application.properties`).
- `RequestFactory` (test util) defines `USAGE_TYPE_DECISIONS` and `USAGE_TYPE_ARTIFACTS` constants — use these, not raw strings.

## CI

The GitHub Actions workflow (`.github/workflows/build-and-test.yml`) requires secrets: `GHE_TOKEN`, `ARTIFACTORY_USER`, `ARTIFACTORY_PASSWORD`, `ODM_URL`, `ICR_IO_URL`, `ICR_IO_PASSWORD`, `SLACK_WEBHOOK_URL`. Integration tests run via `bash test.sh` inside `docker/testing/` after a 100s ODM startup wait.
