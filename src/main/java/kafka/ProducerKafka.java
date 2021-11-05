package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class ProducerKafka {
    public static String getFormatDate(Long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = simpleDateFormat.format(new Date(timestamp));
        return dt;
    }
    public static void main(String[] args) throws InterruptedException {
        //配置信息
        Properties props = new Properties();
        //kafka服务器地址
        props.put("bootstrap.servers", "10.0.12.253:18108");
        //设置数据key和value的序列化处理类
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        //创建生产者实例
        KafkaProducer<String,String> producer = new KafkaProducer<>(props);

        JSONObject jsonObject = new JSONObject();

        int code;
        Random random = new Random(1);
        int n =1;
        while(n++<3000) {
            String url = "http://sdfcvxcv/";
            code = random.nextInt(100);
            url = url + code;
            jsonObject.put("url",url);
            jsonObject.put("http_code",code);
            Long agent_time = System.currentTimeMillis();
            jsonObject.put("agent_timestamp",agent_time);
            jsonObject.put("dt", getFormatDate(agent_time));
            ProducerRecord record = new ProducerRecord<String, String>("r_source01", jsonObject.toJSONString());
            System.out.println(jsonObject.toJSONString());
            producer.send(record);
            Thread.sleep(1000);
        }
    }
}
