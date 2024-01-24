# Java POS System API Collection

## Introduction
This Postman collection contains a set of API endpoints for interacting with a Java Point of Sale (POS) System. It includes functionality related to customers, items, and orders.

## Getting Started
Follow the steps below to use the APIs in this collection:

1. **Clone the Repository:** Clone the repository containing the Java POS System code.

2. **Set Up the Environment:**
   - Open Postman.
   - Import this collection in postman : [Java POS Collection](https://www.postman.com/supply-physicist-73840039/workspace/java-pos/collection/30946389-9ab2f581-2fd4-42f8-80c2-a58df65d2711?action=share&creator=32541800).
   - Create a new environment and configure the required variables (e.g., base URL).

3. **Run the Requests:**
   - Open the desired request.
   - Ensure that the required parameters are set in the request body or as query parameters.
   - Click "Send" to execute the request.

## Collection Structure
- **Customers:**
  - `GET /customer?action=getAllCustomer`: Get all customers.
  - `GET /customer?action=getCustomer&customerId={customerId}`: Get customer details by ID.
  - `POST /customer`: Create a new customer.
  - `PUT /customer`: Update customer information.
  - `DELETE /customer?customerIdValue={customerId}`: Delete a customer.

- **Items:**
  - `POST /item`: Create a new item.
  - `PUT /item`: Update item information.
  - `DELETE /item?item_code={itemCode}`: Delete an item.
  - `GET /item?action=getAllItem`: Get all items.
  - `GET /item?action=getItem&itemId={itemId}`: Get item details by ID.

- **Orders:**
  - `POST /order`: Create a new order.
  - `PUT /order`: Update order information.
  - `DELETE /order?order_id={orderId}`: Delete an order.
  - `GET /order?action=generateOderID`: Generate a new order ID.
  - `GET /order?action=getAllCustomerId`: Get all customer IDs.

## Additional Notes
- Ensure that you have the necessary permissions to perform certain actions.
- Check the request bodies for required parameters and formats.
- Review the response status codes for success or failure.

Feel free to explore and test the API endpoints using this Postman collection.

## Database Connection Configuration

<Resource name="jdbc/pos_system" auth="Container" type="javax.sql.DataSource"
    maxTotal="10" maxIdle="8" maxWaitMillis="-1"
    username="your user name" password="your password" driverClassName="com.mysql.cj.jdbc.Driver"
    url="jdbc:mysql://localhost:3306/pos_system"/>

This repository contains the source code for pos_system. Below are instructions for configuring logging using SLF4J and Logback.

## Logging Configuration

The application utilizes SLF4J as the logging facade and Logback as the logging implementation. The configuration for logging can be customized by modifying the `logback.xml` file.

### Logback.xml

The `logback.xml` file is the configuration file for Logback. It allows you to control various aspects of logging, including log levels, output format, and log file locations.

Here is an example of a basic `logback.xml` configuration:

## xml
<configuration>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <!-- Pattern to output the caller's file name and line number -->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Pattern>
        </layout>
    </appender>

    <root level="info">
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>

## Additional Information

 -- Make sure you have a MySQL server set up.
 
 -- Adjust database credentials and connection details in your application configuration.
 
 -- Customize the schema as per your application's requirements.
 
 -- Feel free to explore and modify the database schema based on your project's needs.

Happy coding!
