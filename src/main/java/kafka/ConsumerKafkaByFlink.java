package kafka;//package kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class ConsumerKafkaByFlink {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        //设置checkpoint时间
//        env.enableCheckpointing(30000);
        //设置时间语义
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //设置容错模式 默认
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        //设置重启策略
//        env.setRestartStrategy(RestartStrategies.noRestart());
//        //设置checkpoint保存地址
//        env.setStateBackend(new FsStateBackend("hdfs://localhost:9000/fink/checkpoints"));
//        //设置checkpoint的完成时间，超时则抛弃
//        env.getCheckpointConfig().setCheckpointTimeout(500);
//        //同一时间只能设置一个检查点
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        Properties pro = new Properties();
        pro.setProperty("bootstrap.servers","10.0.12.254:18108");
        pro.setProperty("zookeeper.connect","10.0.12.254:18127");
        //flink1.11.1
        //DataStreamSource<String> data = env.addSource(new FlinkKafkaConsumer011<String>("topic-test", new SimpleStringSchema(), pro));
        //flink1.12.1
        DataStreamSource<String> data = env.addSource(new FlinkKafkaConsumer<String>("r_source01", new SimpleStringSchema(), pro));
        data.print();
        env.execute("this is kafka_test");
    }
}
