To build and run the Aquariux Trading System project, follow these steps:

### Prerequisites
- Ensure you have Java Development Kit (JDK) 17 installed.
- Ensure you have Maven installed or use the provided Maven wrapper.

### Step 1: Clone the Repository
Clone the repository to your local machine:
```sh
git clone https://github.com/thaihoc215/aquariux_trading.git
cd aquariux-trading
```

Or extract source code package aquariux_trading.zip then `cd aquariux-trading`

### Step 2: Build the Project
Use Maven to build the project
```sh
mvn clean install
```

### Step 3: Run the Project
After building the project, you can run it using the Spring Boot Maven plugin (port 8080 by default)

```sh
mvn spring-boot:run
```

### Step 4: Access the Application
Once the application is running, you can access the following endpoints:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (usr/pwd: sa/password)



**---**
# Project Overview

### Swagger Configuration

**Swagger Configuration**: 

SwaggerConfig.java

### Application Properties

**Configuration**: 

application.properties


### Database Schema

**Schema Definition and initial data**:
- src/resources/db/schema.sql
- src/resources/db/data.sql


### Sample API follow tasks

**Postman Collection**:
- src/main/resources/aquariux.postman_collection.json

#### Task 1: Scheduler to retrieve the pricing from the source provided and store the best pricing
```sh
src\main\java\com\test\aquariux\service\impl\PriceAggregationServiceImpl.java#aggregatePrices
```

#### Task 2: Get Latest Best Aggregated Price
```sh
curl -X GET "http://localhost:8080/api/prices/latest?currencyPair=ETHUSDT"
```
```sh
curl -X GET "http://localhost:8080/api/prices/latest?currencyPair=BTCUSDT"
```

#### Task 3: Execute a Trade
```sh
curl -X POST "http://localhost:8080/api/trades" \
     -H "Content-Type: application/json" \
     -d '{
           "userId": 1,
           "currencyPair": "ETHUSDT",
           "tradeType": "BUY",
           "amount": 1.5
         }'
```
```sh
curl -X POST "http://localhost:8080/api/trades" \
     -H "Content-Type: application/json" \
     -d '{
           "userId": 1,
           "currencyPair": "BTCUSDT",
           "tradeType": "SELL",
           "amount": 0.001
         }'
```


#### Task 4: Get User's Wallet Balance
```sh
curl -X GET "http://localhost:8080/api/users/1/wallet"
```

#### Task 5: Get User's Trading History
```sh
curl -X GET "http://localhost:8080/api/trades/trading-history?userId=1"
```