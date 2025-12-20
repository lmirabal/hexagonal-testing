# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin demonstration project showcasing how hexagonal architecture enables testing at multiple layers using the same test contract. The application is a simple bank system with account creation, deposits, and withdrawals.

## Architecture

The project uses **hexagonal architecture** (ports and adapters) to enforce separation of concerns:

### Module Structure

- **domain**: Core business logic with no external dependencies
  - `Bank` interface: Primary port (driving) defining all bank operations
  - `BankAccountRepository` interface: Secondary port (driven) for data persistence
  - `BankLogic`: Implementation of `Bank` that orchestrates business rules
  - Uses `testFixtures` to publish `BankContract` - an abstract test suite used across layers

- **http**: HTTP API adapter
  - `BankHttp`: Exposes `Bank` interface via HTTP using http4k
  - `BankHttpClient`: Implements `Bank` interface by calling the HTTP API
  - Tests extend `BankContract` and test through HTTP endpoints

- **web**: Web UI adapter
  - `BankWeb`: Handlebars-based web interface
  - Tests extend `BankContract` and test through Selenium WebDriver

### Testing Philosophy

The key architectural benefit demonstrated here is **contract-based testing**:

1. `BankContract` in `domain/src/testFixtures` defines the complete functional test suite
2. Each layer extends `BankContract` and provides its own `Bank` implementation
3. The same test suite validates behavior at:
   - Unit level: `BankLogicTest` tests business logic directly
   - HTTP level: `BankHttpTest` tests through the HTTP API
   - UI level: `BankWebTest` tests through the web interface

This ensures the entire system behaves consistently without duplicating test logic.

## Common Commands

### Build and Test

```bash
# Build and test (preferred - runs tests and verification)
./gradlew check

# Build without tests
./gradlew build

# Run tests for a specific module
./gradlew :domain:test
./gradlew :http:test
./gradlew :web:test

# Run a single test class
./gradlew :domain:test --tests "lmirabal.bank.BankLogicTest"
./gradlew :http:test --tests "lmirabal.bank.http.BankHttpTest"
```

**Note**: Gradle's incremental build is efficient - running `clean` is rarely needed and slows down builds. Only use it if you encounter caching issues.

### Run Application

```bash
# Run the full web application (domain + HTTP + web UI)
./gradlew :web:run
```

The application will start on the default http4k port. Access it in a browser to interact with the bank UI.

## Technology Stack

- **Language**: Kotlin 2.3.0
- **Build**: Gradle 9.2.1
- **Runtime**: JVM 25
- **Web Framework**: http4k (HTTP API and web templating)
- **Testing**: JUnit 5, Hamkrest (matchers)
- **UI Testing**: Selenium WebDriver via http4k-testing-webdriver
- **Functional Types**: result4k for `Result<T, E>` types

## Key Implementation Details

### Error Handling

The domain uses functional error handling via `Result<T, E>` from result4k:
- `withdraw()` returns `Result<BankAccount, NotEnoughFunds>` instead of throwing exceptions
- This allows errors to be mapped to HTTP status codes (400 for insufficient funds) and UI error pages

### Amount Representation

`Amount` is stored in minor units (cents) as `Long` to avoid floating-point precision issues. The web layer converts to/from major units (dollars) for display.

### Repository Pattern

The domain defines `BankAccountRepository` as an interface. `InMemoryBankAccountRepository` is the only implementation, but the pattern allows for easy substitution (e.g., database-backed repository).

### Test Fixtures

The `domain` module uses Gradle's `java-test-fixtures` plugin to share `BankContract` with other modules. Other modules declare `testImplementation testFixtures(project(':domain'))` to access it.

## Development Notes

- When adding new `Bank` operations, update both `Bank` interface and `BankContract` test suite
- Each adapter layer (http, web) should implement the new operation and inherit test coverage automatically
- The codebase intentionally minimizes external dependencies to keep the architecture clear
- Pattern matching on `Result<T, E>` uses `.map()` for success path and `.recover()` for error path
