# Mancala Game Server

## About

This is the demonstration of Mancala aka Kalaha game.

Here you'll find the Game Server, acting as an Oracle.

## Architecture approach

As the main purpose of this project is demonstration of problem-solving skills, the most straight forward approach is used:

- To implement the game logic, and move it closer to be a real online game – 'web service' approach is chosen 
- As there's no UI, Game Server acts as an abstraction to show how game rules and actions could be implemented
- Well-known technologies and frameworks used to ensure optimal quality, security and maintainability

## Architecture Decisions

- User interactions might be fit into `REST` design principe, so we can enjoy a lot of compatible tooling for documentation, load balancing, security, logging, etc:
    - `Spring Boot` was chosen as the most mature and rich solution for building RESTful APIs
    - `Kotlin` was chosen as Java-ecosystem friendly, but more expressive language
- `MongoDB` was chosen to preserve game state, and provide ability of basic horizontal scalability:
  - Document-oriented DBMS is a good match to our data structure as
    - user interactions usually affects a bunch of 'One-to-One' entities  
    - the representation of game is easier to fit onto 'nested' structure, rather that into relational
    - OLTP will likely cause early scaling problems for such data structure in terms of sharding and transactional overhead
  - Sharding would be an easy way for horizontal scaling
  - `Spring` support for `mongo` is great
- Application Security is quite important for a smooth user experience for an online game, as we all already tired of 'unwanted guests in Zoom room'
  - To achieve that using of client-generated tokens would be enough
  - But using Spring Session is more convenient at this scale for both developer and client, as session management is fully automated with cookies
- Testing – as the application is rich of behaviours and components, it's a good example to use Testing-pyramid approach, implementing
  - many small and cheap tests to ensure dozens of business-logic cases
  - few high-order, infrastructure dependent and slow(er) tests, ensuring that all parts of application are working together
- CI/CD to help with our development process and ensure quality. While there's no big deal which to choose for this particular project, GitHub Actions is a good choice as easy, stable, free and fast solution. 

## Architecture drawbacks

- Clients don't have ability to get notified when opponent moves
  - In those terms it would rather make sense to have game running via bidirectional protocol, like WebSockets
  - While the 'Long Pooling' might be a temporary solution, it would make scaling of entire application stack problematic

To provide such ability, we'll need a Pub/Sub (or a Queue, depending on readability requirements), to which Web Service instances would be connected to.

## Clarifying specific game rule cases

#### When move ends is empty pit

If it's players **OWN** pit, then stones from opponents opposite pit are captured straight to own mancala.
> While there an alternate rule exists, allowing player to capture opponents opposite pit to its own pit, but this case isn't covered as alternative game rule yet. 

If it's opponents pit - nothing happens, opponent is taking his next turn.

#### Clearing the board when game ends

Players are grabbing all stones from their own pits and putting them into own mancala. 

> There's also an alternate rule exists, allowing players to split board vertically and put all the stones to the mancala on corresponding left or right mancala, but this case isn't covered as alternative game rule yet.

## Development

### Local run

Requirements:
- JDK 17
- Gradle 8
- Kotlin 1.9.0
- MongoDB server

#### Local mongodb
```sh
$ docker run -d -p 27017:27017 mongo:latest
```

or refer to an example in `docker-compose.yml` 

#### Run
```sh
$ gradle clean bootRun
```

#### Swagger
Available at http://localhost:8080/swagger-ui/index.html 

#### Test
```sh
$ gradle clean test
```

For more commands refer to [Spring Boot Gradle Plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/)

### CI/CD

GitHub Actions is used to test and publish application image.

See:
- [Actions](https://github.com/nezed/mancala/actions)
- [Packages](https://github.com/nezed/mancala/pkgs/container/mancala)

### Run from image

Pre-built image `ghcr.io/nezed/mancala:latest` is ready to use.

```sh
$ docker run ghcr.io/nezed/mancala:latest -p 8080:8080 [--platform linux/amd64]
```

Please ensure MongoDB instance running on localhost:27017 or correct configuration is provided in Spring profile