package transform;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;
import org.apache.log4j.*;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *  1.范型参数使用lambda表达式必须将参数的类型显示定义出来
 *  2.必须要有returns，指定返回类型
 *
 */
public class Transform {
    static Logger log = Logger.getLogger(Transform.class);

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        // socker port
        int port = 33333;
        // flink job name
        String jobName = "this is flink test";

        // 动态指定log file name
        FileAppender appender = (FileAppender)Logger.getRootLogger().getAppender("LOGFILE");
        appender.setFile("/Users/sunshiqi/Documents/project/myJobs/log/tmlog/"+jobName+".log");
        appender.activateOptions();


        Configuration conf = new Configuration();
        conf.setString("taskmanager.tmp.dirs","/Users/sunshiqi/Documents/project/myJobs/log/tmp");
        conf.setString("web.tmpdir","/Users/sunshiqi/Documents/project/myJobs/log/tmpWeb");
        conf.setString("taskmanager.log.path","/Users/sunshiqi/Documents/project/myJobs/log/tmlog/log.log");
        conf.setString("env.java.opts.taskmanager","-Xloggc:/Users/sunshiqi/Documents/project/myJobs/log/tmlog/taskmanager.out");

        conf.setString("env.log.dir","/Users/sunshiqi/Documents/project/myJobs/log/tmlog");
        //conf.setString("web.log.path","/Users/sunshiqi/Documents/project/myJobs/log/tmlog");
        //System.setProperty("log.file","/Users/sunshiqi/Documents/project/myJobs/log/logger.log");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(1);


        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStream<String> data = env.socketTextStream(host, port, '\n');
//          mapT(data);
//        flatT(data);
//        filterT(data);
//        keyByT(data);
            data.print();
        env.execute(jobName);

    }

//    public static void mapT(DataStream<String> data){
//        DataStream md = data.map(line -> Tuple2.of(line, 1)).returns(Types.TUPLE(Types.STRING, Types.INT));
//        data.map(new MapFunction<String, Object>() {
//            @Override
//            public Object map(String s) throws Exception {
//                return new Tuple2<String,Integer>(s,1);
//            }
//        }).print().setParallelism(1);
//
//        //md.print().setParallelism(1);
//    }
//
//    public static void flatT(DataStream<String> data){
//        DataStream<Object> flatMap = data.flatMap(new FlatMapFunction<String, Object>() {
//            @Override
//            public void flatMap(String s, Collector<Object> collector) throws Exception {
//                String[] split = s.split(",");
//                for (String str : split) {
//                    collector.collect(str);
//                }
//            }
//        });
//
//        data.flatMap((String str,Collector<String> collector)->{
//            String[] split = str.split(",");
//            for (String s : split) {
//                collector.collect(s);
//            }
//        }).returns(Types.STRING).print().setParallelism(1);
//
//       // flatMap.print().setParallelism(1);
//    }
//
//    public static void filterT(DataStream<String> data){
//        data.flatMap((String str,Collector<String> collector)->{
//            String[] split = str.split(",");
//            for (String s : split) {
//                collector.collect(s);
//            }
//        }).returns(Types.STRING).filter(str->str.contains("B")).print().setParallelism(1);
//    }
//
//    public static void keyByT(DataStream<String> data) {
//        DataStream<Tuple2<String, Integer>> md = data.map(line -> Tuple2.of(line, 1))
//                .returns(Types.TUPLE(Types.STRING, Types.INT));
//        md.keyBy(0).timeWindow(Time.seconds(10)).sum(1)
//                .print().setParallelism(1);
//
//    }


}
