package utils;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpStatus {
    public static int getStatus(String ip){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ip);
        int statusCode = 0;
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            System.out.println(httpResponse);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statusCode;
    }
    public static void main(String[] args) throws IOException {

        //int code = getStatus("http://127.0.0.1:8081/#/job/487b652dd3ffbfb3e92f606755d0e401");
        //System.out.println(code);

        String url = getJobDetailUrl("http://10.0.9.229:18132/proxy/application_1633931313604_0003", "4a220e6bfbe165136be1442a9252b201");
        HttpResult result = null;
        try {
            result = HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    public static String getJobDetailUrl(String flinkWebUI, String flinkJobId) {
        StringBuilder builder = new StringBuilder();
        return builder.append(flinkWebUI)
                .append("/jobs/")
                .append(flinkJobId)
                .append("")
                .toString();
    }
}
