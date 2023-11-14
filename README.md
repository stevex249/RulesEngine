# Visio Rules Engine
Rules engine for calculating loan product pricing from a set of rules.

## Requirements
For running the application you need:
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 3.5+](https://maven.apache.org/)

## Running the application
- Clone the [project](https://github.com/stevex249/visio.rulesengine)
- Open up the project locally by navigating to `\RulesEngine`
- Run `mvn compile` to install dependencies and compile the project
- Run `mvn spring-boot:run` to start up the project
    - Swagger is installed and can be accessed by navigating to [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/) (port 8080 is default)
- Tests are included in the `src\test` directory and can be run using `mvn test`