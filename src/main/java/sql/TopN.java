package sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class TopN {
    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().useBlinkPlanner().build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);
        String sourceDDL = ""
                + "create table source_kafka "
                + "( "
                + "    userID String, "
                + "    eventType String, "
                + "    eventTime String, "
                + "    productID String "
                + ") with (" +
                "'connector' = 'kafka-0.11'," +
                "'topic'='source'," +
                "'properties.bootstrap.servers' = '127.0.0.1:9092'," +
                "'properties.group.id' = 'testGroup'," +
                "'scan.startup.mode' = 'latest-offset'," +
                "'format' = 'json')";

        tableEnv.executeSql(sourceDDL);

        String sinkDDL = ""
                + "create table sink_mysql "
                + "( "
                + "     datetime STRING, "
                + "     productID STRING, "
                + "     userID STRING, "
                + "     clickPV BIGINT "
                + ") with ( "
                + "    'connector.type' = 'jdbc', "
                + "    'connector.url' = 'jdbc:mysql://localhost:3306/mac', "
                + "    'connector.table' = 't_product_click_topn', "
                + "    'connector.username' = 'root', "
                + "    'connector.password' = 'root', "
                + "    'connector.write.flush.max-rows' = '50', "
                + "    'connector.write.flush.interval' = '2s', "
                + "    'connector.write.max-retries' = '3' "
                + ")";

        tableEnv.executeSql(sinkDDL);

        String execSQL = ""
                + "INSERT INTO sink_mysql "
                + "SELECT datetime, productID, userID, clickPV "
                + "FROM ( "
                + "  SELECT *, "
                + "     ROW_NUMBER() OVER (PARTITION BY datetime, productID ORDER BY clickPV desc) AS rownum "
                + "  FROM ( "
                + "        SELECT SUBSTRING(eventTime,1,13) AS datetime, "
                + "            productID, "
                + "            userID, "
                + "            count(1) AS clickPV "
                + "        FROM source_kafka "
                + "        GROUP BY SUBSTRING(eventTime,1,13), productID, userID "
                + "    ) a "
                + ") t "
                + "WHERE rownum <= 3";

        tableEnv.executeSql(execSQL);
        tableEnv.execute(TopN.class.getSimpleName());

    }
}
