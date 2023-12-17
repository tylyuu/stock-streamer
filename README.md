# stock-streamer

This application is designed to process real-time stock data with results storage in MongoDB.

### Overview
The following UML consitsting of the major classes and functions showcases the data flow in this application. 
![Diagram 2023-12-17 00-13-49](https://github.com/tylyuu/stock-streamer/assets/114101094/d5329226-dcf0-4611-92a0-0e7aa7e8f904)

## Getting Started

### Prerequisites

- JDK 11
- Apache Maven
- Apache Kafka (with Zookeeper)
- Apache Spark
- MongoDB

### Building the Application

To build the application, run the following command in the root directory:

```bash
mvn clean install
```

## Running the Application

1. Start your Kafka and Zookeeper servers.
2. Start your MongoDB instance.
3. Submit the Spark job (if necessary for your SparkService).
4. Run the Spring Boot application:
   ```bash
   java -jar target/stockstreamer-0.0.1-SNAPSHOT.jar
    ```

## Configuration

The application can be configured via the `application.yml` file located in the `src/main/resources` directory. Ensure that you set the correct parameters for:

- Kafka brokers
- MongoDB URI
- Topics used for input and output




