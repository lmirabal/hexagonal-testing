# Testing using hexagonal architecture

Use a bank example to demonstrate how using a hexagonal architecture can help to test an application functionality at
different scales.

## Structure

The application is split into 3 modules:

- domain: business logic driving how the bank behaves.
- HTTP: API to expose the bank functionality via HTTP.
- web: dummy web UI driving the application.

## Solution

The business logic of the bank is fully implemented as part of the domain. As per typical hexagonal architecture, where
an interaction is needed with the outside world, a contract (port) is defined to state how the application expects that
to happen. In this example, only 2 are defined:

- one that allows driving the application by exposing all the bank's capabilities - driving or primary port;
- and another to define how the bank accounts data is stored and queried - driven or secondary port.

The isolation of the business logic that the hexagonal architecture enforces, limits components to mapping the
technology in use to business terms. This is considered one of its main benefits, but here we attempt to show how it can
also greatly help testing.

Having every component in the system defined in terms of contracts means that writing tests based on those very same
contracts allow having a single suite of tests that can be run against any layer of the system, providing excellent test
coverage for the whole system with small effort.

This example mainly focuses on the contract defining how the business behaves as it highlights the benefits of the
approach. Still, the same strategy can be applied to any other port. For example, if there were multiple ways to
store/query accounts, a single test could be defined to test the contract for all of them.

## How to run

The full stack can be run in a single JVM for demonstration purposes using `Main.kt` in the `web` project:
```shell
./gradlew :web:run
```

## Technology

The intention is to have as simple as possible code, so very few external dependencies are used. It's implemented in
[kotlin](https://kotlinlang.org), my preferred programming language, and the only production dependency is the
[http4k](http://http4k.org/) library, which is used to define the HTTP API and the server templating in the UI.

For testing, only [junit5](https://junit.org/junit5/) and [hamkrest](https://github.com/npryce/hamkrest) are used.