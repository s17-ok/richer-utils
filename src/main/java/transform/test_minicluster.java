package transform;//package transform;
//
//import com.pccc.aops.datalake.drag.DragMain;
//import org.apache.flink.api.common.functions.FilterFunction;
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.configuration.ConfigConstants;
//import org.apache.flink.configuration.ConfigOption;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.configuration.WebOptions;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.IterativeStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @Author: logan.yang
// * @Date: 2021/10/27/3:22 下午
// * @Description: No Description
// */
//
//public class test_minicluster {
//
//    public static void main(String[] args) throws Exception {
//        Logger log = LoggerFactory.getLogger(DragMain.class);
//
//        Configuration configuration = new Configuration();
////        System.setProperty("log.file","/Users/logan/software/flink12/flink-1.12.1/log/flink-logan-taskexecutor-1-LoganYangdeMacBook-Pro.local.log.9");
//        configuration.setString(ConfigConstants.TASK_MANAGER_LOG_PATH_KEY, "/Users/logan/log_flink");
//        configuration.setString(ConfigConstants.JOB_MANAGER_WEB_LOG_PATH_KEY, "/Users/logan/log_flink");
////        configuration.set(Config, "/Users/logan/log_flink");
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
////        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
//        log.info("log" + env);
//        env.setParallelism(1);
//        DataStream<Long> someIntegers = env.generateSequence(0, 4);
//
//        IterativeStream<Long> iteration = someIntegers.iterate();
//
//        DataStream<Long> minusOne = iteration.map(new MapFunction<Long, Long>() {
//            @Override
//            public Long map(Long value) throws Exception {
//                System.out.println("map:" +value);
//                return value - 1 ;
//            }
//        });
//
//        DataStream<Long> stillGreaterThanZero = minusOne.filter(new FilterFunction<Long>() {
//            @Override
//            public boolean filter(Long value) throws Exception {
//                System.out.println("filter:" +value);
//                return (value > 0);
//            }
//        });
//        iteration.closeWith(stillGreaterThanZero);
//        env.execute("123");
//    }
////        final
//
//}
