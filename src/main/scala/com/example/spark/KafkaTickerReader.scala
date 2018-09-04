package com.example.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._


object KafkaTickerReader {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().getOrCreate()
    import spark.implicits._

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

    val tickerSchema: StructType = new StructType()
      .add("ticker", "string")
      .add("price", "int")

    val streamingTicker = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", brokers)
      .option("subscribe", topics)
      .option("startingOffsets", "earliest")
      .option("failOnDataLoss", "true")
      .load()
      .selectExpr("CAST(value AS STRING)")
      .select(from_json($"value", tickerSchema))

    val result = streamingTicker.groupBy(window($"timestamp", "30 minutes", "5 minutes"),$"ticker").avg("price")

    println(result)

  }
}
