# ProductsRepository
Spring Boot Microservice with Kafka (Producer) for learning purposes

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
# End of section```



