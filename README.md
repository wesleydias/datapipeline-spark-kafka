Big data processing pipelines using docker-compose
==================================================

Project will show how to build big data pipelines using containers for kafka, spark.

## Install Kafka with DataPipeline on Docker

We will set up our environment with docker-compose so for that, please clone the repository : https://github.com/wesley84/DataPipeline.

Once it’s done, in order to start the tutorial, please :
```bash
> docker-compose up
```

If you want to be able to access your kafka locally on your computer, you shouldn’t forget to add kafka to your /etc/hosts :
```bash
echo '127.0.0.1    kafka' >> /etc/hosts
```

Now open a new terminal and enter in your container kafka.
```bash
> docker exec -it <container_kafka_name> bash
```
and then we will run our producer :
```bash
> java -cp /app/datapipelines_kafka_spark_2.11-0.1.0-SNAPSHOT.jar BasicProducer zookeeper:2181 stocks
```
and then we will run our consumer :
```bash
> java -cp /app/datapipelines_kafka_spark_2.11-0.1.0-SNAPSHOT.jar KafkaTickerReader zookeeper:2181 stocks
```

### Let’s clean our terminals :

On the terminal connect to the kafka container in docker, just do a CTRL-C (to quit the producer) and then :
```bash
> exit
```
On the terminal with the docker-compose running do a CTRL-C and then :
```bash
> docker-compose down
```

### Links :
https://kafka.apache.org/quickstart
