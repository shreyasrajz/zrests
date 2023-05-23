# Onboarding project

### Build a system to support the following operation.

- [x] API to insert restaurant data into MySQL table using both sync and Async flow.
  - [x] Sync flow—directly insert data in MySQL table
  - [x] Async flow – Use Kafka producer and consumer
  You can take basic restaurant details like (res name, address, contact email, contact number etc.)
- [x] API to modify restaurant details
- [x] API to fetch list of restaurants
- [x] API to fetch single restaurant data by restaurant id
- [x] API to delete a restaurant
- [x] Implement a caching layer on top of database – read queries should not go directly to database.
- [ ] Write data to dynamo db as well

### Technology to use: 
- Java, Spring boot, MySQL, Kafka, Redis, Dynamo