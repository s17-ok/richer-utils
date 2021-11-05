package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.HAUtil;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author richer.sun
 * @description 通过HAUtils获取active namenode
 * @date 2021/11/1 4:06 下午
 */
public class GetHdfsHealthByHAUtils {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//        conf.addResource("hadoop-254-2.10.1/core-site.xml");
//        conf.addResource("hadoop-254-2.10.1/hdfs-site.xml");

        conf.addResource("local/core-site.xml");
        conf.addResource("local/hdfs-site.xml");
        System.out.println(conf.get("fs.defaultFS"));
        FileSystem fs = FileSystem.get(new URI(conf.get("fs.defaultFS")), conf, null);
        InetSocketAddress activeNM = HAUtil.getAddressOfActive(fs);
        System.out.println(activeNM.toString());
    }
}
