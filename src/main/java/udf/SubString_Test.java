package udf;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SubString_Test {
    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tenv = StreamTableEnvironment.create(env,settings);

        tenv.createTemporarySystemFunction("substr", SubString.class);

        try {
            tenv.executeSql("create table scores " +
                    "(id INTEGER," +
                    "name String" +
                    ") WITH ( " +
                    "'connector' = 'kafka-0.11'," +
                    "'topic'='r_source'," +
                    "'properties.bootstrap.servers' = '10.0.9.56:18108,10.0.9.57:18108,10.0.9.58:18108'," +
                    "'properties.group.id' = 'testGroup'," +
                    "'scan.startup.mode' = 'latest-offset'," +
                    "'format' = 'csv')");
            tenv.executeSql("create table res_test " +
                    "(id INTEGER," +
                    "name String" +
                    //  " WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND " +
                    ") WITH ( " +
                    "'connector' = 'kafka-0.11'," +
                    "'topic'='r_sink'," +
                    "'properties.bootstrap.servers' = '10.0.9.56:18108,10.0.9.57:18108,10.0.9.58:18108'," +
                    "'properties.group.id' = 'testGroup'," +
                    "'scan.startup.mode' = 'latest-offset'," +
                    "'format' = 'csv')");
            tenv.executeSql("insert into res_test select\n" +
                    "id,\n" +
                    "substr(name,0,1)\n" +
                    "from\n" +
                    "scores");

            tenv.execute("hi");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
