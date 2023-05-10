# Java Spring Boot Microservice Accelerator

## Description
This accelerator will help you bootstrap a Spring Boot microservice with REST API and various capabilities.

## Features
- [RESTful API with OpenAPI spec](src/main/java/com/example/patient/api/PatientController.java)
- [Simplified Hexagonal Architecture](src/main/java/com/example/patient)
- [Unit tests](src/test/java/com/example/patient)
- [Behavior-driven development (BDD) tests](src/test/resources/bdd/patient.feature)
- [Project Lombok](src/main/java/com/example/patient/domain/Patient.java)
---StartJpa
- [JPA for SQL database access](src/main/java/com/example/patient/data)
- [Database migrations with Flyway](src/main/resources/db/migration)
---EndJpa
---StartDocker
- [Testcontainers for integration tests](src/test/java/com/example/ApplicationTest.java)
- [Docker Compose for running local services](docker-compose.yaml)
---EndDocker
---StartRedis
- [Caching with Redis](src/main/java/com/example/patient/data/RedisCacheConfig.java)
---EndRedis

## Coding Standards Conventions 
We use (https://google.github.io/styleguide/javaguide.html) To enforce these standards we use a very generic [.editorconfig](./.editorconfig) file.

### What is .editorconfig?

EditorConfig helps developers define and maintain consistent coding styles between different editors and IDEs. The EditorConfig project consists of a file format for defining coding styles and a collection of text editor plugins that enable editors to read the file format and adhere to defined styles. EditorConfig files are easily readable and they work nicely with version control systems. Find out more at [editorconfig.org](http://editorconfig.org/).

EditorConfig file supports the following file types:
- java - .java
- Script - .sh, .ps1, .psm1, .bat, .cmd
- XML - .xml, .config, .props, .targets, .nuspec, .resx, .ruleset
- JSON - .json, .json5
- YAML - .yml,  .yaml
- HTML - .htm, .html
- JavaScript - .js, .jsm, .ts, .tsx, .vue
- CSS - .css, .sass, .scss, .less, .pcss
- SVG - .svg
- Markdown - .md
#### Setting up .editorconfig on local
- In the Project tool window, right-click a source directory containing the files whose code style you want to define and select `New | EditorConfig` from the context menu.
- Select the properties that you want to define so that IntelliJ IDEA creates stubs for them, or leave all checkboxes empty to add the required properties manually.

SonarLint also provides a comprehensive dashboard that displays all the issues in your codebase and allows you to filter and prioritize them based on severity and other criteria. This makes it easy to track your progress and ensure that you are addressing the most critical issues first. Find out more at [SonarLint Plugin](https://plugins.jetbrains.com/plugin/7973-sonarlint).
#### Setting up SonarLint on local
- Download SonarLint plugin in Intellij:
In Intellij — Go to Settings >> Plugins >> Type ‘SonarLint’ >> Install and Restart IDE.
  - We can use the SonarLint either with
  
    (1)   predefined rules set and select/remove from that list, or
  
    (2) we can choose the SonarQube rules.
- Once you have successfully installed and restarted the IDE you will be able to see SonarLint tab in the the toolbar at the bottom of IDE

## Getting started

### Prerequisites
Before setting up this project, you’ll need:

---StartGradle
- Gradle (a Gradle wrapper `gradlew` is already included for your convenience)
---EndGradle
---StartMaven
- Maven (a Maven wrapper `mvnw` is already included for your convenience)
---EndMaven
- Java Development Kit (JDK) 8 or newer
---StartDocker
- Docker Desktop or Rancher Desktop (for running dependencies locally)
---EndDocker


## Running tests
---StartDocker
- Make sure your Docker/Rancher Desktop is up and running, as the tests need to run docker container(s)
with dependencies (e.g. database) via Testcontainers
---EndDocker
To run automated tests:
```bash
---StartGradle
./gradlew test
---EndGradle
---StartMaven
./mvnw verify
---EndMaven
```
- That runs the unit tests, integration tests and BDD tests
- BDD tests are self-contained and will run the app and its dependencies automatically

---StartDocker
---StartGradle
### JUnit version support

This application includes support for JUnit 5 as a dependency of `spring-boot-starter-test`. Due to
a [known issue with Testcontainers](https://www.testcontainers.org/test_framework_integration/junit_5/#limitations),
it also has a transitive dependency on JUnit 4. [This issue should
be addressed when Testcontainers version 2 is released](https://github.com/testcontainers/testcontainers-java/issues/970#issuecomment-1384580706);
in the meantime, **please avoid importing
JUnit 4 classes and assertions into your codebase.** This means avoiding annotations
like `org.junit.Test` or assertion methods like `org.junit.Assert.assertEquals`. Instead, please use
the equivalent `org.junit.jupiter.api.Test`. Example tests in this repository all use JUnit 5
annotations, as well as assertions from [AssertJ](https://assertj.github.io/doc/).

---EndGradle
---EndDocker
### Test coverage reports
We use Jacoco for reporting test coverage. We believe that focusing on good tests and ideally following Test Driven
Development (TDD) yields better outcomes that focusing on coverage only. The coverage reports can be generated for your
convenience, as well as for DSOMM purposes.

The JaCoCo report is generated as part of running tests:
---StartGradle
```bash
./gradlew test
```
The report is located in [build directory](build/reports/jacoco/test/html/index.html)
---EndGradle

---StartMaven
```bash
./mvnw verify
````
The report is located in [target directory](target/site/jacoco/index.html)
---EndMaven


## Run locally
- Open command line or terminal and navigate to the project's root
---StartDocker
- To start dependencies, run:
```bash
docker compose up
```
---EndDocker
- To start the Spring Boot app, run:
```bash
---StartGradle
./gradlew bootRun
---EndGradle
---StartMaven
./mvnw spring-boot:run
---EndMaven
```
### What is sonarlint?
SonarLint is a code analysis tool that integrates with various IDEs and text editors to provide real-time feedback on code quality issues. SonarLint supports a wide range of programming languages and integrates with various IDEs, including IntelliJ IDEA, Eclipse, Visual Studio, and Visual Studio Code. It can be installed as a plugin or extension for the IDE or text editor of your choice.

Once installed, SonarLint will automatically analyze your code as you type and highlight any issues it finds. It will also provide detailed information about the issue, including a description, severity level, and recommendations for how to fix it.

SonarLint also provides a comprehensive dashboard that displays all the issues in your codebase and allows you to filter and prioritize them based on severity and other criteria. This makes it easy to track your progress and ensure that you are addressing the most critical issues first. Find out more at [SonarLint Plugin](https://plugins.jetbrains.com/plugin/7973-sonarlint).
#### Setting up SonarLint on local
- Download SonarLint plugin in Intellij:
  In Intellij — Go to Settings >> Plugins >> Type ‘SonarLint’ >> Install and Restart IDE.
    - We can use the SonarLint either with

      (1)   predefined rules set and select/remove from that list, or

      (2) we can choose the SonarQube rules.
- Once you have successfully installed and restarted the IDE you will be able to see SonarLint tab in the toolbar at the bottom of IDE

##### Setting up SonarLint with CVS SonarQube server

There are two parts to setting connecting your repository to the CVS SonarQube server, so that you can use SonarLint with the CVS SonarQube rules: creating a project on the SonarQube server, and configuring the SonarLint plugin.

If you haven't already created a project within SonarQube, add your project to SonarQube before configuring your editor as follows:
1. Navigate to https://sonarqube.cvshealth.com
2. Click on "Add Project" on the right side of the page, and then click on "Manually"
3. Identify a project key and display name for your project within SonarQube
4. Either generate a new token for the project, or use an [existing token](https://sonarqube.cvshealth.com/account/security)

Once you've installed the SonarLint plugin for your editor, you can bind your local application to SonarQube following the SonarLint instructions for [IntelliJ](https://github.com/SonarSource/sonarlint-intellij/wiki/Bind-to-SonarQube-or-SonarCloud) or [Visual Studio Code](https://github.com/SonarSource/sonarlint-vscode/wiki/Connected-Mode). There will be some steps specifically that you'll need to take in order to configure this to work with the CVS Health SonarQube instance:
1. Make sure to choose the SonarQube connection type, not SonarCloud
2. When creating a new connection ([IntelliJ](https://github.com/SonarSource/sonarlint-intellij/wiki/Bind-to-SonarQube-or-SonarCloud#configure-a-connection) or [VSCode](https://github.com/SonarSource/sonarlint-vscode/wiki/Connected-Mode#connection-setup)), please use https://sonarqube.cvshealth.com as the SonarQube Server URL
3. Use `Token` as your authentication type, using the Create Token button to generate a personal token

### Execute the Sonar Scanner locally from Command Line
To execute the Sonar scanner locally, run the following command from the command line within the project directory. You must use a project key associated with your project and a valid user token.

```shell 
---StartGradle
./gradlew sonarqube \
---EndGradle
---StartMaven
./mvnw sonar:sonar \
---EndMaven
    -Dsonar.projectKey=$SONAR_PROJECT_KEY \
    -Dsonar.host.url=https://sonarqube.cvshealth.com \
    -Dsonar.login=$SONAR_USER_TOKEN
```
### What is Open API?
OpenAPI is a RESTful API specification describing APIs that conform to RESTful architecture. A specification provides an interface for letting humans and computers understand an API and how to interact with it.
The OpenAPI Specification (OAS) is a framework used by developers to build applications that interact with REST APIs. The specification defines how to communicate with an API, what information can be requested, and what information can be returned. With OpenAPI, developers can ensure their APIs are consistent with the industry standards and can easily integrate their applications with other services. Find out more at [https://springdoc.org/](https://springdoc.org/).


#### Getting springdoc-openapi

When we run our application, then OpenAPI spec available under: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)


## Usage
This accelerator is meant to be a starting point or example on how to create a Spring Boot microservice.

## Architecture Decision Record (ADR)
To learn about architectural decisions for this accelerator, check our [ADR](ADR.md)

## Contact us
Email [DevEcosystemSupport@cvshealth.com](mailto:DevEcosystemSupport@cvshealth.com) if you have questions, need help or would like to report a security issue or bug. You should receive a response within 24 business hours.


---StartAcceleratorDeveloper
## Accelerator Developer Section
This section contains information specific for the development of this accelerator

### Test-driving accelerator

This accelerator contains more complex transforms due to options allowing to disable capabilities. Also, "programming"
an accelerator using yaml and powerful recipes may not be very intuitive. Therefore, it's more important than ever
to test-drive the implementation of the accelerator, to unsure things work as expected. It's also not that difficult to 
accidentally break already implemented functionality, so having a complete set of tests will prevent from regression
and allow us to [go fast forever](https://tanzu.vmware.com/content/blog/why-tdd). Also, since we're promoting TDD
through accelerator, we should also eat our own food. 

When adding/modifying the accelerator, to have high confidence that new features work and old ones are intact, one needs
to run tests with every incremental change. Otherwise, it proves to be difficult to reason about the changes and errors
caused. On top of that, based on user inputs (accelerator options) we need to ensure the critical permutations of
generated output are tested. Currently, to run the accelerator's transforms ("generate" the output code), we need to
perform that on TAP. To help with that, we have developed the [bin/acc-test script](bin/acc-test), and the process is
as follows:
- add a test for a small incremental of the functionality you're adding/modifying - e.g. assert if a file exists, or if
it contains specific content
- run the [bin/acc-test script](bin/acc-test) - it should fail, as we haven't implemented the functionality yet (e.g.
add accelerator transform)
- pay attention to the test failure, test needs to fail for the right reason, and error message should clearly indicate
that; if that's not the case, improve the test
- only when the test is failing for the right reason, modify accelerator (yaml), but only as much as needed to pass
the tests, nothing more
- when you believe you change is ready, you would need to "deploy" (git push) the accelerator to TAP (dev instance)
before running the tests (the tests run against deployed accelerator)
- if the feature you're working on is more complex, and you're expecting to go through multiple cycles (increments),
it would be recommended to create a test accelerator on TAD dev, that points to your feature branch/repo
- after deploying the accelerator, you way need to wait up-to 1 min (or whatever `interval` you have set in the k8s
resource for you accelerator), for it to be refreshed on TAP
- after that, you can run the [bin/acc-test script](bin/acc-test); if your implementation is correct, the tests should
pass; if not iterate on your implementation and/or correct the test

#### bin/acc-test pre-reqs
- kube config pointed at TAP dev and user logged in
- user to have permissions to generate accelerators on TAP dev
- tanzu cli with accelerator plugin installed and available on the path
- jdk 17+ installed
- Docker/Rancher Desktop installed and running

#### bin/acc-test internals
The [bin/acc-test script](bin/acc-test) uses Tanzu CLI to "generate" accelerator. That requires accelerator to be
already available on TAP dev, and your machine to be set up with the CLI and kube configuration for our TAP dev cluster.
The script uses multiple critical permutation of user inputs (accelerator options), and generates multiple outputs.
For each output, the full build is run, including running all the tests that are part of the accelerator. On top of that
we have our own accelerator-creation tests that live in [src/acc-test](src/acc-test). The script copies relevant tests
to the generated output, and they get run with the rest of the tests.

---EndAcceleratorDeveloper
