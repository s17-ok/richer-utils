package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HadoopSimpleAuth {
    public static void main(String[] args) throws IOException {

        System.setProperty("HADOOP_USER_NAME", "sunshiqi");
        Configuration configuration = new Configuration();
        configuration.addResource("local/core-site.xml");
        configuration.addResource("local/hdfs-site.xml");
        String s = configuration.get("fs.defaultFS");
        System.out.println(s);
        FileSystem fs = FileSystem.get(configuration);

        //hadoop http的身份认证方式
        String s1 = configuration.get("hadoop.http.authentication.type");

        //在使用 简单身份认证，是否允许匿名请求，默认为true
        System.out.println(configuration.get("hadoop.http.authentication.simple.anonymous.allowed"));

        String path = configuration.get("hadoop.http.authentication.signature.secret.file");


        //判断handoop是否是简单认证
        if ("simple".equals(configuration.get("hadoop.http.authentication.type"))
                && "true".equals("hadoop.http.authentication.simple.anonymous.allowed")){
            System.out.println("非 简单身份认证环境");
        }

        File file = new File("/Users/sunshiqi/Documents/software/hadoop_local/etc/hadoop/hadoop-http-auth-signature-secret");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String username = bufferedReader.readLine();
        System.out.println(username.replace("\"",""));

    }
}

