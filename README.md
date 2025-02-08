# ProductsRepository
Spring Boot Microservice with Kafka (Producer) for learning purposes

## Proyects related
Producer\
[Producer Repository]([https://www.example.com](https://github.com/hugotb88/ProductsRepository))

Consumer\
[Consumer Repository](https://github.com/hugotb88/EmailNotificationService)

Core Library\
[Core Library](https://github.com/hugotb88/coreKafkaLibrary)

# Important Notes
If you are running Kafka in docker and your are loading the ```HOSTNAME``` variable from a file, use this command

```docker-compose -f docker-compose-triple.yml --env-file environment.env up```

If your applications shows a connection timeout error, maybe the microservice is not able to find ```localhost:9092``` as bootstrap servers as you indicate in the .properties file.
You can review two things:
- ```spring.kafka.producer.bootstrap-servers=bootstrap-servers=localhost:9092,localhost:9094,localhost:9096``` parameter configured in ```appilcation.properties```
- ```127.0.0.1``` mapped to ```localhost``` in ```host``` file, in Windows, is located in ```C:\Windows\System32\drivers\etc``` and should have a line like this on

```# Copyright (c) 1993-2009 Microsoft Corp.
#
# This is a sample HOSTS file used by Microsoft TCP/IP for Windows.
#
# This file contains the mappings of IP addresses to host names. Each
# entry should be kept on an individual line. The IP address should
# be placed in the first column followed by the corresponding host name.
# The IP address and the host name should be separated by at least one
# space.
#
# Additionally, comments (such as these) may be inserted on individual
# lines or following the machine name denoted by a '#' symbol.
#
# For example:
#
#      102.54.94.97     rhino.acme.com          # source server
#       38.25.63.10     x.acme.com              # x client host

# localhost name resolution is handled within DNS itself.
#	127.0.0.1       localhost
#	::1             localhost
127.0.0.1   host.docker.internal    #To map localhost to host.docker.internal
# Added by Docker Desktop
192.168.1.147 host.docker.internal
192.168.1.147 gateway.docker.internal
# To allow the same kube context to work on the host and the container:
127.0.0.1 kubernetes.docker.internal
# End of section 
```

## To Review a topic as Consumer
```./kafka-console-consumer.sh --topic product-created-events-topic --bootstrap-server host.docker.internal:9092 --property print.key=true```

# Notes about Kafka Producer
- If you configure your Producer to get an acknowledgement about that the message was received and stored \
by all the followers (Replicas),performance will decrease but reliability increase. 


```spring.kafka.producer.acks=all``` to configure to get acknowledgment from all the followers (in sync Replicas).

```spring.kafka.producer.acks=1``` to configure to get acknowledgment from the leader only. Useful when the message is not a lot critical.

```spring.kafka.producer.acks=0``` to configure to NOT get acknowledgment . Useful when the amount of messages is high but they are not critical.
not guarantees that the message is stored, but is faster (e.g. GPS locaiton in real time).

----


* When a Kafka Producer sends a message to a broker we have the following options
  * No Response -> acks=0
  * Acknowledgment of successful storage
  * Non-Retryable Error -> A Permanent problem that can't be resolved by retries (e.g. big message).
  * Retryable Error -> A Temporary problem that can be resolved by retries.

```spring.kafka.producer.retries=10``` Configures the number of retries by the Producer to send a message. \
Default value of retries in Kafka is 2147483647 times. 

```spring.kafka.producer.properties.rety.backoff.ms=1000``` Configures the amount of time that kafka will wait until the next retry.\
Default is 100ms

```spring.kafka.producer.properties.delivery.timeout.ms=120000``` Maximum time that Producer can spend trying to deliver the message. \
Default value is 120000 (2 mins), if we change it we need to follow these rules:
* delivery.timeout.ms > linger.ms + request.timeout.ms
  * ```spring.kafka.producer.properties.linger.ms=0``` -> Maximum time that Producer will wait and buffer data before sending a batch of messages.
  * ```spring.kafka.producer.request.timeout.ms=30000``` -> Maximum time that Producer will wait for a response from the broker.

``` 
./kafka-topics.sh --create --topic insync-topic --partitions 3 --replication-factor 3 --bootstrap-server host.docker.internal:9092 --config min.insync.replicas=2
```
To create a Topic with in-sync replicas = 2, that means, two brokers replicating what the leader receives and stores.

To modify an existing topic:

```
./kafka-configs.sh --bootstrap-server host.docker.internal:9092 --alter --entity-type topics --entity-name product-cr
eated-events-topic --add-config min.insync.replicas=2
```
To modify the number of in-sync replicas in a topic.

----

# Make an IDEMPOTENT PRODUCER

_Usually this is a default configuration._

Lets think in the following scenario: 
* A Producer sends a message "A".
* Broker receives the message and stores it in a Kafka topic.
* Broker sends and acknowledgment but a network error happens and it doesnt come to the producer.
* The Producer notices that and retries to send the message "A"
* Broker receives the message and stores it in a Kafka topic.
* Broker sends and acknowledgment and this time the Producer receives it.

Now wee have data duplicated, sometimes is not a big deal, but that can create inconsistency for some apps, thats where an idempotent producer comes.

**"An Idempotent Producer avoids duplicate messages in the presence of failures and retries"**

This means that the producer can send the same message multiple times but it will be stored once, ending with this workflow:
* A Producer sends a message "A".
* Broker receives the message and stores it in a Kafka topic.
* Broker sends and acknowledgment but a network error happens and it doesnt come to the producer.
* The Producer notices that and retries to send the message "A"
* Broker sends and acknowledgment and this time the Producer receives it.

We need to configure the following in the properties file:
`spring.kafka.producer.properties.enable.idempotence=true`

If its a Bean configuration \
```
@Bean
props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
```

### Considerations
We need to take in consideration the following rules when we have an idempotent producer:

* `spring.kafka.producer.properties.acks=true` -> should be true
* `spring.kafka.producer.properties.retries=2147483647` -> should be greater than 0
* `spring.kafka.producer.properties.max.in.flight.requests.per.connection=5` -> should be equal or less than 5








