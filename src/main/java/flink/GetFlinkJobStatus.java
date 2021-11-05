package flink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import utils.HttpClientUtils;
import utils.HttpResult;

/**
 * @author richer.sun
 * @description 获取flink job 的状态
 * @date 2021/8/31 3:43 下午
 */
public class GetFlinkJobStatus {
    public static void main(String[] args) {
        String url = "http://127.0.0.1:8100/jobs/09e4165ce55472b4719cc1ebe1e87af7";
        try {
            HttpResult result = HttpClientUtils.doGet(url);
            JSONObject jsonObject = JSON.parseObject(result.getBody());
            System.out.println(jsonObject);
            //String  vertices = jsonObject.getString("vertices");
            String status = jsonObject.getJSONArray("vertices").getJSONObject(0).getString("status");
            System.out.println(status);
            //int duration = jsonObject.getInteger("duration");
          //  System.out.println(duration);
            //System.out.println(state);
        } catch (Exception e) {
            System.out.println("获取job的status失败");
            e.printStackTrace();
        }
    }
}
