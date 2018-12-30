# Notes to the Coding Test

I'd very much like to receive all the feedback you have on my implementation, especially in the areas of concurrency, performance, design. Thanks in advance.

I hope you enjoy reviewing this test as much as I enjoyed solving it.

## Implementation notes

  - I've followed a 'make it work, then make it concurrent' approach, using some principles described in the Java Concurrency in Practice (JCIP) book.
  - I've followed a double-loop, outside-in TDD approach, using mockist as my main style.
  - Followed a Clean Architecture style
      + Left the JSON/REST delivery details as outside as possible.
      + Implemented a custom Serializer/Deserializer to keep the JSON details outside
      + Given the system is simple enough, did not give too much importance to making the DomainService/ApplicationService distinction
  - Mixed kotlin and java, as this was allowed in the description. 
      + As I developed the code first, all of it in Kotlin, then spawned a jcstress/concurrency project, I had to rewrite the `TransactionRepository` in java, as the jcstress decorates/extends classes at compile and it was not playing nice with my kotlin code. (see 'Integrating jcstress')
  - Didn't mind putting multiple classes in a 

## Git History

There is a repo for the development: https://github.com/alvarotd/test-1

There is another repo for the concurrency work: https://github.com/alvarogarcia7/statistics-concurrency-test

## Performance

The class `Snapshot` has all the magic for trying to make the code as O(1) as possible.

My take on it has been to always keep a snapshot of the results:

  - When adding, modifying the snapshot
  - When data expires (after one minute), modifying the snapshot. This expiring is done via a scheduler, to make it as light as possible, not looping through the elements.

Some concerns:

  - I've kept the maximum of the valid values seen, not of the all of them (i.e., all seen) values.
      + Imagine at 09:00, a transaction of 10.00
      + Imagine at 09:01, a transaction of 1.00
      + At 09:01, we query the statistics:
         -  Following the valid-transactions strategy, the max should be '1'. Implemented in revision [405afc](https://github.com/alvarotd/test-1/commit/405afc727be33e91d37df87a88a0703aa20576e4). This follows the O(1) performance requirement
         - Following the all-transactions strategy, the max should be '10'. Implemented in revision [f0f685](https://github.com/alvarotd/test-1/commit/f0f685f69e8dd92c34a275867dadf7056c11cb94). This does not follow the O(1) performance requirement
      + This test case is not covered by the tests, so I was unsure of what to do. I would normally ask my Product Owner, but this being a Christmas Sunday, so close to the deadline I cannot expect a response on time.

  - To keep the maximum and minimum elements, I've used a `PriorityQueue`, which guarantees O(n log n) for insertion, removal (being n the number of present elements). Also guarantees O(1) for accessing (peek).
  - I haven't found provides record of all past elements (as they all will expire), that provides O(1) for insertion, removal, query for the given scenario.

According to my understanding, you cannot guarantee O(1) in time+memory performance, while returning 'specifying single highest/lowest transaction value in the last 60 seconds' In this case, I have decided to break the O(1) performance requirement, to make it correct according to the requirements.


## What I would do different

### TransactionController

The code is not as clean as I'd like it to be. The reason is that the controller cannot have `Exception Handlers` (Spring REST), because they are not mounted when doing `mockMVC`. The requirements specify you cannot change the `it` folder, therefore, cannot change it to `standaloneMVC` or similar. (I've left the exception handler code in another revision: [bfd58](https://github.com/alvarotd/test-1/commit/bfd58484093a549b5d1bffae6619374444b8e308))

I'd prefer using a `@Valid` annotation to let Spring handle the validation of the object and receive a valid DTO object.

This is why I need an ObjectMapper in my controller.

### Formatting the statistics

The responsibility of formatting the statistics is in `Snapshot`. It should not be there, as this is 'delivery' thing.

### Integrating jcstress

I'd integrate the jcstress/concurrency tests into the same project. Right now, they have different lifecycles and purposes, so it's easier to have them as two different projects.

### Spring

These days I prefer using micronaut (https://micronaut.io) rather than Spring. Micronaut is much faster at runtime (800 ms to start the server), and provides compile-time dependency checking (fewer runtime problems of 'cannot find this class').

## What I like

### Replacing dates

It was easy to replace all the dates from `LocalDateTime` to `ZonedDateTime`, so this shows the code was well designed enough for that change.

### Integration test suite

I like the comprehensive, fast integration test suite provided.

Also, expanded it with E2E tests (not using `mockMVC`) at the unit scope, to make sure everything works as expected.

### Clean architecture

I like the cleanliness that Clean Arch provides. 

Most of the real churn happens in the business logic, not in the delivery details, so let's keep these two separate. This also allows for cheap, extensive testing at the unit/integration level (cheap, fast).


# Requirements

We would like to have a RESTful API for our statistics. The main use case for the API is to calculate realtime statistics for the last 60 seconds of transactions.

The API needs the following endpoints:

  - POST /transactions – called every time a transaction is made.
  - GET /statistics – returns the statistic based of the transactions of the last 60 seconds.
  - DELETE /transactions – deletes all transactions.


You can complete the challenge offline using an IDE of your choice. To download the application skeleton, please enable Use Git in the editor and follow the instructions on screen. Please make sure you push your changes to the master branch and test your solution on HackerRank before submitting.


## Specs

### POST /transactions

This endpoint is called to create a new transaction. It MUST execute in constant time and memory (O(1)).

Body:
```
{
  "amount": "12.3343",
  "timestamp": "2018-07-17T09:59:51.312Z"
}
```
Where:

  - amount – transaction amount; a string of arbitrary length that is parsable as a BigDecimal
  - timestamp – transaction time in the ISO 8601 format `YYYY-MM-DDThh:mm:ss.sssZ` in the UTC timezone (this is not the current timestamp)


Returns: Empty body with one of the following:

  - 201 – in case of success
  - 204 – if the transaction is older than 60 seconds
  - 400 – if the JSON is invalid
  - 422 – if any of the fields are not parsable or the transaction date is in the future


### GET /statistics

This endpoint returns the statistics based on the transactions that happened in the last 60 seconds. It MUST execute in constant time and memory (O(1)).

Returns:
```
{
  "sum": "1000.00",
  "avg": "100.53",
  "max": "200000.49",
  "min": "50.23",
  "count": 10
}
```
Where:

  - sum – a BigDecimal specifying the total sum of transaction value in the last 60 seconds
  - avg – a BigDecimal specifying the average amount of transaction value in the last 60 seconds
  - max – a BigDecimal specifying single highest transaction value in the last 60 seconds
  - min – a BigDecimal specifying single lowest transaction value in the last 60 seconds
  - count – a long specifying the total number of transactions that happened in the last 60 seconds

All BigDecimal values always contain exactly two decimal places and use `HALF_ROUND_UP` rounding. eg: 10.345 is returned as 10.35, 10.8 is returned as 10.80



### DELETE /transactions

This endpoint causes all existing transactions to be deleted

The endpoint should accept an empty request body and return a 204 status code.


## Requirements

These are the additional requirements for the solution:

  - You are free to choose any JVM language to complete the challenge in, but your application has to run in Maven.
  - The API has to be threadsafe with concurrent requests.
  - The POST /transactions and GET /statistics endpoints MUST execute in constant time and memory ie O(1). Scheduled cleanup is not sufficient
  - The solution has to work without a database (this also applies to in-memory databases).
  - Unit tests are mandatory.
  - mvn clean install and mvn clean integration-test must complete successfully.
  - Please ensure that no changes are made to the src/it folder since they contain automated tests that will be used to evaluate the solution.
  - In addition to passing the tests, the solution must be at a quality level that you would be comfortable enough to put in production.

