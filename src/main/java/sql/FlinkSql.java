package sql;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import udf.UDAFSum;

/**
 *
 */

public class FlinkSql {
    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tenv = StreamTableEnvironment.create(env,settings);
        tenv.registerFunction("sumTest",new UDAFSum());
        try {
            tenv.executeSql("create table scores " +
                    "(id INTEGER," +
                    "name String," +
                    "score BIGINT," +
                    "class String," +
                    "dt as PROCTIME()" +
                    ") WITH ( " +
                    "'connector' = 'kafka-0.11'," +
                    "'topic'='source'," +
                    "'properties.bootstrap.servers' = '127.0.0.1:9092'," +
                    "'properties.group.id' = 'testGroup'," +
                    "'scan.startup.mode' = 'latest-offset'," +
                    "'format' = 'json')");
            tenv.executeSql("create table res_test " +
                    "(name String," +
                    "score BIGINT," +
                    "class String" +
                    //  " WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND " +
                    ") WITH ( " +
                    "'connector' = 'kafka-0.11'," +
                    "'topic'='sink'," +
                    "'properties.bootstrap.servers' = '127.0.0.1:9092'," +
                    "'properties.group.id' = 'testGroup'," +
                    "'scan.startup.mode' = 'latest-offset'," +
                    "'format' = 'json')");
            tenv.executeSql("insert into res_test select\n" +
                    "name,\n" +
                    "score,\n" +
                    "class\n" +
                    "from\n" +
                    "(select \n" +
                    "name,\n" +
                    "score,\n" +
                    "class,\n" +
                    "row_number() over(partition by class order by score desc) as rown\n" +
                    "from scores)t1\n" +
                    "where rown<=1");
            tenv.execute("hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}