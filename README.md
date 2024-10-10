# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## My Sequence Diagram

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUmEBFxBoOKlxgPSmo3UwHs9ymAFEoN4yjA9BwYJC0RjJgcjpQTqdsuZygAWJxObokvpk1QUql9Wn08pMlkoNmxDmHYqiE6FWTyJQqdTlSlgACqHVhNzu2O1imUalUmqMOlKADEkJwYCbKFbGWFzcBRpodDytdpdbbyjcYAAzXycTBW0PqINFEQqcMdNUpwy5U644HlSj0nHnYFJrkMsUSqXjGl06DlYAISHADhheQAa3QqrL2cKfIwgqcAGZRaTbVXyjLawyG02WzB252YAd4zbEz3k1RwTA0D4EAhTGCVPaV3rVKUQE3Mh64Tcrdjc5R12WrqPyZTq5O5TBYRw1BfiAQiQwBAkYwAW0CYl2hL4r2yD8jAACswojuKY7vhOsp1t+v6qP+2CAUkIFgVOkFLnGIarnapzGKUCgcMy15Whmm5HqcJ5hheKByCgCg+GAqSwsAfGpHe5E6pR9o0XRzK8fxTGHlmObFkSCLQsiai7lgD4wRuRLXB0dyTBOjxTEJ-H1BAHZoEZUz7Mx3Lrn2YDlAATMhvQDAZfpjDAxlfGZqQWVZNmrAc6AcKYXi+P4ATQOwlIwAAMhA0RJAEaQZFkcF5IUz6VLUDTNC0BjqAkaCip5QzebsXwvG8HyhfZUAFNpL6Vbc1WfE8dVLDVZHaQUCmlAgKWurCI2pai6KxNiCnHhRp4Gigxqmr6oyWgttqSY6Lpuox2jejAa1iMYSbsfqR0dNuEDMNGPixudqg9kN267vuCk9q1xGFmWSZOeUSEir0r6SuhNZfj+f5QABZXAaB4FQKRByneuj1ptyd0PZta4Epm6NQMxGpsdjZ4wFQDZIFomRVPoqiwj17wbeJp4FDRUausyDMRcYPZozAXPXbdMYRY9z3qqmr17oTKifcpDII0WQKPoN0FtZWYOflh90BdASAAF4oMsZHdgU-0wEKACMKHq9KmEMtrwm6wbRvIzovMk+UAubGAICpGJ1osyreMC+TyARR9LVy+UE2upEqiaYreJPqrxJTMdNVPAFQWdp8xvQX92XOTAblA2K6ddaZwnZ9ZudhbGnjeH4gReCg6BJSlvjMOl6SZJgTn2nlFTSNSiXUvU1JFSVqhld0WeWegTWR0r05V-PaCJ8CQcsSgw0d3x4172AU0YrN4tZsGzNhmTFNUzxwmCavVlMwHW3UTtHMwAF8ln7Ly-5lOTUC45CLoDa2aFbZTnKJDXC0N8KwyIgjJGAYzoe0-sJL0aAbpRmFv7BMT0t5bh3FLCO+QvoKwGlvPSINxzgy1sER20NnZQWoMrfIZtLZgLfBAr8Dt+JO0NqqFGxNL4XS-gdTGIsSZizxmguS2hpYoF-niaOh844JwoblFOJJOT50coXVy7ltFmHrlFJuARITMkStCGAABxbydpu6ZT7oXAeKcKg2LHkVew3lZ6PwXt2QoX055WQ3o+fIQ1kCxDsWSca0Jj4zQUQUNGodKbcVkgJYJ6Bn54O2s6D+Yj5DINRqggpwBBbYPupIkR+Dwln3KEQ96ujeT6MQoY6hGs7ZQJwnhAicNvoQWYdvJReZ+kEw0XlCs4CPydKOvQvhjCBF5xYc1U2LSOHA1Qlw6ZkDZk6wWS7IpwiX6iPQeInBosCES1KYk-IfNDTRLULCbJlEew0UjB-bxowmI8ySR7T5KAmIXNqTI-539MzDKJJEo+GkEBaTltIkopQxT-NUNWCo-R-kAElpDVgti5QcAongZSvF5UYKwvg6AQKANs15DITC+P8gAct5elewYCNB0csnsZsS4oRRWijF3lsW4vxYSqYxKUC0s6n0ClVKQA0tJfcGVTwmUsplWyjlxiIoN2ioEbAPgoDYG4PAS8hgHkpB7llYBrjlnlGqHUJorR-m+PMmvUUqrRicpKEpP+sjAput6B6lAAJ4XAu3sNaEDy6ZQviViUJOkJntO4VhBUrJppgEGQ5ZpwCBylyTdsr8qalTpsGaIX51TSgpNvukh+rqn64Ikm-PJbprlCIvsc0mpTykSIbYHMNhC3oBndhWrtPaSFkIAUCoa1zxlaPzRhHZvDUj8IOSbNhaynBWw2TbAtdC9n60WV6rNsEc2tLzZs0GyaGTQJ6fA+GJFS0y1uagzi3EHmwiDc8lm+Q2bNmZKC+Rs7bXIqFTi8oeKCVLO9as09vKNlYrAzACDAoyLhUio3GKlgUB7ggMkGAAApCArpbHeUCJS6lzjrW40RflI0jqWjOrmf6qyoojXACw1AOAEARpQFmAho9zVAlRz9dXFCbGONcZ4ysAA6iwTF48WgACFEoKDgAAaQZaBkVkGQ3L0uTvAAVkRtA76jOx2LSfeNpY50XpoZrBkRblQZqg8e+AG7z07oXYW5sionOPsUc+itVa0n30yWgL9r9Cg0V2syVtgZikVr9RgrBPap11Mlo05ZQD4KgO3VMrzWEb2wN6Qgh9UGy1L2UaM+NCKqG2Y6YupjK7M0rPXae9ZkytkFftk1-Zgi3blo7aUUd5ypH6eG6cwpH1AtDfZpwd9CGIvqFZo6Ob-7vLfIGzNvBBoNsHUwULSpvbbQIvBA0xJlW8wKx+dtyiu2vlnKO2lvGAGps-37RLEoSBIxhHE5QST0BykLnXuOqO5D4WULKJ1y9u7pyNi4nOYHLXuX6KFMOPLXXaFw9nK2YAwVytPr5n+qoVKOO1uY1k47y2f2rb-TAV7wALukOE2ZtAajYU1ch0ilzrX-pwaMWhnVZivDsbc1xWAwBsBGsIPEICjje792o0SIeI8x4T1aMYRezPfW-m4L3AamiZEgG4HgJ5NyZCoMhBYVANArTk9mDcPj3kltPRp+ebj3BwCbeHbNgKDuOhO9GEYeRz3w3B7e1lvRp7cvQ7szMorMMgKlfpEg6bfNjfi7ty7lb5RYjQEMBsfCYQqb6GWhoEhZxfUZ7wOzuFemlflh59l-sxc2l1wikAA
