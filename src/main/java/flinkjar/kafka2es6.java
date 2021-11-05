package flinkjar;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author richer.sun
 * @description kafka到es6
 * @date 2021/9/28 10:32 上午
 */
public class kafka2es6 {
    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tenv = StreamTableEnvironment.create(env,settings);
        try {

            tenv.executeSql("create table scores " +
                    "(mes String" +
                    ") WITH ( " +
                    "'connector' = 'kafka'," +
                    "'topic'='r_source01'," +
                    "'properties.bootstrap.servers' = '10.0.9.44:18108'," +
                    "'properties.group.id' = 'testGroup'," +
                    "'scan.startup.mode' = 'latest-offset'," +
                    "'format' = 'json')");

            tenv.executeSql("CREATE TABLe bb(mes STRING) with('connector' = 'elasticsearch-6','hosts' = 'http://10.0.9.229:9200','index' = 'aaa','document-type' = 'bbb')");
            tenv.executeSql("insert into bb (select mes from scores)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
