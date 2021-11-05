package hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import utils.HadoopConstants;
import utils.HttpClientUtils;
import utils.HttpResult;
import utils.HttpStatusCodeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author richer.sun
 * @description 获取50070的状态
 * @date 2021/8/13 11:24 上午
 */
@Slf4j
public class GetHdfsHealthByHttpStatus {
    public static void main(String[] args) throws Exception {

        List<String> ipAndPorts = new ArrayList<>();
        String url = HadoopConstants.HTTP_PREFIX;

        Configuration conf = new Configuration();
        conf.addResource("hadoop-254-2.10.1/core-site.xml");
        conf.addResource("hadoop-254-2.10.1/hdfs-site.xml");

        Map<String, String> namenodes = conf.getPropsWithPrefix("dfs.namenode.http-address");

        if (!namenodes.isEmpty()){
            log.info("get namenode.http.address {}",namenodes);
            ipAndPorts = namenodes.values().stream().filter(ip->!ip.contains("0.0.0.0:")).collect(Collectors.toList());
        }

        if (ipAndPorts.isEmpty()){
            Map<String, String> adress = conf.getPropsWithPrefix("dfs.namenode.rpc-address");
            log.info("get dfs.namenode.rpc-address {}",adress);
            ipAndPorts = adress.values().stream().filter(ip->!ip.contains("0.0.0.0")).map(s->s.split(":")[0]+":50070").collect(Collectors.toList());
        }

        boolean healthCluster = isHealthCluster(ipAndPorts);
        System.out.println(healthCluster);
    }

    private static boolean isHealthCluster(List<String> ipAndPorts) {
        boolean isHealth = false;
        for (String ipAndPort : ipAndPorts) {
            String url = HadoopConstants.HTTP_PREFIX + ipAndPort + "/webhdfs/v1//?op=GETFILESTATUS";
            System.out.println(url);
            HttpResult httpResult;
            try {
                httpResult = HttpClientUtils.doGet(url);
                int statusCode = httpResult.getCode();
                System.out.println(statusCode);
                if (statusCode == HttpStatusCodeConstants.SUCCESS){
                    isHealth = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("hdfs集群异常，无法获取到avtive namenode");
            }
        }
        return isHealth;
    }


}
