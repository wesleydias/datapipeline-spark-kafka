package com.producers.kafka

/**
  * Created by umitcakmak on 30/05/17.
  */

import java.util.Properties

import org.apache.kafka.clients.producer._
import scala.util.Random
import com.google.gson.Gson

import java.sql.Timestamp


object BasicProducer {

  case class tickcode(ticker: String, price: Int, timestamp: String)

  val randomTick = List("AMZN", "MSFT", "AAPL")
  // === Configurations of amount of data to produce ===
  val recordsPerSecond = 3
  val wordsPerRecord = 10
  val numSecondsToSend = 1000

  def getPrice(tickName: String): Int = {
    if (tickName eq "AMZN")
      return 1902 / scala.util.Random.nextInt(10) + 1902
    else if (tickName eq "MSFT")
      return 107 / scala.util.Random.nextInt(10) + 107
    else
      return 215 / scala.util.Random.nextInt(10) + 215
  }

  def GsonTest(): String = {
    val tickName = randomTick(Random.nextInt(randomTick.size))
    val tickPrice = getPrice(tickName)
    val dt = new Timestamp(System.currentTimeMillis()-100)
    val p = tickcode(tickName, tickPrice,dt.toString())
    // create a JSON string from the Person, then print it
    val gson = new Gson
    val jsonString = gson.toJson(p)
    return jsonString
  }

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      System.err.println(
        s"""
           |Usage: KafkaTickerReader <brokers> <topics>
           |  <brokers> is a list of one or more Kafka brokers
           |  <topics> is a list of one or more kafka topics to consume from
           |
        """.stripMargin)
      System.exit(1)
    }

    val Array(brokers, topics) = args

    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)

    System.out.println("Starting here...")

    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val TOPIC = "stocks"

    for (round <- 1 to numSecondsToSend) {
      for (recordNum <- 1 to recordsPerSecond) {
        val data = GsonTest()
        val record = new ProducerRecord[String, String](TOPIC, data)
        producer.send(record)
      }
    }

    producer.close()

  }

}
