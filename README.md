# Kohdneyms
---
Multiplayer game based on protobuf and grpc

## Compiling & Building
From checkout root run
```bash
mvn clean package
```
This will create a jar with all dependencies for the Client and Server
## Running
### Server
```bash
cd kohdneyms-server/target
java -jar kohdneyms-server-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
### Client
```bash
cd kohdneyms-client/target
java -jar kohdneyms-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
