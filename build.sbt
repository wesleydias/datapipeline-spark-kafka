name := "datapipelines_kafka_spark"

scalaVersion := "2.11.7"

val sparkVersion = "2.3.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  ("org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion) exclude ("org.spark-project.spark", "unused"),
  "org.apache.kafka" % "kafka-clients" % "0.10.0.1"
)

assemblyJarName in assembly := name.value + "allinone.jar"