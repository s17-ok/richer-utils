package kereros;

/**
 * @author richer.sun
 * @description hadoop增加kerberos认证
 * @date 2021/9/13 1:39 下午
 */
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.HAUtil;
import org.apache.hadoop.security.UserGroupInformation;

import static org.apache.hadoop.fs.CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHENTICATION;
import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class HdfsKerberos {
    public static void main(String[] args) throws Exception {

        System.setProperty("HADOOP_USER_NAME", "root");
        System.setProperty("user.name", "root");
        System.setProperty("java.security.krb5.conf","/Users/sunshiqi/Documents/project/myFlink/src/main/resources/krb5.conf");
        //打印调试日志
        System.setProperty("sun.security.krb5.debug", "true");
        Configuration conf = new Configuration();
        conf.addResource("local/core-site.xml");
        conf.addResource("local/hdfs-site.xml");
        //设置hadoop登陆认证为kerberos
        conf.set(HADOOP_SECURITY_AUTHENTICATION, UserGroupInformation.AuthenticationMethod.KERBEROS.name());
        conf.set("hadoop.rpc.protection","authentication");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab("root/cloudwise-01@CLOUDWISE.COM","/Users/sunshiqi/Documents/project/myFlink/src/main/resources/root.keytab");

        System.out.println(UserGroupInformation.getCurrentUser());
    }
}

