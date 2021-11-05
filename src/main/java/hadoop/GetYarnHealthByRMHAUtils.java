package hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.RMHAUtils;
import java.io.IOException;

/**
 * @author richer.sun
 * @description yarn kerberos测试
 * @date 2021/9/17 6:00 下午
 */
@Slf4j
public class GetYarnHealthByRMHAUtils {
    public static void main(String[] args) throws  IOException, YarnException {
        YarnClient yarnClient = YarnClient.createYarnClient();
        Configuration conf = new Configuration();
//        conf.addResource("local/core-site.xml");
//        conf.addResource("local/hdfs-site.xml");
//        conf.addResource("local/yarn-site.xml");
        conf.addResource("hadoop-254-2.10.1/core-site.xml");
        conf.addResource("hadoop-254-2.10.1/hdfs-site.xml");
        conf.addResource("hadoop-254-2.10.1/yarn-site.xml");

        conf.set("fs.hdfs.impl.disable.cache","true");


        YarnConfiguration yarnConfiguration = new YarnConfiguration(conf);
        String activeRMHAId = RMHAUtils.findActiveRMHAId(yarnConfiguration);
        System.out.println(activeRMHAId);
        String id = conf.get("yarn.resourcemanager.webapp.address." + activeRMHAId);
        System.out.println(id);


//        yarnClient.init(yarnConfiguration);
//        yarnClient.start();
//        List<QueueInfo> allQueues = yarnClient.getAllQueues();
//        System.out.println(allQueues.stream());



    }
}
