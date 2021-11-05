package utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author marejune
 * @title: HttpResult
 * @projectName aops_aopc_dataflow_service
 * @description: TODO
 * @date 2020/10/105:18 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult {
    // 响应的状态码
    private int code;
    // 响应的响应体
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
