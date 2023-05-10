
## Why RESTful API
This one looks straightforward, right? It's often not, as RESTful can mean different things to different people, and
many are not aware of intricacies and limitations of the underlying HTTP standard. On top of that, REST is not a natural
fit for most of the domains. Let's address those one by one.

### Richardson Maturity Model
The model has four levels (0-3), which is the best indication that different systems/teams implement "REST APIs"
differently. [This blog post](https://martinfowler.com/articles/richardsonMaturityModel.html) by Martin Fowler explains
the model quite well.

The API in this accelerator fits into Level 2 (HTTP Verbs), second from the top, which in our opinion is the sweet spot
of simplicity and doing it right. We intentionally avoided "Hypermedia Controls" with HATEOAS (Hypertext As The Engine
Of Application State) as this may complicate things quite a bit. However, if you believe your system would benefit from
that, there is [Spring HATEOAS](https://spring.io/projects/spring-hateoas) that would help you implement it on the API
producer-side. Be warned though, the tooling is lacking for the consumer-side, and your consumers/teams may not be that
versed in HATEOAS. Most likely YAGNI, so don't do it for the cool-factor of getting your API to Level 3. It'll be an
overkill.  

### HTTP standard
REST was created to address issues with misusing HTTP. For example, HTTP verbs have very special meanings and specs,
that every piece of standard HTTP software and infrastructure adheres to. Misuse of HTTP verbs in API can lead to
serious errors and consequences.

Let's have a closer look at the classic GET. According to the spec, this verb is both safe and cacheable. Safe means
that it must not alter the state of the server, which in turn means that any HTTP software or infrastructure element
in the chain (http client, proxy) could safely re-try this request on failure. On the other hand, cacheable means that
any element in the HTTP chain could cache the previous response, and return the cached response for the same requests
in the future. It's not uncommon to see GET being misused for example to trigger some job on the server. Now knowing
about those two properties of the GET, safe and cacheable, we can tell that we don't have any guaranties on how many
times that job is going to run, or if at all. If there was an intermittent network failure, we could see retried
requests that would be reaching the server. The other extreme is that the job doesn't get triggered at all, as
we get a cache hit and the request doesn't even reach the server. The more appropriate HTTP verb for this use case
would be POST, which is neither safe nor cacheable, and therefore giving us better guaranties that the job is going to
run once.

This is just one example and there is way more things that could go wrong. HTTP response status codes have special
meaning as well. For example, some errors are re-triable and some not. There is also a plethora of standard HTTP headers
that control caching, content negotiation, tracing, access, authentication and other aspects of security. We also can
have different permutations, for example some status codes and verbs can indicate whether response is allowed to have
body or not.

Recommendation: Find your favorite and trusted sources that provide relevant HTTP details in a digestible format.
Finding those details in the HTTP spec might be too much to ask. Here is a few resources:
- [Mozilla HTTP Guide](https://developer.mozilla.org/en-US/docs/Web/HTTP) - quite comprehensive
- [REST API Tutorial](https://www.restapitutorial.com/) - good for a quick status code search, or a refresher on verbs
- Funny status codes:
  - [if you're a cat person](https://httpcats.com/)
  - [if you're a dog person](https://httpstatusdogs.com/)
  - [if you're a cat person - classic](https://http.cat/)

### Is RESTful the right choice for my domain
Most likely not. What?! Yep, the elephant in the room. REST is all about nouns (resources) and is really limited to CRUD
operations. The real world domains use a lot of verbs (actions/commands/events). However, we kind of got used trying
hard to model any domain with REST and nouns (also heavily influenced by OOP), and everybody has their favorite
techniques to work around that mismatch. That's because REST APIs (and OOP) are everywhere, and that's what we know
best. Though, the systems want to behave differently, and we're seeing a shift in the industry and increasing
adoption of patterns that support that. For example, [CQRS](https://martinfowler.com/bliki/CQRS.html) pattern splits
Commands from Queries, where verbs model the former better. Events in event-based systems try to model actions. We are
seeing many APIs that use RPC over HTTP model, and they violate REST principles, but they can better express their
domains. Those who stick with REST often need to make exceptions.

If you feel like your domain would be hard to model with REST, and your system is telling you that it wants to behave
differently, for example you see a lot of actions and verbs, don't be afraid to "break" REST. However, do not violate
the HTTP standards.

### Other important decisions
A few more decisions that we made for the API of this accelerator:
- paths have singular nouns (e.g., `/patient`, not `/patients`)
- multi-word nouns should use kebab casing (hyphens) or snake casing (underscores); avoid camel/pascal casing as URLs
are technically case-insensitive
- put all endpoints under a single `/api` path to make it easier to manage
- we haven't included versions - we'll leave it up to you, as there are multiple options, and it may depend on your use
case, whether you're serving multiple API versions from the same deployable, or maybe managing versions in API gateway
- API documented with OpenAPI/Swagger and spec available at `/api-docs`


## BDD test setup
The BDD tests often fall into the upper part of the [test pyramid](https://martinfowler.com/articles/practical-test-pyramid.html#TheTestPyramid).
With the Gherkin abstraction and overhead they better fit into the end-to-end (e2e) category of tests, and sometimes can
be implemented and run by QA. However, for simplicity, the BDD tests in this accelerator were embedded with other tests,
and the tests are responsible for starting the database and the application. Some additional work will be required if you
need to run tests against a fully deployed app on any environment.

Also note that BDD test results are published to https://reports.cucumber.io/ for convenience. You may want to consider
disabling that in [BddTest](src/test/java/com/example/bdd/PatientBddTest.java) for your project if you don't want them to be sent
to the third party.


## Why Test Driven Development (TDD)
We all agree that having good tests is critical to ensure good quality in software. Unfortunately chasing test coverage
metrics may not lead to good tests. On the contrary, it can often lead to really poor tests, if coverage is the outcome.
Practicing test-driven development, when done well (you really write that test before the implementation) produces
great tests. We care about the quality, and use tests to really drive the implementation, and this accelerator gives you
a good starting point on that path. You can find all kind of tests here, from unit, to integration, even BDD.

But the quality is not the only outcome. We truly believe that thanks to TDD we can really go fast forever. If you are
curious, you can read more about that in [this blog post](https://tanzu.vmware.com/content/blog/why-tdd) and
join the club and go fast forever on your projects too.

### Detroit vs London school of TDD
This is a more nuanced topic on testing, and you can find endless debates online on which approach is better. We believe
that you can get the best of both worlds by mixing and matching, and apply what makes the most sense to you in
the specific scenario. Therefore, you will find here some tests that rely on mocks (more of a London school), as well as
some more black-boxy tests (e.g. `SpringBootTest`).

### Detroit and SpringBootTests
In case you end up leaning more towards the Detroit end of the spectrum in your project, and use a lot of
`@SpringBootTest` (or others that use Application Context: `@WebMvcTest`, `@DataJpaTest`), we want to make you aware
of potential problems. Those tests are more "expensive" to run, as they create Application Context, or actually many
contexts, when we use `@MockBean` or change configuration properties between tests. To help with that, Spring Test does
Context Caching out of the box, but that causes more interesting side effects, like keeping all the unique contexts in
the cache and therefore running out of resources (e.g. exceeding max connections on database). So it can be a fine
balance to try to optimize your tests for speed and resources. As a rule of thumb, keep up to ten most commonly used
contexts in the cache, and evict less common ones with the `@DirtiesContext` annotation. Intrigued by this? You can
learn more about how that Context Caching works and how to optimize it in [this blog post](https://rieckpil.de/improve-build-times-with-context-caching-from-spring-test/).

### Triple A pattern in tests
When you look closer at our tests, you many notice that they have similar structure. We follow the AAA
(Arrange, Act, Assert) pattern where applicable. This is also similar to Given, When, Than in the Gherkin syntax.
That means that each test has three distinct sections, setting up the test data, calling the test subject, and finally
verifying the results. Following such a structure makes those tests more readable, but also prevents from trying to test
too much in one test. If the test doesn't fit into that structure, it's a good indication that we may need to split it
into more tests.

Note: the Arrange section may not be always present, or may live in some other place, such as a common setup for tests.

### Controlling the time
Most of the time, we write code the returns same results for the same set of inputs. Test-driving such code is
relatively straightforward. Every now and again, the implementation includes some more dynamic elements, for example
a random number generator, or a clock. Instead of trying to be clever with the assertions and overcomplicate them,
we should leverage the Inversion of Control, and start controlling those dynamic elements. That way we still write good
tests, but we also improve the design of our code.

For example, whenever we have date or timestamp in the code under test, we replace the real clock with "fixed" clock
in tests, so we can control the time and simplify the tests. We also make sure the production code uses the real code.

### Test libraries
You can tell by now that we like good tests. Having right tools and libraries helps us to write better tests. Here are
some of our picks that you see in the code.

#### AssertJ
One of the most important part of every test is an assertion. So we should use the right tool for this job.
The standard `assert*` methods that come with JUnit don't really cut the mustard, and failure messages can be misleading
when the methods are used incorrectly. AssertJ gives much better experience. With the fluent API the assertions read
better, and it's quite hard to use them incorrectly. And most importantly, the failure messages are much better and
provide more details and extra context. There are some other decent assertion libraries, but since AssertJ comes with
Spring Test, we use it as much better alternative to standard assertions.

#### REST-assured
Do you like test libraries with fluent API built around Given/When/Then? You're gonna love [REST-assured](https://rest-assured.io/).
A good choice for testing REST services.

#### Awaitility
Testing asynchronous code can be hard. Adding many `Thread.sleep` makes it even worse, as now your tests run much slower,
and they can be quite flaky. [Awaitility](http://www.awaitility.org/) helps with testing asynchronous code, as it has
a good _DLS that allows you to express expectations of an asynchronous system in a concise and easy to read manner_.


## Why Project Lombok
Java has been infamous for its boilerplate when it comes to Java Beans. IDEs can often help with that, but it's still
a lot of unnecessary code to write/generate, read and maintain. Since Java 14 (really 17, as that's the next LTS version)
this has been improved with the [record classes](https://www.baeldung.com/java-record-keyword). However, not everybody
can switch to Java 17, and the industry adopted [Project Lombok](https://projectlombok.org/) that now comes bundled
with some popular IDEs (no need to install an extra plugin). This accelerator uses Lombok for various Java Beans,
including JPA entities. However, bear in mind that some frameworks/libraries, for example JPA, may require very specific
implementations. Here is a few points to remember when using Lombok with JPA entities, just as an example:
- Avoid using `@EqualsAndHashCode` and `@Data` with JPA entities
- Always exclude lazy attributes when using `@ToString`
- Don’t forget to add `@NoArgsConstructor` to entities with `@Builder` or `@AllArgsConstructor`

You can learn more on [Lombok and JPA: What Could Go Wrong?](https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong)
and watch out for other libraries that may have specific requirements.


---StartJpa
## Why database migrations
With agile's rapid delivery and incremental approach to building software, database schemas need to evolve with the code.
That requires capability to modify (migrate) the schema while deploying newer versions of the code. Database migration tools
make that job very easy, and for Java/JVM stack, there are two main options: [Liquibase](https://www.liquibase.org/)
and [Flyway](https://flywaydb.org/). This accelerator comes with a Flyway setup, as it is a more streamlined and easier-to-use tool. Spring Boot has been configured to run database migrations on the startup of the app.

Important notes:
- One of the main principles of the Microservice Architecture is that each service owns its data. This is
  the prerequisite for embedding database migrations within the app.
- To facilitate zero-downtime deployments, each deployment must have only backward compatible schema changes. When using
  blue-green or rolling deployments, for some period of time, both the old and the new code must work with the old and
  the new schema. Backward compatibility often requires splitting breaking migrations into multiple independent deployments,
  and using techniques such as [dual writing](https://stripe.com/blog/online-migrations).

## Why UUIDs as primary keys
There are a few popular options for the data type used for primary keys within a database: namely, numbers or UUIDs. There are pros and cons of each; numbers are more efficient and result in faster lookups, while UUIDs can be more secure and can help maintain relationships during a data migration. We ultimately decided to use UUIDs as an out-of-the-box approach, due to their security benefits. Depending on your needs, you might choose to change this to use numbers instead, or even choose a numeric primary key but use a UUID as a public identifier to try to get the best of all worlds. You can learn more in this article on [UUID or GUID as Primary Keys?](https://tomharrisonjr.com/uuid-or-guid-as-primary-keys-be-careful-7b2aa3dcb439), or [Primary Keys: IDs versus GUIDs](https://blog.codinghorror.com/primary-keys-ids-versus-guids/).

---EndJpa
---StartRedis
## Why use Redis for caching
Redis (which stands for REmote DIctionary Server) is an open source, in-memory, NoSQL key/value store that is primarily used as an application cache or quick-response database. It provides unrivaled speed, reliability, and performance since it saves data in memory rather than on a disk or solid-state drive (SSD).
When an app uses external data sources, the latency and throughput of such sources might be a performance barrier, especially as traffic grows and the app scales. In these instances, one method to increase performance is to store and manipulate data in memory, which is physically closer to the application.
This is why Redis was created: It keeps all data in memory for the fastest possible performance when reading or writing data, and it has built-in replication capabilities that allow you to physically place data closer to the user for the lowest possible latency.
Other Redis characteristics worth noting include support for multiple data structures, built-in Lua scripting, multiple levels of on-disk persistence, and high availability.

Redis has also different capabilities, such us session storage, queueing, pub/sub and others. However, in this accelerator we're using Redis as cache only.


---EndRedis

---StartDocker
## Testcontainers in Java
Please check [application-test.yaml](src/test/resources/application-test.yaml) to see how a proper database is wired with test-container.
Yes, we could have cheated here and used an in-memory database (e.g., H2) instead. However, using a completely different database
than our production code uses doesn't give us a lot of confidence, does it?

---EndDocker
