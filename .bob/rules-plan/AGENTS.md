# Project Architecture Rules (Non-Obvious Only)

## Module split
- `model` = pure Java POJOs + Jackson annotations, **no Spring**. `service` depends on `model` and adds all Spring/JPA/reporting logic. New domain types (request/response shapes) go in `model`; service beans and controllers go in `service`.

## Property bootstrapping (non-standard)
`MeteringServiceApplication.retrieveProperties()` runs BEFORE Spring context starts. It reads `metering_service_default.properties`, applies `target_` remapping and `prefix_` prepending, then injects as Spring default properties. Command-line args (`--key=value`) can override non-mandatory props but are blocked from overriding mandatory ones. This means some properties visible in `application.properties` are actually injected via this mechanism, not directly by Spring.

## Scheduling & test timing coupling
`ReportHandler.doReporting()` runs on a `@Scheduled` fixed rate. Tests set `processingRate=10000` and `processingInitialDelay=10000`, then `Thread.sleep(15000)` to catch exactly one cycle. Any change to processing logic that adds latency will silently break tests without obvious failures — check sleep margins when refactoring `doReporting()`.

## Remainder accounting
DECISIONS metric uses `usingRemainders=true` (SUM aggregation, FLOOR rounding). Sub-threshold values accumulate in the `METRIC` table (`RemainderStatus.TO_BE_PROCESSED`). ARTIFACTS metric uses `usingRemainders=false` (MAX aggregation). Do not conflate the two patterns when adding new metric types.

## Dual-profile JAR outputs
The build produces different JAR names per profile (`metering-service-ODM.jar` vs `metering-service-ADS.jar`) via `spring-boot-maven-plugin` `<finalName>`. The `metering-service-no-dependencies.jar` finalName in `<build>` is the pre-repackage artifact — the runnable fat JAR gets the profile-specific name from the plugin config.

## Database schema
H2 schema is in `service/src/main/resources/schema.sql` and created with `spring.jpa.hibernate.ddl-auto=update`. All IDs are `VARCHAR` (UUID strings). The `REGISTRATION.DEFINITION` and `USAGE.DEFINITION` columns store full JSON-serialized request payloads — deserialization happens lazily in `toRegistrationRequest()` / `toUsageRequest()` on the entity.

## Dependabot gap
`dependabot.yml` only covers GitHub Actions (`github-actions` ecosystem), **not** Maven dependencies. Maven dependency upgrades must be done manually.
