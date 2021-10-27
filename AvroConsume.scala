package bixiproject3

object AvroConsume extends App {

  import io.confluent.kafka.serializers.KafkaAvroDeserializer
  import org.apache.avro.generic.GenericRecord
  import org.apache.kafka.clients.consumer.ConsumerConfig._
  import org.apache.kafka.clients.consumer.KafkaConsumer
  import org.apache.kafka.common.serialization.StringDeserializer

  import java.time.Duration
  import java.util.Properties
  import scala.collection.JavaConverters._

  val consumerProperties = new Properties()
  consumerProperties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  consumerProperties.setProperty(GROUP_ID_CONFIG, "test")
  consumerProperties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  consumerProperties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)
  consumerProperties.setProperty("schema.registry.url", "http://localhost:8081")
  consumerProperties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest")

  val consumer = new KafkaConsumer[String, GenericRecord](consumerProperties)
  consumer.subscribe(List("xxxxxxxx").asJava)

  println("| Key | Message | Partition | Offset |")
  while (true) {
    val polledRecords = consumer.poll(Duration.ofSeconds(1))
    if (!polledRecords.isEmpty) {
      val recordIterator = polledRecords.iterator()
      while (recordIterator.hasNext) {
        val record = recordIterator.next()
        println(s"| ${record.key()} | ${record.value().toString} | ${record.partition()} | ${record.offset()} |")
      }
    }
  }
}


