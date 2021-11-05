package kafka;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Arrays;
import java.util.Properties;

public class ConnTest {
     public static void deleteTopic(String toicName){
        ZkUtils zk = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
        AdminUtils.deleteTopic(zk,toicName);
        zk.close();
    }
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "10.0.9.56:18108");
        properties.put("group.id", "topic-test");
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("r_source01"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(" value is =>>> %s", record.value());
                System.out.println();
            }
        }

    }
}
