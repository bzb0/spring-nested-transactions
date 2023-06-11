# Spring Nested Transactions Demo

### Description

A demo Spring Boot project that uses Spring Data JDBC for persisting data in a relational database. This project demonstrates 
how Spring Data handles nested database transactions. In the project we define two services **ProjectService** and **NestedProjectService**,
and as the name suggests the **ProjectService** is calling methods from the **NestedProjectService**. All the methods from
both services start a new transaction and with this we want to test if the initial transaction started in **ProjectService**, 
will be rolled back in case the transaction started in **NestedProjectService** will fail (will be rolled back).
The project is written in Java 17 and uses Gradle as build and dependency management tool. The project tech stack is:

```
  Java              17
  Spring Boot       2.7.12
  Gradle            8.1.1
```

### Building the project

In order to build the project, the following Gradle command has to be executed:

```shell
./gradlew clean build
```

### Starting the application

The Spring Boot application can be started with the following Gradle command:

```shell
./gradlew bootRun
```

The application will be started at port **8080** under the context root **/spring-nested-transactions**. The following CRUD endpoints will be available:

* POST http://localhost:8080/projects
* GET http://localhost:8080/projects/{id}
* PUT http://localhost:8080/projects/{id}

If the endpoint `PUT http://localhost:8080/projects/{id}` is called with the following request body:

```json
{
  "name": "INVALID",
  "description": "test"
}
```

the "nested" transaction started in the **NestedProjectService** will fail, so we can see what will happen with the 
transaction in the **ProjectService**.
