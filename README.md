# Microservice-order-system
The system create an order and payment it with microservice system
## Day 1: System Design & Repo setup.
### Tasks: 
1. Draw architecture diagram
2. Create mono-repo
3. Write Readme.  
**Value: I design the system before I write the code.**

#### Answer and Note:
![High-Level Diagram](./Architecture-diagram/High-level diagram.drawio.svg)
1. io.spring.dependency-management: a plugin for using dependencyManagement.  
2. `dependencyManagement` is a rule , It won't download any packages to classpath.
3. Dependencies block cannot be used in parent module, because parent module doesn't any code.
4. `mavenBom` is version management , if sub-module declares a lib without specifying a particular version, the version will be obtained from mavenboom..
5.  `id 'org.springframework.boot' version '3.2.1' apply false` is a plugin for tasks, e.g: bootRun, bootJar, `apply false` are downloads but not applied for this module.
6. `id 'java-library'` is plugin for exporting libs.
7. `dependencies { api ... }` exports a lib (public lib).
8. `implementation project(':common')` requires this block to import lib expoprted from common.
9. `dependencies { implement ... }` is a private lib.

## Day 2: Order Service (CORE)
### Tasks:
1. Spring boot
2. Order Entity
3. Create order API
4. Order status lifecycle
5. Database migration (Flyway/Liquibase)
### Must-have:
1. Transaction
2. Validation
3. Exception handling.  
**Value: Transaction boundary in microservice**

#### Answer and Note:
1. Properties in an entity should be Objects, not primitive types.
2. In springboot: 1 request is handled by one thread by default, therefore there is no need to return completefuture for every API, Asynchronous processing should be used for business logic.
3. layers should communicate via interfaces
4. `ResponseEntity.accepted()` returns 202, this status code is used to indicate that the request has been accepted and is being processed, but has not been completed yet.
5. Import `org.springframework.boot:spring-boot-starter-validation` to use validation.  
   5.1. Validation rules can be defined on fields using annotations such as `@NotEmpty` `@Min`, etc. You can also define a custom error message for each annotation.  
   5.2. In the controller layer, use `@valid` on the request parameter that needs validation, if request is invalid Spring will automatically return a validation error message and the controller method will not be executed.  
   5.3. `@Validated` is placed on a class (such as `@Service` or `@Component`), it enables validation for individual method parameters. For example: `public void updateAge(@Min(18) int age)`.  
   5.4. annotations have a `groups` property to categorize validation rules. For example, an id might be required for an update API but not for creation API.
```
//create a new interface
public interface UpdateGroup{}
public interface CreateGroup{}

// in DTO
public class UserDTO {
    @NotNull(groups = UpdateGroup.class)
    private Long id;
   ...
}
// In controller
 @PostMapping("/users")
    public void create(@Validated(CreateGroup.class) @RequestBody UserDTO user) {
        ...
    }

    @PutMapping("/users")
    public void update(@Validated({Default.class, UpdateGroup.class}) @RequestBody UserDTO user) {
        ...
    }
```
   * Annotations without an explicit groups property belong to the `jakarta.validation.groups.Default` group. If you only declare `CreateGroup.class`, other groups (such as `Default.class` and `UpdateGroup.class`) will be ignored.  
   5.5. If you want the API method to be called regardless of validation success or failure, you must include a  `BindingResult bindingResult` parameter in the controller method.  
6. You can also handle exceptions using `@RestControllerAdvice`.  
   6.1. Create a class with `@RestControllerAdvice`.  
   6.2. for e.g : you can catch `BindException` to return a custom response.  
   ``` @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException e) {
        String errorMessage = "Request is invalid";
        log.error("Request is invalid");
        if (e.getBindingResult().hasErrors())
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return errorMessage;
    }`
   ```
   6.3 You can also define and handle custom exception (e.g: `AppRunTimeException.class`) to catch and process.
7. `ApplicationEventPublisher` is a Spring-provided object, used to publish events. To listen for an event, use an event listener annotation.  
```
      @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
       public void onOrderEvent(OrderCreatedEvent orderCreatedEvent) 
```
8. To use Lombok, you must add the following dependencies
```
      compileOnly "org.projectlombok:lombok:1.18.42"
      annotationProcessor "org.projectlombok:lombok:1.18.42"
```
9. To Use JPA, import `org.springframework.boot:spring-boot-starter-data-jpa`.  
   9.1. Use `@transactiona;` on methods to enable transactional behavior.  
   9.2. Spring provides 7 transaction propagation modes:  
   &emsp; 9.2.1. `REQUIRE`(default), if there is an active transaction, the method joins it. If not, the new transaction is created.  
   &emsp; 9.2.2. `SUPPORTS` : Use an existing transaction if present; otherwise, executes without a transaction context.  
   &emsp; 9.2.3. `MANDATORY`: require an existing transaction, otherwise an exception is thrown.  
   &emsp; 9.2.4. `NEVER`: Throws an exception if a transaction already exists.  
   &emsp; 9.2.5. `NOT_SUPPORTED`: Suspends the current transaction and executes the method without a transaction context.  
   &emsp; 9.2.6. `REQUIRES_NEW`: Suspends the current transaction, creates a new one, commits it, and then resumes the previous transaction.  
   &emsp; 9.2.7. `NESTED` Creates a new transaction if none exists. If a transaction exists, Spring creates a savepoint and rolls back to it in case of an error.  
   9.3 transactional provides two important attributes rollbackFor and noRollbackFor. For example: roll back for all subclasses of `Exception` except `EntityNotFoundException`.  
```
   @transactional
      (rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
   public void update (Long id, String name){
      Author author = authorRepository.findbyId(id).orElse(null);
      author.setName(name);
   }
```   
Default the transaction rollback only for `RuntimeException`, If you want to roll back for all exceptions, use `Throwable.class` in rollbackFor.  
&emsp; 9.4 Use `readOnly = true` for select queries to improve performance.  
10. Flyway: To use Flyway, add the following dependencies `org.flywaydb:flyway-core:11.20.0` and `org.flywaydb:flyway-mysql:11.20.0`, Flyway configurations are defined in application.yml, and migration scripts are placed under the resources directory. Once a migration version is applied, 
Flyway does not allow modifying or rolling back the content of an existing migration file.

## Day 3: Kafka Integration
### Tasks:
1. Kafka producer in order service.
2. Emit `OrderCreated` event.
3. Consumer base config.
4. Event schema.
### Must have:
1. Event Id.
2. idempotency key.  
**Value: At-least-once, duplicate handling**
## Day 4: Inventory service
### Tasks:
1. Consume `OrderCreated`.
2. Check & reserve stock
3. Emit `InventoryReserved`
4. Redis lock when subtract stock.  
**Value: Distributed lock & race condition**
## Day 5: Payment Service
### Tasks:
1. Consume `InventoryReserved`.
2. Simulate payment
3. Emit `PaymentCompleted`/`PaymentFailed`.  
**Value: Saga choreography**
## Day 6: Order Finalization
### Tasks:
1. Order service consume payment result
2. Update final status.
3. Failure handling.  
**Value: Eventually consistency**
## Day 7: Redis Cache
### Tasks:
1. Cache Order detail.
2. Cache-aside pattern
3. TTL
4. Invalidate cache on update.  
**Value: Cache inconsistency**
## Day 8: Resilience
### Tasks:
1. Retry Kafka consumer.
2. Dead letter topic.
3. Timeout.
4. Circuit breaker (basic).  
**Value: When downstream service die**
## Day 9: Observability
### Tasks:
1. A TraceId for all services.
2. Logging.
3. Actuator health.  
**Value: Debug production issue**
## Day 10: Docker
### Tasks:
1. Dockerfile each services
2. Docker Compose:  
2.1 Kafka  
2.2 Redis  
2.3 DB  
2.4 Services.  
**Value: Local dev same as Prod**
## Day 11: Kubernetes (INTERVIEW LEVEL)
### tasks:
1. Deployment yaml.
2. Service yaml.
3. ConfigMap.
4. Resource limit.
5. Health probe.  
**Value: Developers understand that apps live in K8s.**
## Day 12: Clean up & README
### tasks:
1. Complete Readme:  
1.1 Architecture.  
1.2 Event flow.  
1.3 Failure cases.  
2. Diagram export.
## Day 13: Mock Interview
### Questions:
1. Order Service:  
   1.1 Why create orders synchronously but process them asynchronously?  
   1.2 Where is the boundary?
2. Inventory Service:  
   2.1 How can we avoid overselling?
   2.2 What happens if the Redis lock fails?
3. Payment Service:  
   3.1 How do I handle a duplicate payment?  
   3.2 What happen if payment succeeds but Kafka fails?
4. Kafka:  
   4.1 When do I need to duplicate a message?  
   4.2 When do we make an offset commit?
5. K8s:  
   5.1 What is an OOMKilled Pod?  
   5.2 Readiness vs liveness?
## Day 14: Review & Polish
1. Simplify code
2. Remove overkill

## CheckList PASS Interview
### Java / Spring
- [ ] @Transactional rollback
- [ ] Bean lifecycle
- [ ] Exception handling
- [ ] Thread safety
### Kafka
- [ ] Consumer group
- [ ] At-least-once
- [ ] Idempotency
- [ ] DLQ
### Redis
- [ ] Cache-aside
- [ ] TTL
- [ ] Lock
- [ ] Inconsistency
### Microservice
- [ ] Saga
- [ ] Event-driven
- [ ] No shared DB
- [ ] Service independence
### Docker / K8s
- [ ] Resource limit
- [ ] Health check
- [ ] ConfigMap
- [ ] Rolling update