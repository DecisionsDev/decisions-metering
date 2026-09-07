# Project Coding Rules (Non-Obvious Only)

- **Never** instantiate `ObjectMapper` directly — always use `Environment.deserializeObjectFromJson()` / `Environment.serializeObjectToJson()` / `Environment.deserializeObjectFromJsonOrDefault()`.
- **Never** call `LoggerFactory.getLogger(SomeClass.class)` — always use `Environment.getLogger()` which returns a shared named logger.
- **Never** hardcode log/error strings — all user-facing and log messages must reference a key constant from `service/src/main/java/.../util/Messages.java` and be retrieved via `Environment.getMessage(Messages.KEY, args...)`.
- JPA: use `jakarta.persistence.*` (Jakarta EE 10 / Spring Boot 4). Using `javax.persistence.*` will cause compile errors.
- Spring Boot 4.x changed package roots: use `org.springframework.boot.webmvc.autoconfigure.*` and `org.springframework.boot.persistence.autoconfigure.*`.
- `@ComponentScan`, `@EnableJpaRepositories`, and `@EntityScan` in `MeteringServiceApplication` list all packages explicitly — when adding a new package, add it to all three annotations or beans will not be discovered.
- `metering_service_default.properties` uses `target_<key>` and `prefix_<key>` conventions to remap property names to Spring properties. Adding a new configurable property requires a `target_` entry; a `prefix_` entry is needed when a value needs a prepended string (e.g., JDBC URL prefix).
- Entity IDs are always `UUID.randomUUID().toString()` assigned in the no-arg constructor — never use numeric auto-increment IDs.
- `Environment.METRIC_CONFIGURATION` is a static global set at `@PostConstruct`. In tests it is populated via `MeteringServiceApplication.init()`, not re-set between test classes — tests that depend on specific metric config must not alter it.
- The `license_metric_logger` JAR is system-scope. Any Maven command requires `-Dodm.home=<path>` (ODM profile) or `-Dilmt.jar.file.path=<path>` (ADS profile) even if tests are skipped.
- `model/pom.xml` declares `jackson-annotations` at `2.22` (without patch) and `jackson-databind` at `2.22.1` — keep these in sync with `jackson.version` in `service/pom.xml`.
- When upgrading `apache httpcomponents fluent-hc` (test dep, currently `4.5.2`), migrate to HttpClient5 API (`org.apache.hc.client5`) — the APIs are incompatible, `TestUtility` and both test classes need updating.
