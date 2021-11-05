package kereros.get;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author richer.sun
 * @description 测试类 http请求访问kerberos
 * @date 2021/9/15 6:03 下午
 */
public class TestSendHttpRequestToUrl {
    private static Logger logger= LoggerFactory.getLogger(TestSendHttpRequestToUrl.class);
    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME", "root");
        String user ="HTTP/cloudwise-01@CLOUDWISE.COM";
        String keytab="/Users/sunshiqi/Documents/project/myFlink/src/main/resources/root.keytab";
        String krb5Location="/Users/sunshiqi/Documents/project/myFlink/src/main/resources/krb5.conf";
        //打印调试日志
        System.setProperty("sun.security.krb5.debug", "true");
        try{
            RequestKerberosUrlUtils restTest = new RequestKerberosUrlUtils(user,keytab,krb5Location, false);
            /**
             * httpclient认证kerberos 访问hdfs rest api
             */
            String url_liststatus="http://cloudwise-02:50070/webhdfs/v1/?op=GETFILESTATUS";
            HttpResponse httpResponse = restTest.callRestUrl(url_liststatus,user);
            /**
             * httpclient认证kerberos 访问yarn rest api
             */
            String allApp = "http://cloudwise-01:8088/ws/v1/cluster/apps";
            InputStream is = httpResponse.getEntity().getContent();
            System.out.println("Status code " + httpResponse.getStatusLine().getStatusCode());
            System.out.println("message is :"+ Arrays.deepToString(httpResponse.getAllHeaders()));
            System.out.println("string：\n"+new String(IOUtils.toByteArray(is), "UTF-8"));
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }
}