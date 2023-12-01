# Opentelemetry with Springboot

As part of this Proof of Concept (PoC), we are looking to instrument springboot applications with Opentelemetry.

We have checked for distributed tracing across http calls and asynchronous messaging via RabbitMQ messaging across service. The traces are visualized in a Jaeger backend and the application logs are indexed in splunk to correlate logs with traces.

## Prerequisites to run
- Java 17
- Docker

## Build and run the project
- Build the projects

    ```bash
    make
    ```

- Deploy the services and wait for them all to be healthy

    ```bash
    docker-compose up --build
    ```

## Services deployed

- #### order-service
  A sample order service deployed as a springboot web application exposing endpoints to place and order and acknowledging gratitude

- #### inventory-service
  A sample inventory service deployed as a springboot web application exposing endpoints to understand inventory

- #### jaeger
  The jaeger service where open telemetry exports traces to visualize them

- #### rabbitmq
  The rabbitmq service to enable async messaging among the services

- #### splunk
  The splunk core service to view a local splunk instance

- #### splunkforwarder
  The splunk universal forwarder to forward log events to the splunk indices

## Common logging library

The `common-logging` library contains the logging configuration shared between the two springboot services `order-service` and `inventory-service`.

## Request flow traced

![Request Flow](images/Request-Flow.png?raw=true)

1. The trace starts with a call to the `POST /order` call to the `order-service` to place an order
2. The `order-service` checks with `inventory-service` if items are available for the order to be placed by calling the `GET /inventory` API
3. If items are available, the `order-service` confirms the order and updates the inventory by calling the `PUT /inventory` API call in `inventory-service`
4. The `order-service` makes a follow up http call to `inventory-service` at the `GET /inventory` API to log the balance of items available
5. The `order-service` publishes a message to the `q.gratitude` queue in RabbitMQ, with the gratitude message received from the original `POST /order` call for `inventory-service` to consume
6. The `inventory-service` which has a listener to the `q.gratitude` queue, processes the gratitude message
7. Finally, the `inventory-service`, post processing the gratitude message, makes an http call to `order-service` at the `GET /gratitude-ack` endpoint for acknowledging the gratitude

## Run, visualize, and correlate the trace

To run the request mentioned above and trace it:
1. Build and run the project
2. Make a call to `POST /order` via the following curl command:
    ```bash
    curl --request POST \
    --url http://localhost:8081/order \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data noOfItems=15 \
    --data 'gratitude=Thank you!'
    ```
3. Visit the jaeger dashboard on `http://localhost:16686`. Under the search tab select **order-service** -> Click on **Find Traces** -> From the results click on **order-service: POST /order**. You should be able to visualize the request trace as shown below:

   ![Jaeger Trace](images/Jaeger-Trace.png?raw=true)

4. Capture the trace id from the URL which is in the format `http://localhost:16686/trace/<trace_id>`
5. Head over to the splunk dashboard at `http://localhost:8000/`. Login with username **admin** and password **password** -> Go to **Search & Reporting** app -> In the search query paste the following splunk query to view the logs threaded by the trace id captured in Step 4: `index="main" | spath trace_id | search trace_id="35341aa5fa30ee24f9b5e07b12f14c12"`
