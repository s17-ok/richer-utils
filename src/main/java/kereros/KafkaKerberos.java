package kereros;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author richer.sun
 * @description kafka增加kerberos权限认证
 * @date 2021/9/13 1:41 下午
 */
public class KafkaKerberos {
    public static void main(String[] args) {
        //kerberos keytab设置
        System.setProperty("java.security.krb5.conf","/Users/sunshiqi/Documents/project/myFlink/src/main/resources/krb5.conf");
        System.setProperty("java.security.auth.login.config","/Users/sunshiqi/Documents/project/myFlink/src/main/resources/kafka-jaas.conf");
        System.setProperty("sun.security.krb5.debug", "true");
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "cloudwise-03:18108");
        properties.put("group.id", "topic-test");
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //kerberos认证
        properties.put("sasl.kerberos.service.name", "kafka");
        properties.put("sasl.mechanism", "GSSAPI");
        properties.put("security.protocol", "SASL_PLAINTEXT");

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
