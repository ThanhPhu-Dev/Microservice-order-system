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
1. 
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