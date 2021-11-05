package hadoop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import utils.HadoopConstants;
import utils.HttpClientUtils;
import utils.HttpResult;
import utils.HttpStatusCodeConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
public class GetYarnHealthByHttp {
    public static void main(String[] args) throws IOException, YarnException {
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration configuration = new Configuration();
        configuration.addResource("58/core-site.xml");
        configuration.addResource("58/hdfs-site.xml");
        configuration.addResource("58/yarn-site.xml");

        YarnConfiguration yarnConfiguration = new YarnConfiguration();
        yarnConfiguration.addResource("58/yarn-site.xml");
        GetYarnHealthByHttp submitFlinkJob = new GetYarnHealthByHttp();
        String resourceManagerNode = submitFlinkJob.getResourceManagerNode(HadoopConstants.HTTP_PREFIX);
        if (resourceManagerNode == null || resourceManagerNode == "") {
            System.out.println("没有活跃的avtive resourcemanager");
            return;
        }
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConfiguration);
        yarnClient.start();
        YarnClusterMetrics yarnClusterMetrics = yarnClient.getYarnClusterMetrics();

        yarnClient.getAllQueues();
        System.out.println(yarnClusterMetrics);
    }

    public String getResourceManagerNode(String prefix){
        String rmPath = null;
        List<String> ipAndPorts = new ArrayList<>();
        Configuration conf = getConfiguration();
        Map<String, String> resourceNodes = conf.getPropsWithPrefix("yarn.resourcemanager.webapp.address");

        if (!resourceNodes.isEmpty()){
            log.info("get yarn.resourcemanager.webapp.address {}",resourceNodes);
            ipAndPorts = resourceNodes.values().stream().filter(ip->!ip.contains("0.0.0.0:")).collect(Collectors.toList());
        }

        if (ipAndPorts.isEmpty()){
            Map<String, String> hostname = conf.getPropsWithPrefix("yarn.resourcemanager.hostname");
            log.info("get yarn.resourcemanager.hostname {}",hostname);
            ipAndPorts = hostname.values().stream().filter(ip->!ip.contains("0.0.0.0")).map(s->s+":8088").collect(Collectors.toList());
        }
        rmPath = getActiveRM(ipAndPorts, rmPath);

        if (rmPath == null || rmPath == "") {
            try {
                //第一次循环没有获取到avtive rm,则等待30秒再次尝试
                Thread.sleep(10000);
                rmPath = getActiveRM(ipAndPorts,rmPath);
            } catch (InterruptedException e) {
                log.error("try to get active rm again {}", e.getMessage());
            }
        }
        log.info("get real resourcemangernode {}",rmPath != null ? prefix+rmPath : rmPath);
        return rmPath != null ? prefix+rmPath : rmPath;
    }

    /**
     * 从rm集合中获取avtive rm
     * @param ipAndPorts
     * @param rmPath
     * @return
     */
    public String getActiveRM(List<String> ipAndPorts, String rmPath){
        for (String ipAndPort : ipAndPorts) {
            String url = HadoopConstants.HTTP_PREFIX +ipAndPort+ HadoopConstants.YARN_INFO_SUFFIX;
            try {
                log.info("get rm node url,{}",url);
                HttpResult httpResult = HttpClientUtils.doGet(url,null,2000);
                int code = httpResult.getCode();

                if (code == HttpStatusCodeConstants.SUCCESS){
                    String body = httpResult.getBody();
                    JSONObject jsonObject = JSON.parseObject(body);

                    String haState = jsonObject.getJSONObject("clusterInfo").getString("haState");
                    if ("ACTIVE".equals(haState)){
                        rmPath = ipAndPort;
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("get resource manager url failed, {}",e.getMessage());
            }
        }
        return rmPath;
    }
    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addResource("58/core-site.xml");
        configuration.addResource("58/hdfs-site.xml");
        configuration.addResource("58/yarn-site.xml");
        return configuration;
    }
}
