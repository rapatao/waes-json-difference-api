WAES Coding Test
===

The goal of this assignment is to show your coding skills and what you value in software engineering. We value new ideas so next to the original requirement feel free to improve/add/extend. We evaluate the assignment depending on your role (Developer/Tester) and your level of seniority.

# The assignment

- Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
    - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
        - The provided data needs to be diff-ed and the results shall be available on a third end
point
    - <host>/v1/diff/<ID>
        - The results shall provide the following info in JSON format
            - If equal return that
            - If not of equal size just return that
            - If of same size provide insight in where the diffs are, actual diffs are not needed. 
                - So mainly offsets + length in the data

- Make assumptions in the implementation explicit, choices are good but need to be
communicated

# Stack & Requirements

* Java 8
* Spring Boot 2
* Lombok
* Swagger 2
* Cassandra
* Docker
* Docker Compose

# Building

This project uses Maven for project management tool.
To build the project you need to have running Cassandra without user/password and on the default port (9042).

The project contains a `docker-compose` that runs the required Cassandra using the default configuration that is required for this project runs properly.

## Building and running the tests

* Start the required components and pre-configurated on the `docker-compose` file.
```bash
$ docker-compose up
```

* Compile and package the project

```bash
$ mvn clean package
```

## Building without running the tests

In this case, you do not need to have the Cassandra running, so you just have to execute the following command:

```bash
$ mvn clean package -DskipTests
```

# Running

To run the project you need to have the Cassandra running without credentials configuration and with the default port (9042) exposed.

```bash
$ java -jar target/json-difference-api-1.0-SNAPSHOT.jar
```

## Running using Docker Compose

The following command will start the application as well as the required Cassandra.

```bash
$ docker-compose -f docker-compose.yml -f docker-compose.app.yml up
```
