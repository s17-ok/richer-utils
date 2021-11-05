package udf;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * 标量函数
 */
public class SubString extends ScalarFunction {
    public String eval(String str, Integer a, Integer b) {
        return str.substring(a,b);
    }
}
