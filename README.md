# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qWIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMfFmShWQoLGBfO8KlyUALrkK-oNRWqpcCZVqlva6sArb50hAANbod0amD+62owUMzGhbGUvH6iZEyMBoOhtAU47JglUc4JyJ9SjxKOBkPoXrRShYyLBdBgeIAJgADK3iiVy5n0D1MOgNCYzJZrDZoNJHjBQRBOGgfP5ApgG2FizW4olUhlsjl9Gp8Z25SteRsND0S1AIjqqSnCShYggZ0g0LaffLj-yc7i9fnC5dYyaYEeF43kPA4TBjJ04wiN0QXBSFvVhBEICRRYIJ+O5oMBcgwQhKFu0rOckKRctwONYUiyGL9TlTO8YBJMlP11ajbzpf87niDQUAQJ4UEta05nwrMDivPMDVYyCALNC0AxfT40OdLhMPFD0vUEsN1U1EiTDIl1E1zb8DTLDMCMY68f1pCjzyM60e2zatqFrCjlybGA2w7Uo1LQHoByHcwrFsMwUDDad2EsZgbD8AIgmQRt-ishIZBw9JgW3XcuH3exjKzeyYkvJMDJo+IH1Cq0BKy9BhPy5iCxUCIdPuQCnj4m1PMddC1CU7C4Lw8q5w09No3qutKKY6lCro0lyVM0SaPE9qGs47jAmasqbIItqFOGkSCtvFt23AkxtuqwtBis1z9vPYbnNiNz+zQQdTD80dpg0Kc3BgABxe1MQihdopCZhTrXeIEg+4Eslydh7WKTycsoPL9OOtMgNBNwvuqLhVorITpp2mqUDqtjGRgZAeHR1Q5g2qDwlFWDcMA+0xRkOpIsCdlXxWbQEFAYN2eWOooeqCV7XhRFPvtbSifjPSqLG3aGeqJmWb+vnfRgLmedV+VBZQYXqlxpHDSBhz1x1pWYFZtkPjVjWQF563tftPWxEupyYowPb3JKM3mYtlWHc57m7a1lYdedvsfMekdbGwCwoGwbj4BZQJxeqecoqXd3AdXE2Qc3CGch1mHes7MP7TPNd-iOuX8dNZPdZQbxybmMvqkqxGa9-GB6qZevm9bh15LjEUsMlaUoQHmBw01cmh4xLaqs75HGZkA2l6NnOYgeFe4dgN2Ac90uV+8+7wOHfybEcLiH28GAACkICfVODBsW3g0zgG4uBjdngLovMrWlmTszk4AQAfFAAWx9d5V0XjeWujUwBwDjtMDAzcQFgOgJAxWMh26yzgV3HuQF+4ryphiJSdN4I+zqCALOoDwGi2Qs-OeGEEZ4PMneei5Jq74NquEHuVgtCBGIdg0hGEaaAjHp6CeK9qG0IwbAIiTChoUW4ewz2B0148I3lEXO51WzQP3o2G6+1wKR3PqOMwwBnCIHNLAYA2AE6EG-OnRczkv66ISklFKuQjC71YaNLRppuJ4AUGSSmmj2GEwkuxGANDbHhOUeI+IiVwTJQYShFEiS3QpPBsCdJA1fDMJdEk7uXj8k6yKdLSIqjDJ6IiWJSy39boGMGNdOpJgfJAA
