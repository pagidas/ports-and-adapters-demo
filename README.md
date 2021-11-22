[![card-wallet Java CI](https://github.com/pagidas/ports-and-adapters-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/pagidas/ports-and-adapters-demo/actions/workflows/ci.yml)

# Ports and adapters (aka hexagonal architecture) demo

The main focus of this project is to _have-a-go_ on implementing
a small application using 
[Ports and adapters pattern (aka Hexagonal architecture)](https://alistair.cockburn.us/hexagonal-architecture/).

This architecture improves modular code, and prioritises on defining
the protocol of your application (an API) and implementing 
its behaviour abstract from any technology. Therefore, our business 
logic is well encapsulated, and is not coupled to implementation details. 
It also helps on testing as the very architecture, due to its glossary, 
enforces the (_SOLI**D**_) **_Dependency Inversion_** principle.

## Theme of application

We implemented a card-wallet application, which can:
- create a wallet
- list all wallets
- add a pass*
- debit a pass*

`Pass`* - a pass for this example is only a sort of club-card in a super-market containing points.

## Solution

Following the ports and adapters pattern where we think in terms of 
"inside" and "outside" of the application, our business logic on the
"inside" is our very domain. Now, any external conversation (interaction)
our domain needs to do is defined by a port (an interface). Our demo
project contains two ports:

- **_driving_** or **_primary_** port.
  - shapes up driving the application by exposing all card-wallet's capabilities.
- **_driven_** or **_secondary_** port.
  - shapes up the way domain is stored and queried.

These ports (interfaces/abstractions) merely define the shape of the interaction
and not any behaviour.

As for the "outside" world, what is driving our domain application, and what is
driven by our domain application, we have two adapters (there may be more):

- **_driving_** or **_primary_** adapter.
  - exposes card-wallet's capabilities in HTTP.
- **_driven_** or **_secondary_** adapter.
  - domain is stored and queried in a NoSQL MongoDb fashion.

The "inside" world is abstract from any technology, and the "outside" world only maps
technology in terms of business implemented "inside". This isolation is the benefit of this architecture.

One thing we're exploring in this demonstration, using the ports and adapters
pattern with its clearly defined boundaries (ports) between components;
is contract testing.

A bit earlier I stated,
> _These ports (interfaces/abstractions) merely define the shape of the interaction
and not any behaviour._

Just by looking at a port, we do not really know the validity of the input we need to supply, 
just its type. Also, we do not know what exactly is the output and its variants; again, only its type.

What is lacking upfront for a client (an adapter) implementing/using a port is really a set
of rules explaining how it works. And we have attempted to do this by writing down
a single test suite. 

`Interface + Test Suite = Contract`.

By having this contract test in place, plus the isolation and boundaries of the ports and adapters
pattern, we can easily use that same contract against any layer (adapter) of the system,
concluding to excellent test coverage.

With that regards, our demo project contains two contract tests -- one per port.
Any adapter implementing/using a port will need to comply with the respective contract.

## Structure of code

The structure is a multi-module project that is broken down to:
- core modules:
    - `card-wallet-domain`
      - business logic of the application.
      - both ends of the applications are interfaces defined to shape how the business logic works.
    - `card-wallet-http-adapter`
      - API of the application is exposed via HTTP.
    - `card-wallet-nosql-adapter`
      - application can talk to a NoSQL MongoDb to exercise its business logic.
    - `card-wallet-application`
      - the so-called service or application. 
      - glues business logic with all technology-specific modules together.
      - uses Koin for dependency injection.
- helper modules:
  - `testcontainers-common`
    - testcontainers library that helps spinning up a MongoDb docker container for testing. 

## Technology

The language used to implement this demo is [Kotlin](https://kotlinlang.org/)

Production dependencies being defined in the relevant modules are:
- [http4k](https://www.http4k.org/)
- [KMongo](https://litote.org/kmongo/)
- [koin](https://insert-koin.io/)

Testing dependencies:
- [junit5](https://junit.org/junit5/)
- [hamkrest](https://github.com/npryce/hamkrest)
- [Testcontainers](https://www.testcontainers.org/)

